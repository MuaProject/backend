package Mua.Mua_backend.global.exception.member;

import Mua.Mua_backend.global.exception.CustomException;
import Mua.Mua_backend.global.exception.ErrorCode;

public class AlreadyNicknameException extends CustomException {

    public AlreadyNicknameException() { super(ErrorCode.ALREADY_NICKNAME_EXCEPTION); }
}