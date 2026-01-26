package Mua.Mua_backend.global.exception.example;

import Mua.Mua_backend.global.exception.CustomException;
import Mua.Mua_backend.global.exception.ErrorCode;

public class NoFeedUpdateContentException extends CustomException {

    public NoFeedUpdateContentException() { super(ErrorCode.NO_FEED_UPDATE_CONTENT_EXCEPTION); }
}
