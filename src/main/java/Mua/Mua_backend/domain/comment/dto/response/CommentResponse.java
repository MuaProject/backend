package Mua.Mua_backend.domain.comment.dto.response;

import Mua.Mua_backend.domain.comment.entity.Comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record CommentResponse(
        Long commentId,
        Long feedId,
        String description,
        Long parentId,
        Integer depth,
        String commentType,
        Long memberId,
        String nickname,
        LocalDateTime createdAt,
        List<CommentResponse> children
) {
    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getFeed().getId(),
                comment.getDescription(),
                comment.getParentId(),
                comment.getDepth(),
                comment.getCommentType().name(),
                comment.getMember() != null ? comment.getMember().getId() : null,
                comment.getMember() != null ? comment.getMember().getNickname() : "SYSTEM",
                comment.getCreatedAt(),
                new ArrayList<>()
        );
    }
}