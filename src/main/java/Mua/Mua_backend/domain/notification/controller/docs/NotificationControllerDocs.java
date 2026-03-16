package Mua.Mua_backend.domain.notification.controller.docs;

import Mua.Mua_backend.domain.member.entity.Member;
import Mua.Mua_backend.domain.notification.dto.response.NotificationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(name = "Notification", description = "알림 관련 API")
@SecurityRequirement(name = "JWT")
public interface NotificationControllerDocs {

    @Operation(
            summary = "내 알림 목록 조회",
            description = """
                로그인한 사용자의 알림 목록을 최신순으로 조회합니다.
                
                - 가장 최근에 생성된 알림이 먼저 조회됩니다.
                - 읽음 여부, 알림 내용, 생성 시간 등의 정보가 포함됩니다.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "알림 목록 조회 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(
                                    schema = @Schema(implementation = NotificationResponse.class)
                            ),
                            examples = @ExampleObject(
                                    name = "알림 목록 응답 예시",
                                    value = """
                                            [
                                              {
                                                "notificationId": 1,
                                                "content": "회원님의 게시글에 댓글이 달렸습니다.",
                                                "isRead": false,
                                                "createdAt": "2026-02-25T10:15:00"
                                              },
                                              {
                                                "notificationId": 2,
                                                "content": "회원님의 게시글이 좋아요를 받았습니다.",
                                                "isRead": true,
                                                "createdAt": "2026-02-24T18:20:00"
                                              }
                                            ]
                                            """
                            )
                    )
            )
    })
    @GetMapping
    List<NotificationResponse> getMyNotifications(
            @Parameter(hidden = true)
            @AuthenticationPrincipal Member member
    );


    @Operation(
            summary = "알림 읽음 처리",
            description = """
                특정 알림을 읽음 상태로 변경합니다.
                
                - 이미 읽은 알림이어도 요청은 정상 처리됩니다.
                - 성공 시 200 OK를 반환합니다.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "알림 읽음 처리 성공"
            )
    })
    @PatchMapping("/{notificationId}/read")
    ResponseEntity<Void> readNotification(
            @Parameter(description = "읽음 처리할 알림 ID", example = "1")
            @PathVariable Long notificationId,

            @Parameter(hidden = true)
            @AuthenticationPrincipal Member member
    );


    @Operation(
            summary = "내 알림 전체 삭제",
            description = """
                로그인한 사용자의 모든 알림을 삭제합니다.
                
                - 삭제된 알림은 복구할 수 없습니다.
                - 성공 시 204 No Content를 반환합니다.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "알림 전체 삭제 성공"
            )
    })
    @DeleteMapping
    ResponseEntity<Void> deleteMyNotifications(
            @Parameter(hidden = true)
            @AuthenticationPrincipal Member member
    );
}