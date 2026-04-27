package Mua.Mua_backend.domain.feed.repository;

import Mua.Mua_backend.domain.feed.entity.Feed;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface FeedRepository extends JpaRepository<Feed, Long> {

    /**
     * 최신 피드 조회 (커서 기반 페이징)
     */
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

    /**
     * 거리 기반 피드 조회 (가까운 순)
     */
    @Query("""
        SELECT f FROM Feed f
        WHERE
            (:cursorId IS NULL OR f.id < :cursorId)
            AND f.location.latitude IS NOT NULL
            AND f.location.longitude IS NOT NULL
        ORDER BY
            (
                (f.location.latitude - :latitude) * (f.location.latitude - :latitude) +
                (f.location.longitude - :longitude) * (f.location.longitude - :longitude)
            ) ASC,
            f.id DESC
    """)
    List<Feed> findNearestFeeds(
            @Param("cursorId") Long cursorId,
            @Param("latitude") Double latitude,
            @Param("longitude") Double longitude,
            Pageable pageable
    );

    /**
     * 게임 시작 대상 피드 조회
     */
    @Query("""
        SELECT f FROM Feed f
        WHERE f.playDate <= :now
          AND f.gameStarted = false
    """)
    List<Feed> findFeedsToStart(
            @Param("now") LocalDateTime now
    );
}
