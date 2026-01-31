package Mua.Mua_backend.domain.comment.controller;

import Mua.Mua_backend.domain.comment.dto.request.CommentCreateRequest;
import Mua.Mua_backend.domain.comment.dto.response.CommentResponse;
import Mua.Mua_backend.domain.comment.service.CommentService;
import Mua.Mua_backend.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feeds/{feedId}/comments")
public class CommentController {

    private final CommentService commentService;


    // 댓글, 대댓글 작성
    @PostMapping
    public ResponseEntity<Void> createComment(
            @PathVariable Long feedId,
            @AuthenticationPrincipal Member member,
            @RequestBody CommentCreateRequest request
    ) {
        commentService.createComment(feedId, member.getId(), request);
        return ResponseEntity.ok().build();
    }

    // 댓글 목록 조회
    @GetMapping
    public ResponseEntity<List<CommentResponse>> getComments(
            @PathVariable Long feedId
    ) {
        return ResponseEntity.ok(commentService.getComments(feedId));
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal Member member
    ) {
        commentService.deleteComment(commentId, member.getId());
        return ResponseEntity.noContent().build();
    }
}
