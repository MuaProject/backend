package Mua.Mua_backend.domain.feed.dto.response;

import java.time.LocalDateTime;

public record FeedResponse (
        Long feedId,
        String title,
        String image,
        String playGround,
        LocalDateTime playDate,
        Integer playCount,
        LocalDateTime createdAt
) {}
