package Mua.Mua_backend.domain.feed.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record FeedCreateRequest (
        String image,

        @NotBlank(message = "제목은 필수입니다.")
        String title,

        @NotBlank(message = "장소는 필수입니다.")
        String playGround,

        @NotNull(message = "일정은 필수입니다.")
        @Future(message = "일정은 현재 시간 이후여야 합니다.")
        LocalDateTime playDate,

        Integer round,

        @Min(value = 0, message = "모집 인원은 0명 이상이어야 합니다.")
        Integer playCount,

        String description,
        Integer timer,
        String address,
        Double latitude,
        Double longitude
) {}