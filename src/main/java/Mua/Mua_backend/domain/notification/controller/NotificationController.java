package Mua.Mua_backend.domain.notification.controller;

import Mua.Mua_backend.domain.member.entity.Member;
import Mua.Mua_backend.domain.notification.dto.response.NotificationResponse;
import Mua.Mua_backend.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    // 알림 목록 조회
    @GetMapping
    public List<NotificationResponse> getMyNotifications(
            @AuthenticationPrincipal Member member
    ) {
        return notificationService.getMyNotifications(member);
    }

    // 알림 읽음 처리
    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<Void> readNotification(
            @PathVariable Long notificationId,
            @AuthenticationPrincipal Member member
    ) {
        notificationService.readNotification(notificationId, member);
        return ResponseEntity.ok().build();
    }

    // 알림 전체 삭제
    @DeleteMapping
    public ResponseEntity<Void> deleteMyNotifications(
            @AuthenticationPrincipal Member member
    ) {
        notificationService.deleteMyNotifications(member);
        return ResponseEntity.noContent().build();
    }
}
