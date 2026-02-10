package Mua.Mua_backend.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record FcmTokenRequest(
        @NotBlank String fcmToken
) {
}
