package Mua.Mua_backend.domain.participation.entity;

import Mua.Mua_backend.domain.feed.entity.Feed;
import Mua.Mua_backend.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "participation")
public class Participation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "participation_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_id", nullable = false)
    private Member applicant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id", nullable = false)
    private Feed feed;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ParticipationStatus status;

    @Column(name = "applied_at", nullable = false)
    private LocalDateTime appliedAt;

    @Builder
    private Participation(Member applicant, Feed feed) {
        this.applicant = applicant;
        this.feed = feed;
        this.status = ParticipationStatus.PENDING;
        this.appliedAt = LocalDateTime.now();
    }

    public void approve() {
        this.status = ParticipationStatus.APPROVED;
    }

    public void reject() {
        this.status = ParticipationStatus.REJECTED;
    }
}
