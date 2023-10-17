package com.example.serviceclient.exceptions;

import java.util.Map;

public class PasswordMissMatchException extends BaseException {
    public PasswordMissMatchException(String message, Map<String, String> errorMetaData) {
        this(ErrorCode.RESOURCE_NOT_FOUND,message, errorMetaData);
    }

    public PasswordMissMatchException(ErrorCode errorCode, String message, Map<String, String> errorMetaData) {
        super(errorCode,message, errorMetaData);
    }

    public PasswordMissMatchException(String message, Map<String, String> errorMetaData, Throwable cause) {
        this(ErrorCode.RESOURCE_NOT_FOUND,message, errorMetaData, cause);
    }

    public PasswordMissMatchException(ErrorCode errorCode,
                                      String message, Map<String, String> errorMetaData, Throwable cause) {
        super(errorCode,message, errorMetaData, cause);
    }
}
