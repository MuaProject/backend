package Mua.Mua_backend.domain.notification.controller;

import Mua.Mua_backend.domain.member.entity.Member;
import Mua.Mua_backend.domain.notification.dto.response.NotificationResponse;
import Mua.Mua_backend.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationRepository notificationRepository;

    @GetMapping
    public List<NotificationResponse> getMyNotifications(
            @AuthenticationPrincipal Member member
    ) {
        return notificationRepository
                .findByMemberOrderByCreatedAtDesc(member)
                .stream()
                .map(NotificationResponse::from)
                .toList();
    }

    // 알림 전체 삭제
    @DeleteMapping
    public ResponseEntity<Void> deleteMyNotifications(
            @AuthenticationPrincipal Member member
    ) {
        notificationRepository.deleteByMember(member);
        return ResponseEntity.noContent().build();
    }
}
