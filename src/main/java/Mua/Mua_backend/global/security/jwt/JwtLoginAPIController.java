package Mua.Mua_backend.global.security.jwt;

import Mua.Mua_backend.domain.member.entity.Member;
import Mua.Mua_backend.global.exception.member.ForbiddenException;
import Mua.Mua_backend.global.security.entity.RefreshToken;
import Mua.Mua_backend.global.security.repository.RefreshTokenRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
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
@Transactional
public class JwtLoginAPIController {

    private final JwtTokenUtil jwtTokenUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request,
                                     HttpServletResponse response) {

        String refreshToken = null;

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                }
            }
        }

        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Refresh token missing");
        }

        RefreshToken storedToken = refreshTokenRepository
                .findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (storedToken.isExpired()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Refresh token expired");
        }

        Long memberId = storedToken.getMemberId();

        String newAccessToken =
                jwtTokenUtil.generateAccessToken(memberId, "USER");

        // Rotation
        String newRefreshToken = java.util.UUID.randomUUID().toString();

        storedToken.rotate(
                newRefreshToken,
                LocalDateTime.now().plusDays(14)
        );

        refreshTokenRepository.save(storedToken);

        // 새 RefreshToken 쿠키 다시 내려줌
        Cookie newCookie = new Cookie("refreshToken", newRefreshToken);
        newCookie.setHttpOnly(true);
        newCookie.setSecure(false);
        newCookie.setPath("/");
        newCookie.setMaxAge(60 * 60 * 24 * 14);

        response.addCookie(newCookie);

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + newAccessToken)
                .build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @AuthenticationPrincipal Member member,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        String refreshToken = null;

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                }
            }
        }

        if (refreshToken == null) {
            return ResponseEntity.noContent().build();
        }

        RefreshToken storedToken = refreshTokenRepository
                .findByToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));

        if (!storedToken.getMemberId().equals(member.getId())) {
            throw new ForbiddenException();
        }

        refreshTokenRepository.deleteByToken(refreshToken);

        // 쿠키 삭제
        Cookie deleteCookie = new Cookie("refreshToken", null);
        deleteCookie.setMaxAge(0);
        deleteCookie.setPath("/");
        response.addCookie(deleteCookie);

        return ResponseEntity.noContent().build();
    }
}
