package Mua.Mua_backend.global.exception.feed;

import Mua.Mua_backend.global.exception.CustomException;
import Mua.Mua_backend.global.exception.ErrorCode;

public class InvalidFeedLocationException extends CustomException {

    public InvalidFeedLocationException() {
        super(ErrorCode.INVALID_FEED_LOCATION);
    }
}
