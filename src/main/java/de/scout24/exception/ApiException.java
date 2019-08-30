package de.scout24.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {

    private final HttpStatus code;
    private final ErrorCodes errorCodes;

    public ApiException(HttpStatus code, String msg) {
        super(msg);
        this.code = code;
        this.errorCodes = null;
    }

    public ApiException(HttpStatus code, Exception e) {
        super(e);
        this.code = code;
        this.errorCodes = null;
    }

    public ApiException(HttpStatus code, ErrorCodes errorCodes) {
        super(errorCodes.name());
        this.code = code;
        this.errorCodes = errorCodes;
    }

    public HttpStatus getHttpStatus() {
        return code;
    }

    public ErrorCodes getErrorCodes() {
        return errorCodes;
    }
}
