package Mua.Mua_backend.global.security.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    private Long memberId;

    @Column(nullable = false, length = 500)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiration;

    public RefreshToken(Long memberId) {
        this.memberId = memberId;
    }

    public RefreshToken(Long memberId, String token, LocalDateTime expiration) {
        this.memberId = memberId;
        this.token = token;
        this.expiration = expiration;
    }

    public void rotate(String newToken, LocalDateTime newExpiration) {
        this.token = newToken;
        this.expiration = newExpiration;
    }

    public boolean isExpired() {
        return expiration.isBefore(LocalDateTime.now());
    }
}
