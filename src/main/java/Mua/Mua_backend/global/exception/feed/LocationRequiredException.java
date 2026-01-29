package Mua.Mua_backend.global.exception.feed;

import Mua.Mua_backend.global.exception.CustomException;
import Mua.Mua_backend.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class LocationRequiredException extends CustomException {

    public LocationRequiredException() { super(ErrorCode.LOCATION_REQUIRED); }
}
