package Mua.Mua_backend.domain.feed.entity;

import Mua.Mua_backend.domain.member.entity.Member;
import Mua.Mua_backend.global.common.BaseTimeEntity;
import Mua.Mua_backend.global.exception.feed.FeedUpdateForbiddenException;
import Mua.Mua_backend.global.exception.feed.InvalidFeedLocationException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "feed")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feed extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_id")
    private Long id;

    @Column(name = "feed_image")
    private String image;

    @Column(name = "feed_title")
    private String title;

    @Column(name = "play_ground")
    private String playGround;

    @Column(name = "play_date")
    private LocalDateTime playDate;

    @Column(name = "round")
    private Integer round;

    @Column(name = "play_count")
    private Integer playCount;

    @Column(name = "description")
    private String description;

    @Column(name = "timer")
    private Integer timer;

    @Embedded
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member writer;

    @Column(nullable = false)
    private boolean gameStarted = false;

    @Builder
    private Feed(
            String image,
            String title,
            String playGround,
            LocalDateTime playDate,
            Integer round,
            Integer playCount,
            String description,
            Integer timer,
            Location location,
            Member writer
    ) {
        validatePlayDate(playDate);

        this.writer = writer;
        this.image = image;
        this.title = title;
        this.playGround = playGround;
        this.playDate = playDate;
        this.round = round;
        this.playCount = playCount;
        this.description = description;
        this.timer = timer;
        this.location = location;
    }

    public void update(
            String title,
            String image,
            String description,
            Integer timer,
            String playGround,
            LocalDateTime playDate,
            Integer round,
            Location location
    ) {
        updateContent(title, image, description, timer);
        updateSchedule(playGround, playDate, round);
        updateLocation(location);
    }

    public void assertWrittenBy(Member member) {
        if (!isWrittenBy(member)) {
            throw new FeedUpdateForbiddenException();
        }
    }

    public boolean isWrittenBy(Member member) {
        return writer.getId().equals(member.getId());
    }

    public void updateContent(
            String title,
            String image,
            String description,
            Integer timer
    ) {
        if (title != null) {
            this.title = title;
        }
        if (image != null) {
            this.image = image;
        }
        if (description != null) {
            this.description = description;
        }
        if (timer != null) {
            this.timer = timer;
        }
    }

    public void updateSchedule(
            String playGround,
            LocalDateTime playDate,
            Integer round
    ) {
        if (playDate != null) {
            validatePlayDate(playDate);
            this.playDate = playDate;
        }
        if (playGround != null) {
            this.playGround = playGround;
        }
        if (round != null) {
            this.round = round;
        }
    }

    public void updateLocation(Location location) {
        if (location != null) {
            this.location = location;
        }
    }

    private void validatePlayDate(LocalDateTime playDate) {
        if (playDate.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Play date cannot be in the past.");
        }
    }

    public void markGameStarted() {
        this.gameStarted = true;
    }
}
