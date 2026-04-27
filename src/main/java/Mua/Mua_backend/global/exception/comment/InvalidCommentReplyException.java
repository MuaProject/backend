package Mua.Mua_backend.global.exception.comment;

import Mua.Mua_backend.global.exception.CustomException;
import Mua.Mua_backend.global.exception.ErrorCode;

public class InvalidCommentReplyException extends CustomException {

    public InvalidCommentReplyException() {
        super(ErrorCode.INVALID_COMMENT_REPLY);
    }
}
