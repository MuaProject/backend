package Mua.Mua_backend.domain.feed.scheduler;

import Mua.Mua_backend.domain.feed.entity.Feed;
import Mua.Mua_backend.domain.feed.repository.FeedRepository;
import Mua.Mua_backend.domain.member.entity.Member;
import Mua.Mua_backend.domain.notification.entity.NotificationType;
import Mua.Mua_backend.domain.notification.service.NotificationService;
import Mua.Mua_backend.domain.participation.entity.ParticipationStatus;
import Mua.Mua_backend.domain.participation.repository.ParticipationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GameStartScheduler {

    private final FeedRepository feedRepository;
    private final ParticipationRepository participationRepository;
    private final NotificationService notificationService;

    // 게임 시작 알림 playDate <= now 승인된, 참가자(APPROVED)에게만 전송, 1분마다 실행
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void notifyGameStart() {
        LocalDateTime now = LocalDateTime.now();

        // 시작 시간이 된 Feed 조회
        List<Feed> feeds = feedRepository.findFeedsToStart(now);

        for (Feed feed : feeds) {

            // 승인된 참가자 조회
            List<Member> approvedMembers =
                    participationRepository.findMembersByFeedAndStatus(
                            feed.getId(),
                            ParticipationStatus.APPROVED
                    );

            // 알림 전송
            for (Member member : approvedMembers) {
                notificationService.sendNotification(
                        member,
                        NotificationType.GAME_STARTED,
                        feed.getId(),
                        "경기가 시작되었습니다."
                );
            }

            // 중복 알림 방지용 상태 변경
            feed.markGameStarted();
        }
    }
}
