package Mua.Mua_backend.global.exception.feed;

import Mua.Mua_backend.global.exception.CustomException;
import Mua.Mua_backend.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class FeedNotFoundException extends CustomException {

    public FeedNotFoundException() { super(ErrorCode.FEED_NOT_FOUND); }
}
