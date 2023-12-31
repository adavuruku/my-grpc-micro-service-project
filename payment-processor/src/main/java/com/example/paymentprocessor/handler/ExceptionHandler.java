package com.example.paymentprocessor.handler;


import com.example.bookservice.exceptions.*;
import com.example.service_error_details.ErrorDetail;
import com.google.protobuf.Any;
import com.google.rpc.Code;
import io.grpc.StatusRuntimeException;
import io.grpc.protobuf.StatusProto;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

/**
 * This is Grpc exception handler which intercepts all the exceptions thrown by the rpc methods
 * and handles them at a common place. Basically, the intent here is to capture all the
 * exception details inside the ErrorDetail object and wrap it in
 * StatusRuntimeException. Client will unwrap the StatusRuntimeException object and interpret
 * the error messages accordingly.
 */
@GrpcAdvice
public class ExceptionHandler {

    @GrpcExceptionHandler(ResourceNotFoundException.class)
    public StatusRuntimeException handleResourceNotFoundException(ResourceNotFoundException error) {
        var errorMetaData = error.getErrorMetaData();
        var errorInfo =
                ErrorDetail.newBuilder()
                        .setErrorCode(ErrorCode.RESOURCE_NOT_FOUND.getShortCode())
                        .setMessage(error.getMessage())
                        .putAllMetadata(errorMetaData)
                        .build();
        var status =
                com.google.rpc.Status.newBuilder()
                        .setCode(Code.NOT_FOUND.getNumber())
                        .setMessage(ErrorCode.RESOURCE_NOT_FOUND.getMessage())
                        .addDetails(Any.pack(errorInfo))
                        .build();
        return StatusProto.toStatusRuntimeException(status);
    }

    @GrpcExceptionHandler(DuplicateKeyException.class)
    public StatusRuntimeException handleDuplicateKeyException(DuplicateKeyException error) {
        var errorMetaData = error.getErrorMetaData();
        var errorInfo =
                ErrorDetail.newBuilder()
                        .setErrorCode(ErrorCode.USER_ALREADY_EXISTS.getShortCode())
                        .setMessage(error.getMessage())
                        .putAllMetadata(errorMetaData)
                        .build();
        var status =
                com.google.rpc.Status.newBuilder()
                        .setCode(Code.ALREADY_EXISTS.getNumber())
                        .setMessage(ErrorCode.USER_ALREADY_EXISTS.getMessage())
                        .addDetails(Any.pack(errorInfo))
                        .build();
        return StatusProto.toStatusRuntimeException(status);
    }

    @GrpcExceptionHandler(Exception.class)
    public StatusRuntimeException handleAllUncaughtException(Exception error) {
        var errorInfo =
                ErrorDetail.newBuilder()
                        .setErrorCode(ErrorCode.INTERNAL_SERVER_ERROR.getShortCode())
                        .setMessage(error.getMessage())
                        .build();
        var status =
                com.google.rpc.Status.newBuilder()
                        .setCode(Code.INTERNAL.getNumber())
                        .setMessage(ErrorCode.INTERNAL_SERVER_ERROR.getMessage())
                        .addDetails(Any.pack(errorInfo))
                        .build();
        return StatusProto.toStatusRuntimeException(status);
    }
}