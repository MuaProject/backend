package Mua.Mua_backend.global.security.oauth;

import Mua.Mua_backend.domain.member.entity.Member;
import Mua.Mua_backend.global.security.entity.RefreshToken;
import Mua.Mua_backend.global.security.jwt.JwtTokenUtil;
import Mua.Mua_backend.global.security.repository.RefreshTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

	private final JwtTokenUtil jwtTokenUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    // 토큰 만료시간 (Refresh: 14일)
    private static final long REFRESH_TOKEN_EXPIRE_DAYS = 14; // 14일

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException {

        CustomOAuth2User principal = (CustomOAuth2User) authentication.getPrincipal();

        Member member = principal.getMember();

        String accessToken = jwtTokenUtil.generateAccessToken(member.getId(), member.getRole().name());

        // RefreshToken (랜덤 문자열)
        String refreshToken = java.util.UUID.randomUUID().toString();

        RefreshToken entity = refreshTokenRepository
                .findByMemberId(member.getId())
                .orElse(new RefreshToken(member.getId()));

        entity.rotate(
                refreshToken,
                LocalDateTime.now().plusDays(REFRESH_TOKEN_EXPIRE_DAYS)
        );

        refreshTokenRepository.save(entity);

        response.sendRedirect("http://localhost:3000/oauth/success?token=" + accessToken); // 프론트에 전달
	}
}
