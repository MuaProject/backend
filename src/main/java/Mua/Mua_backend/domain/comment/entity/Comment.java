package Mua.Mua_backend.domain.comment.entity;

import Mua.Mua_backend.domain.feed.entity.Feed;
import Mua.Mua_backend.domain.member.entity.Member;
import Mua.Mua_backend.domain.participation.entity.ParticipationStatus;
import Mua.Mua_backend.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "comment",
        indexes = {
                @Index(name = "idx_comment_feed", columnList = "feed_id"),
                @Index(name = "idx_comment_parent", columnList = "parent_id")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(name = "description", nullable = false, length = 100)
    private String description;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "depth", nullable = false)
    private Integer depth;

    @Enumerated(EnumType.STRING)
    @Column(name = "comment_type",nullable = false)
    private CommentType commentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id", nullable = false)
    private Feed feed;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @Column(name = "participation_id")
    private Long participationId;

    @Builder(access = AccessLevel.PRIVATE)
    private Comment(
            String description,
            Long parentId,
            Integer depth,
            CommentType commentType,
            Member member,
            Feed feed,
            Long participationId
    ) {
        this.description = description;
        this.parentId = parentId;
        this.depth = depth;
        this.commentType = commentType;
        this.member = member;
        this.feed = feed;
        this.participationId = participationId;
    }

    // USER 댓글 생성
    public static Comment createUserComment(
            String description,
            Feed feed,
            Member member,
            Long parentId
    ) {
        return Comment.builder()
                .description(description)
                .parentId(parentId)
                .depth(parentId == null ? 0 : 1)
                .commentType(CommentType.USER)
                .member(member)
                .feed(feed)
                .build();
    }

    // SYSTEMEVENT 댓글 생성
    public static Comment createEventComment(
            String description,
            Feed feed,
            Long participationId,
            CommentType type
    ) {
        return Comment.builder()
                .description(description)
                .parentId(null)
                .depth(0)
                .commentType(type)
                .member(null)
                .feed(feed)
                .participationId(participationId)
                .build();
    }

    public void delete() {
        this.isDeleted = true;
        this.description = "삭제된 댓글입니다.";
    }
}
