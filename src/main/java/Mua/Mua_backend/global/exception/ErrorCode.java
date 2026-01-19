package Mua.Mua_backend.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INVALID_INPUT(400, "잘못된 입력 값입니다."),
    UNAUTHORIZED(401, "인증이 필요합니다."),
    FORBIDDEN(403, "접근 권한이 없습니다."),
    NOT_FOUND(404, "존재하지 않는 리소스입니다."),
    INTERNAL_SERVER_ERROR(500, "서버 오류가 발생했습니다."),

    // example
    EXAMPLE_NOT_FOUND(404, "예시를 찾을 수 없습니다.");

    private final int status;
    private final String message;

}
