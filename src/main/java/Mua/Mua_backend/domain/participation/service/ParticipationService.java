package Mua.Mua_backend.domain.participation.service;

import Mua.Mua_backend.domain.comment.service.CommentService;
import Mua.Mua_backend.domain.feed.entity.Feed;
import Mua.Mua_backend.domain.feed.repository.FeedRepository;
import Mua.Mua_backend.domain.member.entity.Member;
import Mua.Mua_backend.domain.participation.dto.response.ParticipationResponse;
import Mua.Mua_backend.domain.participation.entity.Participation;
import Mua.Mua_backend.domain.participation.repository.ParticipationRepository;
import Mua.Mua_backend.global.exception.feed.FeedNotFoundException;
import Mua.Mua_backend.global.exception.feed.FeedUpdateForbiddenException;
import Mua.Mua_backend.global.exception.participation.AlreadyParticipatedException;
import Mua.Mua_backend.global.exception.participation.ParticipationNotFoundException;
import Mua.Mua_backend.global.exception.participation.SelfParticipationNotAllowedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ParticipationService {

    private final ParticipationRepository participationRepository;
    private final FeedRepository feedRepository;
    private final CommentService commentService;

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
        commentService.createSystemComment(feed.getId(), message);
    }

    // 참가자 전체 조회
    @Transactional(readOnly = true)
    public List<ParticipationResponse> getParticipations(Long feedId) {
        List<Participation> participations =
                participationRepository.findByFeedId(feedId);

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
    }

    // 참가 거절
    public void reject(Long participationId, Member writer) {
        Participation participation = participationRepository.findById(participationId)
                .orElseThrow(() -> new ParticipationNotFoundException());

        validateWriter(participation, writer);

        participation.reject();
    }

    private void validateWriter(Participation participation, Member writer) {
        if (!participation.getFeed().getWriter().getId().equals(writer.getId())) {
            throw new FeedUpdateForbiddenException();
        }
    }
}
