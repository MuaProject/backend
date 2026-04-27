package Mua.Mua_backend.global.exception.comment;

import Mua.Mua_backend.global.exception.CustomException;
import Mua.Mua_backend.global.exception.ErrorCode;

public class CommentDeleteForbiddenException extends CustomException {

    public CommentDeleteForbiddenException() {
        super(ErrorCode.COMMENT_DELETE_FORBIDDEN);
    }
}
