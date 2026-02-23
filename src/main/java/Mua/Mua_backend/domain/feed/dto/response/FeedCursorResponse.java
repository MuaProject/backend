package Mua.Mua_backend.domain.feed.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record FeedCursorResponse(
        List<FeedResponse> feeds,
        Long nextCursorId,
        LocalDateTime nextCursorCreatedAt,
        boolean hasNext
) {}