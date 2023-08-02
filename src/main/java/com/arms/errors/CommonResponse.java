package com.arms.errors;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;

// import com.arms.errors.response.ErrorCode;

@Getter
@NoArgsConstructor
public class CommonResponse {

    public static <T> ApiResult<T> success(T response) {
        return new ApiResult<>(true, response, null);
    }

    // public static ApiResult<?> error(Throwable throwable, HttpStatus status) {
    //     return new ApiResult<>(false, null, new ApiError(throwable, status));
    // }

    public static ApiResult<?> error(String message, HttpStatus status) {
        return new ApiResult<>(false, null, new ApiError(message, status));
    }

    // public static ApiResult<?> error(ErrorCode errorCode, HttpStatus status) {
    //     return new ApiResult<>(false, null, new ApiError(errorCode, status));
    // }

    // public static ApiResult<?> error(String message, ErrorCode errorCode, HttpStatus status) {
    //     return new ApiResult<>(false, null, new ApiError(message, errorCode, status));
    // }
    
    @ToString
    public static class ApiError {
        private final String message;
        private String errorCode;
        private final int status;

        // ApiError(Throwable throwable, HttpStatus status) {
        //     this(throwable.getMessage(), status);
        // }

        ApiError(String message, HttpStatus status) {
            this.message = message;
            this.status = status.value();
        }

        // ApiError(ErrorCode errorCode, HttpStatus status) {
        //     this.message = errorCode.getErrorMsg();
        //     this.errorCode = errorCode.name();
        //     this.status = status.value();
        // }

        // ApiError(String message, ErrorCode errorCode, HttpStatus status) {
        //     this.message = message;
        //     this.errorCode = errorCode.name();
        //     this.status = status.value();
        // }

        public String getMessage() {
            return message;
        }

        public int getStatus() {
            return status;
        }

        public String getErrorCode() {
            return errorCode;
        }
    }

    @ToString
    public static class ApiResult<T> {
        private final boolean success;
        private final T response;
        private final ApiError error;

        private ApiResult(boolean success, T response, ApiError error) {
            this.success = success;
            this.response = response;
            this.error = error;
        }

        public boolean isSuccess() {
            return success;
        }

        public ApiError getError() {
            return error;
        }

        public T getResponse() {
            return response;
        }

    }



}
