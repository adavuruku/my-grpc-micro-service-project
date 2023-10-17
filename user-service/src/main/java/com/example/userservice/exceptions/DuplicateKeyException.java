package com.example.userservice.exceptions;

import java.util.Map;

public class DuplicateKeyException extends BaseException {
    public DuplicateKeyException(String message, Map<String, String> errorMetaData) {
        this(ErrorCode.USER_ALREADY_EXISTS,message, errorMetaData);
    }

    public DuplicateKeyException(ErrorCode errorCode, String message, Map<String, String> errorMetaData) {
        super(errorCode,message, errorMetaData);
    }

    public DuplicateKeyException(String message, Map<String, String> errorMetaData, Throwable cause) {
        this(ErrorCode.USER_ALREADY_EXISTS, message, errorMetaData, cause);
    }

    public DuplicateKeyException(ErrorCode errorCode,
                                 String message, Map<String, String> errorMetaData, Throwable cause) {
        super(errorCode,message, errorMetaData, cause);
    }
}
