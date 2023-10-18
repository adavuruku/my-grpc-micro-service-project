package com.example.serviceclient.exceptions;

import com.example.service_error_details.ErrorDetail;
import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import io.grpc.StatusRuntimeException;

import java.util.Map;

/**
 * The gRPC backend service wraps all the server side exceptions into StatusRuntimeException class
 * and sends it to the client. This mapper is required to unwrap the StatusRuntimeException and
 * extract the required exception details into ServiceException class
 * */
public class ServiceExceptionMapper {

    public static ServiceException map(StatusRuntimeException error) {

        var status = io.grpc.protobuf.StatusProto.fromThrowable(error);

        ErrorDetail errorDetail = ErrorDetail.newBuilder()
                .setErrorCode(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus().toString())
                .setMessage(ErrorCode.INTERNAL_SERVER_ERROR.getMessage())
                .putAllMetadata(Map.of())
                .build();

        for (Any any : status.getDetailsList()) {
            if (!any.is(ErrorDetail.class)) {
                continue;
            }
            try {
                errorDetail = any.unpack(ErrorDetail.class);
            } catch (InvalidProtocolBufferException cause) {
                errorDetail =
                        ErrorDetail.newBuilder()
                                .setErrorCode(ErrorCode.INVALID_OPERATION.getMessage())
                                .setMessage(cause.getMessage())
                                .putAllMetadata(Map.of())
                                .build();
            }
        }

        return new ServiceException(
                ErrorCode.errorCode(errorDetail.getErrorCode()),
                errorDetail.getMessage(),
                errorDetail.getMetadataMap());
    }
}