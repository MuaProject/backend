package Mua.Mua_backend.global.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

// OncePerRequestFilter : 매번 들어갈 때마다 체크 해주는 필터
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private static final String TOKEN_COOKIE_NAME = "jwtToken";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 토큰 추출 (헤더 우선, 없으면 쿠키)
        String token = resolveToken(request);

        // 토큰이 없으면 비로그인 요청 → 그냥 통과
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // JWT 검증 (서명 + 만료)
        if (!jwtTokenUtil.validateToken(token)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired JWT");
            return;
        }

        // JWT에서 사용자 정보 추출
        Long memberId = jwtTokenUtil.getMemberId(token);
        String role = jwtTokenUtil.getRole(token);

        // 인증 객체 생성 (ROLE_ prefix 필수)
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        memberId,
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + role))
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 다음 필터로
        filterChain.doFilter(request, response);
    }

    /**
     * Authorization 헤더 → 없으면 Cookie(jwtToken)에서 토큰 추출
     */
    private String resolveToken(HttpServletRequest request) {

        // Authorization Header
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        // Cookie
        if (request.getCookies() == null) {
            return null;
        }

        return Arrays.stream(request.getCookies())
                .filter(cookie -> TOKEN_COOKIE_NAME.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}
