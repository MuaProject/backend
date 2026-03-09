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
import Mua.Mua_backend.global.exception.feed.FeedUpdateForbiddenException;
import Mua.Mua_backend.global.exception.participation.AlreadyParticipatedException;
import Mua.Mua_backend.global.exception.participation.ParticipationNotFoundException;
import Mua.Mua_backend.global.exception.participation.SelfParticipationNotAllowedException;
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

    // 참가 신청
    public void apply(Long feedId, Member member) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new FeedNotFoundException());

        if (feed.getWriter().getId().equals(member.getId())) {
            throw new SelfParticipationNotAllowedException();
        }

        if (participationRepository.existsByFeedAndApplicant(feed, member)) {
            throw new AlreadyParticipatedException();
        }

        Participation participation = Participation.builder()
                .feed(feed)
                .applicant(member)
                .build();

        participationRepository.save(participation);

        String message = member.getNickname() + "님이 참가 신청했습니다.";
        commentService.createEventComment(
                feed.getId(),
                participation.getId(),
                message,
                CommentType.APPLY
        );
    }

    // 참가자 전체 조회
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

    // 참가 승인
    public void approve(Long participationId, Member writer) {
        Participation participation = participationRepository.findById(participationId)
                .orElseThrow(() -> new ParticipationNotFoundException());

        validateWriter(participation, writer);

        participation.approve();

        // 참가 확정 알림
        Member applicant = participation.getApplicant();
        Feed feed = participation.getFeed();

        String message = applicant.getNickname() + "님의 참가가 승인되었습니다.";

        commentService.createEventComment(
                feed.getId(),
                participation.getId(),
                message,
                CommentType.APPROVE
        );

        notificationService.sendNotification(
                applicant,
                NotificationType.PARTICIPATION_APPROVED,
                feed.getId(),
                "참가가 확정되었습니다."
        );
    }

    // 참가 거절
    public void reject(Long participationId, Member writer) {
        Participation participation = participationRepository.findById(participationId)
                .orElseThrow(() -> new ParticipationNotFoundException());

        validateWriter(participation, writer);

        participation.reject();

        // 참가 거절 알림
        Member applicant = participation.getApplicant();
        Feed feed = participation.getFeed();

        String message = applicant.getNickname() + "님의 참가가 거절되었습니다.";

        commentService.createEventComment(
                feed.getId(),
                participation.getId(),
                message,
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
            // 최초 조회
            participations = (participationStatus == null)
                    ? participationRepository
                    .findByApplicant_IdOrderByCreatedAtDescIdDesc(
                            member.getId(), pageable
                    )
                    : participationRepository
                    .findByApplicant_IdAndStatusOrderByCreatedAtDescIdDesc(
                            member.getId(), participationStatus, pageable
                    );
        } else {
            // 다음 페이지
            participations = (participationStatus == null)
                    ? participationRepository
                    .findNextPage(
                            member.getId(), cursorTime, cursorId, pageable
                    )
                    : participationRepository
                    .findNextPageWithStatus(
                            member.getId(), participationStatus,
                            cursorTime, cursorId, pageable
                    );
        }

        return participations.stream()
                .map(MyParticipationResponse::from)
                .toList();
    }

    private void validateWriter(Participation participation, Member writer) {
        if (!participation.getFeed().getWriter().getId().equals(writer.getId())) {
            throw new FeedUpdateForbiddenException();
        }
    }
}
