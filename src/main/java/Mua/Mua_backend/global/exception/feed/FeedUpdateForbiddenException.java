package Mua.Mua_backend.global.exception.feed;

import Mua.Mua_backend.global.exception.CustomException;
import Mua.Mua_backend.global.exception.ErrorCode;

public class FeedUpdateForbiddenException extends CustomException {

    public FeedUpdateForbiddenException() { super(ErrorCode.FEED_UPDATE_FORBIDDEN); }
}
