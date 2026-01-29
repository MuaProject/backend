package Mua.Mua_backend.global.exception.participation;

import Mua.Mua_backend.global.exception.CustomException;
import Mua.Mua_backend.global.exception.ErrorCode;

public class ParticipationNotFoundException extends CustomException {

    public ParticipationNotFoundException() { super(ErrorCode.PARTICIPATION_NOT_FOUND_EXCEPTION); }
}