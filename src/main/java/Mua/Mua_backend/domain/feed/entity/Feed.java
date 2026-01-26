package Mua.Mua_backend.domain.feed.entity;

import Mua.Mua_backend.domain.feed.dto.request.FeedUpdateRequest;
import Mua.Mua_backend.domain.member.entity.Member;
import Mua.Mua_backend.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name= "feed" )
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feed extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_id")
    private Long id;

    // 게시물 정보
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

    // 위치 정보
    @Column(name = "address")
    private String address;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    // 작성자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member writer;

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
            String address,
            Double latitude,
            Double longitude,
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
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void update(FeedUpdateRequest request) {
        updateContent(
                request.title(),
                request.image(),
                request.description(),
                request.timer()
        );
        updateSchedule(
                request.playGround(),
                request.playDate(),
                request.round()
        );
        updateLocation(
                request.address(),
                request.latitude(),
                request.longitude()
        );
    }

    // 게시물 기본 정보 수정
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

    // 일정/장소 수정
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

    // 위치 정보 수정
    public void updateLocation(
            String address,
            Double latitude,
            Double longitude
    ) {
        if (address != null) {
            this.address = address;
        }
        if (latitude != null) {
            this.latitude = latitude;
        }
        if (longitude != null) {
            this.longitude = longitude;
        }
    }

    private void validatePlayDate(LocalDateTime playDate) {
        if (playDate.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("지난 날짜로는 일정 변경이 불가능합니다.");
        }
    }
}
