package Mua.Mua_backend.domain.notification.dto.response;

import Mua.Mua_backend.domain.notification.entity.Notification;
import Mua.Mua_backend.domain.notification.entity.NotificationType;

import java.time.LocalDateTime;

public record NotificationResponse (
        Long notificationId,
        String message,
        boolean read,
        LocalDateTime createdAt,
        NotificationType targetType,
        Long targetId
) {
    public static NotificationResponse from(Notification notification) {
        return new NotificationResponse(
                notification.getNotificationId(),
                notification.getMessage(),
                notification.isRead(),
                notification.getCreatedAt(),
                notification.getTargetType(),
                notification.getTargetId()
        );
    }
}
