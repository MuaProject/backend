package Mua.Mua_backend.domain.feed.repository;

import Mua.Mua_backend.domain.feed.entity.Feed;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface FeedRepository extends JpaRepository<Feed, Long> {

    @Query("""
    SELECT f FROM Feed f
    WHERE
        (:cursorCreatedAt IS NULL OR
         (f.createdAt < :cursorCreatedAt OR
          (f.createdAt = :cursorCreatedAt AND f.id < :cursorId)))
    ORDER BY f.createdAt DESC, f.id DESC
""")
    List<Feed> findLatestFeeds(
            @Param("cursorId") Long cursorId,
            @Param("cursorCreatedAt") LocalDateTime cursorCreatedAt,
            Pageable pageable
    );

    @Query("""
    SELECT f FROM Feed f
    WHERE
        (:cursorId IS NULL OR f.id < :cursorId)
    ORDER BY
        (
            (f.latitude - :latitude) * (f.latitude - :latitude) +
            (f.longitude - :longitude) * (f.longitude - :longitude)
        ) ASC,
        f.id DESC
""")
    List<Feed> findNearestFeeds(
            @Param("cursorId") Long cursorId,
            @Param("latitude") Double latitude,
            @Param("longitude") Double longitude,
            Pageable pageable
    );

    @Query("""
        select f from Feed f
        where f.playDate <= :now
        and f.gameStarted = false
    """)
    List<Feed> findFeedsToStart(LocalDateTime now);
}
