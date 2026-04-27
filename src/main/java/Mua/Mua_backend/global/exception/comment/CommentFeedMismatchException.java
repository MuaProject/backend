package Mua.Mua_backend.global.exception.comment;

import Mua.Mua_backend.global.exception.CustomException;
import Mua.Mua_backend.global.exception.ErrorCode;

public class CommentFeedMismatchException extends CustomException {

    public CommentFeedMismatchException() {
        super(ErrorCode.COMMENT_FEED_MISMATCH);
    }
}
