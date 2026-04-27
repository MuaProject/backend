package Mua.Mua_backend.global.exception.comment;

import Mua.Mua_backend.global.exception.CustomException;
import Mua.Mua_backend.global.exception.ErrorCode;

public class CommentNotFoundException extends CustomException {

    public CommentNotFoundException() {
        super(ErrorCode.COMMENT_NOT_FOUND);
    }
}
