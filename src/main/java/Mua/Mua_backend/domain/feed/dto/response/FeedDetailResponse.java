package Mua.Mua_backend.domain.feed.dto.response;

import java.time.LocalDateTime;

public record FeedDetailResponse(
        Long feedId,
        String image,
        String title,
        String playGround,
        LocalDateTime playDate,
        Integer round,
        Integer playCount,
        String description,
        Integer timer,
        WriterResponse writer
) {}