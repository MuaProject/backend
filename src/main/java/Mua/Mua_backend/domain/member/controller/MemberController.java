package Mua.Mua_backend.domain.member.controller;

import Mua.Mua_backend.domain.member.dto.request.FcmTokenRequest;
import Mua.Mua_backend.domain.member.entity.Member;
import Mua.Mua_backend.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    // FCM 토큰 저장 / 갱신, 로그인 성공 직후 호출, 기존 토큰 있으면 덮어씀
    @PostMapping("/fcm-token")
    public ResponseEntity<Void> saveFcmToken(
            @AuthenticationPrincipal Member member,
            @RequestBody @Valid FcmTokenRequest request
    ) {
        memberService.updateFcmToken(member, request.fcmToken());
        return ResponseEntity.noContent().build();
    }

    // FCM 토큰 삭제 (로그아웃 시)
    @DeleteMapping("/fcm-token")
    public ResponseEntity<Void> deleteFcmToken(
            @AuthenticationPrincipal Member member
    ) {
        memberService.removeFcmToken(member);
        return ResponseEntity.noContent().build();
    }
}
