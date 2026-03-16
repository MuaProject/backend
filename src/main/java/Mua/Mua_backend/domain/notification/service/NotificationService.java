package Mua.Mua_backend.domain.notification.service;

import Mua.Mua_backend.domain.member.entity.Member;
import Mua.Mua_backend.domain.notification.dto.response.NotificationResponse;
import Mua.Mua_backend.domain.notification.entity.Notification;
import Mua.Mua_backend.domain.notification.entity.NotificationType;
import Mua.Mua_backend.domain.notification.repository.NotificationRepository;
import Mua.Mua_backend.global.fcm.FcmService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final FcmService fcmService;

    // 알림 생성 + FCM 전송
    public void sendNotification(
            Member targetMember,
            NotificationType type,
            Long targetId,
            String message
    ) {
        Notification notification = Notification.builder()
                .member(targetMember)
                .message(message)
                .targetType(type)
                .targetId(targetId)
                .build();

        notificationRepository.save(notification);

        fcmService.send(targetMember.getFcmToken(), message);
    }

    // 알림 목록 조회
    public List<NotificationResponse> getMyNotifications(Member member) {
        return notificationRepository
                .findByMemberOrderByCreatedAtDesc(member)
                .stream()
                .map(NotificationResponse::from)
                .toList();
    }

    // 읽음 처리
    public void readNotification(Long notificationId, Member member) {
        Notification notification = notificationRepository
                .findById(notificationId)
                .orElseThrow();

        if (!notification.getMember().getId().equals(member.getId())) {
            throw new RuntimeException("권한 없음");
        }

        notification.markAsRead();
    }

    // 알림 전체 삭제
    public void deleteMyNotifications(Member member) {
        notificationRepository.deleteByMember(member);
    }
}
