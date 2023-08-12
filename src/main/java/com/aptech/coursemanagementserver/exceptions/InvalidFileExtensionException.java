package com.aptech.coursemanagementserver.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
public class InvalidFileExtensionException extends RuntimeException {

    public InvalidFileExtensionException(String extension) {
        super(String.format("The File with extension: [%s] is not supported.", extension));
    }

    public InvalidFileExtensionException(String message, Throwable cause) {
        super(message, cause);
    }
}
