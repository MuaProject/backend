package Mua.Mua_backend.domain.participation.repository;

import Mua.Mua_backend.domain.feed.entity.Feed;
import Mua.Mua_backend.domain.member.entity.Member;
import Mua.Mua_backend.domain.participation.entity.Participation;
import Mua.Mua_backend.domain.participation.entity.ParticipationStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {

    boolean existsByFeedAndApplicant(Feed feed, Member applicant);

    List<Participation> findByFeed_Id(Long feedId);

    List<Participation> findByApplicant_IdOrderByCreatedAtDescIdDesc(
            Long applicantId,
            Pageable pageable
    );

    List<Participation> findByApplicant_IdAndStatusOrderByCreatedAtDescIdDesc(
            Long applicantId,
            ParticipationStatus status,
            Pageable pageable
    );

    // 다음 페이지 (커서 기반)
    @Query("""
        select p from Participation p
        where p.applicant.id = :applicantId
          and (
            p.createdAt < :cursorTime
            or (p.createdAt = :cursorTime and p.id < :cursorId)
          )
        order by p.createdAt desc, p.id desc
    """)
    List<Participation> findNextPage(
            @Param("applicantId") Long applicantId,
            @Param("cursorTime") LocalDateTime cursorTime,
            @Param("cursorId") Long cursorId,
            Pageable pageable
    );

    @Query("""
        select p from Participation p
        where p.applicant.id = :applicantId
          and p.status = :status
          and (
            p.createdAt < :cursorTime
            or (p.createdAt = :cursorTime and p.id < :cursorId)
          )
        order by p.createdAt desc, p.id desc
    """)
    List<Participation> findNextPageWithStatus(
            @Param("applicantId") Long applicantId,
            @Param("status") ParticipationStatus status,
            @Param("cursorTime") LocalDateTime cursorTime,
            @Param("cursorId") Long cursorId,
            Pageable pageable
    );
}
