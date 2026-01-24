package Mua.Mua_backend.global.security.repository;

import Mua.Mua_backend.global.security.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    // RefreshToken 재발급 시 사용
    Optional<RefreshToken> findByToken(String token);

    // 로그아웃 / 강제 로그아웃 시 사용
    Optional<RefreshToken> findByMemberId(Long memberId);
}
