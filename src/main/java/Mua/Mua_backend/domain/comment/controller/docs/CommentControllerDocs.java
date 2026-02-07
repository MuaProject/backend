package Mua.Mua_backend.domain.comment.controller.docs;

import Mua.Mua_backend.domain.comment.dto.request.CommentCreateRequest;
import Mua.Mua_backend.domain.comment.dto.response.CommentResponse;
import Mua.Mua_backend.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Comment", description = "댓글 관련 API")
@SecurityRequirement(name = "JWT")
public interface CommentControllerDocs {

    @Operation(
            summary = "댓글/대댓글 작성",
            description = "특정 피드에 댓글 또는 대댓글을 작성합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "댓글 작성 성공"
    )
    ResponseEntity<Void> createComment(
            @Parameter(description = "피드 ID") @PathVariable Long feedId,
            @AuthenticationPrincipal Member member,
            @RequestBody CommentCreateRequest request
    );

    @Operation(
            summary = "댓글 목록 조회",
            description = "특정 피드의 댓글 목록을 조회합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "댓글 목록 조회 성공"
    )
    ResponseEntity<List<CommentResponse>> getComments(
            @Parameter(description = "피드 ID") @PathVariable Long feedId
    );

    @Operation(
            summary = "댓글 삭제",
            description = "댓글 ID를 기준으로 댓글을 삭제합니다."
    )
    @ApiResponse(
            responseCode = "204",
            description = "댓글 삭제 성공"
    )
    ResponseEntity<Void> deleteComment(
            @Parameter(description = "댓글 ID") @PathVariable Long commentId,
            @AuthenticationPrincipal Member member
    );
}
