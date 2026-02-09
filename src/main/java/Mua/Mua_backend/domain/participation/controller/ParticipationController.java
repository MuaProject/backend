package Mua.Mua_backend.domain.participation.controller;

import Mua.Mua_backend.domain.member.entity.Member;
import Mua.Mua_backend.domain.participation.controller.docs.ParticipationControllerDocs;
import Mua.Mua_backend.domain.participation.dto.response.MyParticipationResponse;
import Mua.Mua_backend.domain.participation.dto.response.ParticipationResponse;
import Mua.Mua_backend.domain.participation.service.ParticipationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ParticipationController implements ParticipationControllerDocs {

    private final ParticipationService participationService;

    // 참가 신청
    @PostMapping("/feeds/{feedId}/participations")
    public ResponseEntity<Void> apply(
            @PathVariable Long feedId,
            @AuthenticationPrincipal Member member
    ) {
        participationService.apply(feedId, member);
        return ResponseEntity.ok().build();
    }

    // 참가자 전체 조회
    @GetMapping("/feeds/{feedId}/participations")
    public ResponseEntity<List<ParticipationResponse>> getParticipations(
            @PathVariable Long feedId
    ) {
        return ResponseEntity.ok(
                participationService.getParticipations(feedId)
        );
    }

    // 참가 승인
    @PatchMapping("/participations/{id}/approve")
    public ResponseEntity<Void> approve(
            @PathVariable Long id,
            @AuthenticationPrincipal Member member
    ) {
        participationService.approve(id, member);
        return ResponseEntity.ok().build();
    }

    // 참가 거절
    @PatchMapping("/participations/{id}/reject")
    public ResponseEntity<Void> reject(
            @PathVariable Long id,
            @AuthenticationPrincipal Member member
    ) {
        participationService.reject(id, member);
        return ResponseEntity.ok().build();
    }

    // 내가 참가한 경기(기록) 조회
    @GetMapping("/me/participations")
    public ResponseEntity<List<MyParticipationResponse>> getMyParticipations(
            @AuthenticationPrincipal Member member,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) LocalDateTime cursorTime,
            @RequestParam(required = false) Long cursorId,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                participationService.getMyParticipations(
                        member, status, cursorTime, cursorId, size
                )
        );
    }


}
