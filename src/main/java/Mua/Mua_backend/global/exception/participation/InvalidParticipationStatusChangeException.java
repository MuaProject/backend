package Mua.Mua_backend.global.exception.participation;

import Mua.Mua_backend.global.exception.CustomException;
import Mua.Mua_backend.global.exception.ErrorCode;

public class InvalidParticipationStatusChangeException extends CustomException {

    public InvalidParticipationStatusChangeException() {
        super(ErrorCode.INVALID_PARTICIPATION_STATUS_CHANGE);
    }
}
