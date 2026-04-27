package Mua.Mua_backend.domain.participation.service;

import Mua.Mua_backend.domain.comment.entity.CommentType;
import Mua.Mua_backend.domain.comment.service.CommentService;
import Mua.Mua_backend.domain.feed.entity.Feed;
import Mua.Mua_backend.domain.feed.repository.FeedRepository;
import Mua.Mua_backend.domain.member.entity.Member;
import Mua.Mua_backend.domain.notification.entity.NotificationType;
import Mua.Mua_backend.domain.notification.service.NotificationService;
import Mua.Mua_backend.domain.participation.dto.response.MyParticipationResponse;
import Mua.Mua_backend.domain.participation.dto.response.ParticipationResponse;
import Mua.Mua_backend.domain.participation.entity.Participation;
import Mua.Mua_backend.domain.participation.entity.ParticipationStatus;
import Mua.Mua_backend.domain.participation.repository.ParticipationRepository;
import Mua.Mua_backend.global.exception.feed.FeedNotFoundException;
import Mua.Mua_backend.global.exception.participation.AlreadyParticipatedException;
import Mua.Mua_backend.global.exception.participation.ParticipationNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ParticipationService {

    private final ParticipationRepository participationRepository;
    private final FeedRepository feedRepository;
    private final CommentService commentService;
    private final NotificationService notificationService;

    public void apply(Long feedId, Member member) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(FeedNotFoundException::new);

        if (participationRepository.existsByFeedAndApplicant(feed, member)) {
            throw new AlreadyParticipatedException();
        }

        Participation participation = Participation.apply(feed, member);
        participationRepository.save(participation);

        String message = member.getNickname() + "님이 참가 요청했습니다.";
        commentService.createEventComment(
                feed.getId(),
                participation.getId(),
                message,
                CommentType.APPLY
        );
    }

    @Transactional(readOnly = true)
    public List<ParticipationResponse> getParticipations(Long feedId) {
        List<Participation> participations =
                participationRepository.findByFeed_Id(feedId);

        return participations.stream()
                .map(p -> new ParticipationResponse(
                        p.getId(),
                        p.getApplicant().getId(),
                        p.getApplicant().getNickname(),
                        p.getStatus(),
                        p.getAppliedAt()
                ))
                .toList();
    }

    public void approve(Long participationId, Member writer) {
        Participation participation = participationRepository.findById(participationId)
                .orElseThrow(ParticipationNotFoundException::new);

        participation.assertManageableBy(writer);
        participation.approve();

        Member applicant = participation.getApplicant();
        Feed feed = participation.getFeed();

        commentService.updateEventComment(
                participation.getId(),
                CommentType.APPROVE
        );

        notificationService.sendNotification(
                applicant,
                NotificationType.PARTICIPATION_APPROVED,
                feed.getId(),
                "참가가 확정되었습니다."
        );
    }

    public void reject(Long participationId, Member writer) {
        Participation participation = participationRepository.findById(participationId)
                .orElseThrow(ParticipationNotFoundException::new);

        participation.assertManageableBy(writer);
        participation.reject();

        Member applicant = participation.getApplicant();
        Feed feed = participation.getFeed();

        commentService.updateEventComment(
                participation.getId(),
                CommentType.REJECT
        );

        notificationService.sendNotification(
                applicant,
                NotificationType.PARTICIPATION_REJECTED,
                feed.getId(),
                "참가가 거절되었습니다."
        );
    }

    @Transactional(readOnly = true)
    public List<MyParticipationResponse> getMyParticipations(
            Member member,
            String status,
            LocalDateTime cursorTime,
            Long cursorId,
            int size
    ) {
        ParticipationStatus participationStatus =
                (status == null) ? null : ParticipationStatus.valueOf(status);

        Pageable pageable = PageRequest.of(0, size);
        boolean hasCursor = cursorTime != null && cursorId != null;

        List<Participation> participations;

        if (!hasCursor) {
            participations = (participationStatus == null)
                    ? participationRepository.findByApplicant_IdOrderByCreatedAtDescIdDesc(
                    member.getId(), pageable
            )
                    : participationRepository.findByApplicant_IdAndStatusOrderByCreatedAtDescIdDesc(
                    member.getId(), participationStatus, pageable
            );
        } else {
            participations = (participationStatus == null)
                    ? participationRepository.findNextPage(
                    member.getId(), cursorTime, cursorId, pageable
            )
                    : participationRepository.findNextPageWithStatus(
                    member.getId(), participationStatus, cursorTime, cursorId, pageable
            );
        }

        return participations.stream()
                .map(MyParticipationResponse::from)
                .toList();
    }
}
