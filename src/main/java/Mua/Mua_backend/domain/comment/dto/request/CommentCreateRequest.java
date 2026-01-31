package Mua.Mua_backend.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CommentCreateRequest(
        @NotBlank(message = "댓글 내용은 필수입니다.")
        String description,

        Long parentId // null이면 일반 댓글
) {}