package Mua.Mua_backend.global.exception.member;

import Mua.Mua_backend.global.exception.CustomException;
import Mua.Mua_backend.global.exception.ErrorCode;

public class MemberNotFoundException extends CustomException {

    public MemberNotFoundException() { super(ErrorCode.MEMBER_NOT_FOUND); }
}