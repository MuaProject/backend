package Mua.Mua_backend.global.exception.comment;

import Mua.Mua_backend.global.exception.CustomException;
import Mua.Mua_backend.global.exception.ErrorCode;

public class EventCommentDeleteNotAllowedException extends CustomException {

    public EventCommentDeleteNotAllowedException() {
        super(ErrorCode.EVENT_COMMENT_DELETE_NOT_ALLOWED);
    }
}
