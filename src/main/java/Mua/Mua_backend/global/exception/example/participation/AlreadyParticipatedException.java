package Mua.Mua_backend.global.exception.example.participation;

import Mua.Mua_backend.global.exception.CustomException;
import Mua.Mua_backend.global.exception.ErrorCode;

public class AlreadyParticipatedException extends CustomException {

    public AlreadyParticipatedException() { super(ErrorCode.ALREADY_PARTICIPATED_EXCEPTION); }
}