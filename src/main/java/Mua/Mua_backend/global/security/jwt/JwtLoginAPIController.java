package Mua.Mua_backend.global.security.jwt;

import Mua.Mua_backend.domain.member.entity.Member;
import Mua.Mua_backend.global.exception.member.ForbiddenException;
import Mua.Mua_backend.global.security.entity.RefreshToken;
import Mua.Mua_backend.global.security.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
public class JwtLoginAPIController {

    private final JwtTokenUtil jwtTokenUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(
            @RequestHeader("Refresh-Token") String refreshToken
    ) {
        try {
            // DB에서 RefreshToken 조회
            RefreshToken storedToken = refreshTokenRepository
                    .findByToken(refreshToken)
                    .orElseThrow(() -> new RuntimeException("Refresh token not found"));

            // 만료 체크
            if (storedToken.isExpired()) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Refresh token expired");
            }

            Long memberId = storedToken.getMemberId();

            // 새 AccessToken 발급 (JWT)
            String newAccessToken =
                    jwtTokenUtil.generateAccessToken(memberId, "USER");

            // RefreshToken 회전
            String newRefreshToken = java.util.UUID.randomUUID().toString();
            storedToken.rotate(
                    newRefreshToken,
                    LocalDateTime.now().plusDays(14)
            );
            refreshTokenRepository.save(storedToken);

            // 응답
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + newAccessToken)
                    .header("Refresh-Token", newRefreshToken)
                    .build();

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid refresh token");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @AuthenticationPrincipal Member member,
            @RequestHeader("Refresh-Token") String refreshToken
    ) {

        RefreshToken storedToken = refreshTokenRepository
                .findByToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));

        // 본인 토큰인지 검증
        if (!storedToken.getMemberId().equals(member.getId())) {
            throw new ForbiddenException();
        }

        // 토큰 삭제
        refreshTokenRepository.deleteByToken(refreshToken);

        return ResponseEntity.noContent().build();
    }
}
