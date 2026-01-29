package Mua.Mua_backend.domain.participation.repository;

import Mua.Mua_backend.domain.feed.entity.Feed;
import Mua.Mua_backend.domain.member.entity.Member;
import Mua.Mua_backend.domain.participation.entity.Participation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {

    boolean existsByFeedAndApplicant(Feed feed, Member applicant);

    List<Participation> findByFeedId(Long feedId);
}
