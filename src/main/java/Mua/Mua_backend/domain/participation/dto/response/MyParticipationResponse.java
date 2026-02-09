package Mua.Mua_backend.domain.participation.dto.response;

import Mua.Mua_backend.domain.participation.entity.Participation;
import Mua.Mua_backend.domain.participation.entity.ParticipationStatus;

import java.time.LocalDateTime;

public record MyParticipationResponse(
        Long participationId,
        Long feedId,
        String feedTitle,
        String playGround,
        LocalDateTime playDate,
        ParticipationStatus status
) {
    public static MyParticipationResponse from(Participation participation) {
        return new MyParticipationResponse(
                participation.getId(),
                participation.getFeed().getId(),
                participation.getFeed().getTitle(),
                participation.getFeed().getPlayGround(),
                participation.getFeed().getPlayDate(),
                participation.getStatus()
        );
    }

}