package Mua.Mua_backend.domain.participation.dto.response;

import Mua.Mua_backend.domain.participation.entity.ParticipationStatus;

import java.time.LocalDateTime;

public record ParticipationResponse(
        Long participationId,
        Long applicantId,
        String applicantNickname,
        ParticipationStatus status,
        LocalDateTime appliedAt
) {}