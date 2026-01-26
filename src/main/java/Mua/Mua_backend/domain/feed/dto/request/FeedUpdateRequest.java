package Mua.Mua_backend.domain.feed.dto.request;

import Mua.Mua_backend.global.annotation.NotBlankIfPresent;
import jakarta.validation.constraints.Future;

import java.time.LocalDateTime;

public record FeedUpdateRequest (
    @NotBlankIfPresent(message = "제목은 비워둘 수 없습니다.")
    String title,

    String image,
    String description,
    Integer timer,

    @NotBlankIfPresent(message = "장소는 비워둘 수 없습니다.")
    String playGround,

    @Future(message = "일정은 현재 시간 이후여야 합니다.")
    LocalDateTime playDate,

    Integer round,
    String address,
    Double latitude,
    Double longitude
) {
    public boolean isAllNull() {
        return title == null
                && image == null
                && description == null
                && timer == null
                && playGround == null
                && playDate == null
                && round == null
                && address == null
                && latitude == null
                && longitude == null;
    }
}
