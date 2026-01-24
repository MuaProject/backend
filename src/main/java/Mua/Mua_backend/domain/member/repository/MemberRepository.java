package Mua.Mua_backend.domain.member.repository;

import Mua.Mua_backend.domain.member.entity.Member;
import Mua.Mua_backend.global.security.oauth.AuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByProviderAndProviderId(
            AuthProvider provider,
            String providerId
    );
}
