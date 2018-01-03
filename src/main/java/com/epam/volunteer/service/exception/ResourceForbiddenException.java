package com.epam.volunteer.service.exception;

public class ResourceForbiddenException extends RuntimeException {

    public ResourceForbiddenException() {
    }

    public ResourceForbiddenException(String message) {
        super(message);
    }

    public ResourceForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceForbiddenException(Throwable cause) {
        super(cause);
    }
}
