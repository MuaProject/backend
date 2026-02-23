package Mua.Mua_backend.domain.feed.controller.docs;

import Mua.Mua_backend.domain.feed.dto.request.FeedCreateRequest;
import Mua.Mua_backend.domain.feed.dto.request.FeedUpdateRequest;
import Mua.Mua_backend.domain.feed.dto.response.FeedCursorResponse;
import Mua.Mua_backend.domain.feed.dto.response.FeedDetailResponse;
import Mua.Mua_backend.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Feed", description = "게시물 관련 API")
@SecurityRequirement(name = "JWT")
public interface FeedControllerDocs {

    @Operation(
            summary = "피드 목록 조회",
            description = "조건에 따라 피드 목록을 조회합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "피드 목록 조회 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = FeedCursorResponse.class)
            )
    )
    ResponseEntity<FeedCursorResponse> getFeeds(
            @Parameter(description = "커서 ID") @RequestParam(required = false) Long cursorId,
            @Parameter(description = "커서 생성 시간") @RequestParam(required = false) LocalDateTime cursorCreatedAt,
            @Parameter(description = "정렬 기준 (LATEST / DISTANCE)") @RequestParam(defaultValue = "LATEST") String sort,
            @Parameter(description = "위도") @RequestParam(required = false) Double latitude,
            @Parameter(description = "경도") @RequestParam(required = false) Double longitude,
            @Parameter(description = "조회 개수") @RequestParam(defaultValue = "10") int size
    );

    @Operation(
            summary = "피드 상세 조회",
            description = "피드 ID로 상세 정보를 조회합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "피드 상세 조회 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = FeedDetailResponse.class)
            )
    )
    ResponseEntity<FeedDetailResponse> getFeed(
            @Parameter(description = "피드 ID") @PathVariable Long feedId
    );

    @Operation(
            summary = "피드 생성",
            description = "새로운 피드를 생성합니다."
    )
    @ApiResponse(responseCode = "200", description = "피드 생성 성공")
    Long createFeed(
            @AuthenticationPrincipal Member member,
            @RequestBody FeedCreateRequest request
    );

    @Operation(
            summary = "피드 수정",
            description = "기존 피드를 수정합니다."
    )
    @ApiResponse(responseCode = "200", description = "피드 수정 성공")
    void updateFeed(
            @Parameter(description = "피드 ID") @PathVariable Long feedId,
            @AuthenticationPrincipal Member member,
            @RequestBody FeedUpdateRequest request
    );

    @Operation(
            summary = "피드 삭제",
            description = "피드를 삭제합니다."
    )
    @ApiResponse(responseCode = "200", description = "피드 삭제 성공")
    void deleteFeed(
            @Parameter(description = "피드 ID") @PathVariable Long feedId,
            @AuthenticationPrincipal Member member
    );
}
