package Mua.Mua_backend.domain.notification.service;

import Mua.Mua_backend.domain.member.entity.Member;
import Mua.Mua_backend.domain.notification.entity.Notification;
import Mua.Mua_backend.domain.notification.entity.NotificationType;
import Mua.Mua_backend.domain.notification.repository.NotificationRepository;
import Mua.Mua_backend.global.fcm.FcmService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final FcmService fcmService;

    public void sendNotification(
            Member targetMember,
            NotificationType type,
            Long targetId,
            String message
    ) {
        Notification notification = new Notification(
                targetMember,
                message,
                type,
                targetId
        );

        notificationRepository.save(notification);

        fcmService.send(targetMember.getFcmToken(), message);
    }
}
