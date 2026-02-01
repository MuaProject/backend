package Mua.Mua_backend.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INVALID_INPUT(400, "잘못된 입력 값입니다."),
    LOCATION_REQUIRED(400, "거리순 정렬을 위해 위치 정보가 필요합니다."),
    NO_FEED_UPDATE_CONTENT_EXCEPTION(400, "수정할 내용이 없습니다."),
    MEMBER_NOT_FOUND(404, "회원 정보를 찾을 수 없습니다."),

    UNAUTHORIZED(401, "인증이 필요합니다."),
    FORBIDDEN(403, "접근 권한이 없습니다."),
    FEED_UPDATE_FORBIDDEN(403, "게시물을 수정할 권한이 없습니다."),
    SELF_PARTICIPATION_NOT_ALLOWED_EXCEPTION(403, "본인 게시물에는 참가 신청할 수 없습니다."),
    INVALID_TOKEN(401, "유효하지 않거나 만료된 토큰입니다."),

    NOT_FOUND(404, "존재하지 않는 리소스입니다."),
    FEED_NOT_FOUND(404, "게시물을 찾을 수 없습니다."),
    PARTICIPATION_NOT_FOUND_EXCEPTION(404, "참가 신청이 존재하지 않습니다."),

    ALREADY_PARTICIPATED_EXCEPTION(409, "이미 참가 신청한 게시물입니다."),

    INTERNAL_SERVER_ERROR(500, "서버 오류가 발생했습니다."),

    // example
    EXAMPLE_NOT_FOUND(404, "예시를 찾을 수 없습니다.");

    private final int status;
    private final String message;

}
