package Mua.Mua_backend.global.exception.participation;

import Mua.Mua_backend.global.exception.CustomException;
import Mua.Mua_backend.global.exception.ErrorCode;

public class SelfParticipationNotAllowedException extends CustomException {

    public SelfParticipationNotAllowedException() { super(ErrorCode.SELF_PARTICIPATION_NOT_ALLOWED_EXCEPTION); }
}