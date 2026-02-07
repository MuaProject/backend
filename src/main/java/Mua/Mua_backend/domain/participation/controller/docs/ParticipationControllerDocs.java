package Mua.Mua_backend.domain.participation.controller.docs;

import Mua.Mua_backend.domain.member.entity.Member;
import Mua.Mua_backend.domain.participation.dto.response.MyParticipationResponse;
import Mua.Mua_backend.domain.participation.dto.response.ParticipationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Participation", description = "경기 참가 관련 API")
@SecurityRequirement(name = "JWT")
public interface ParticipationControllerDocs {

    @Operation(
            summary = "참가 신청",
            description = "특정 피드에 참가 신청을 합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "참가 신청 성공"
    )
    ResponseEntity<Void> apply(
            @Parameter(description = "피드 ID")
            @PathVariable Long feedId,
            @AuthenticationPrincipal Member member
    );

    @Operation(
            summary = "참가자 목록 조회",
            description = "특정 피드의 전체 참가자 목록을 조회합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "참가자 목록 조회 성공"
    )
    ResponseEntity<List<ParticipationResponse>> getParticipations(
            @Parameter(description = "피드 ID")
            @PathVariable Long feedId
    );

    @Operation(
            summary = "참가 승인",
            description = "참가 신청을 승인합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "참가 승인 성공"
    )
    ResponseEntity<Void> approve(
            @Parameter(description = "참가 ID")
            @PathVariable Long id,
            @AuthenticationPrincipal Member member
    );

    @Operation(
            summary = "참가 거절",
            description = "참가 신청을 거절합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "참가 거절 성공"
    )
    ResponseEntity<Void> reject(
            @Parameter(description = "참가 ID")
            @PathVariable Long id,
            @AuthenticationPrincipal Member member
    );

    // 커서 기반 무한 스크롤 조회
    @Operation(
            summary = "내 경기 참가 기록 조회 (커서 기반 무한 스크롤)",
            description =
                    "로그인한 사용자의 경기 참가 기록을 조회합니다.\n\n" +
                            "- cursorTime, cursorId가 없으면 최초 조회\n" +
                            "- cursorTime, cursorId가 있으면 다음 페이지 조회\n" +
                            "- status 파라미터로 참가 상태별 필터링 가능\n" +
                            "- 정렬 기준: createdAt DESC, id DESC"
    )
    @ApiResponse(
            responseCode = "200",
            description = "내 경기 참가 기록 조회 성공"
    )
    ResponseEntity<List<MyParticipationResponse>> getMyParticipations(
            @AuthenticationPrincipal Member member,

            @Parameter(
                    description = "참가 상태 (PENDING, APPROVED, REJECTED)",
                    required = false
            )
            @RequestParam(required = false) String status,

            @Parameter(
                    description = "커서 기준 시간 (마지막 요소의 createdAt)",
                    required = false
            )
            @RequestParam(required = false) LocalDateTime cursorTime,

            @Parameter(
                    description = "커서 기준 ID (마지막 요소의 id)",
                    required = false
            )
            @RequestParam(required = false) Long cursorId,

            @Parameter(
                    description = "조회 개수 (기본값 10)",
                    required = false
            )
            @RequestParam(defaultValue = "10") int size
    );
}
