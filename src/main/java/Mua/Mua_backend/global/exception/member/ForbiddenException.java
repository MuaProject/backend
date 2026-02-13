package Mua.Mua_backend.global.exception.member;

import Mua.Mua_backend.global.exception.CustomException;
import Mua.Mua_backend.global.exception.ErrorCode;

public class ForbiddenException extends CustomException {

    public ForbiddenException() { super(ErrorCode.FORBIDDEN); }
}