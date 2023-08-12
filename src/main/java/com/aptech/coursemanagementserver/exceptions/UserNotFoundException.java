package com.aptech.coursemanagementserver.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    private String username;
    private String fieldName;
    private Object fieldValue;

    public UserNotFoundException(String username, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s : '%s'", fieldName, fieldValue));
        this.username = username;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public UserNotFoundException(String message) {
        super(message);
    }

    public String getUsername() {
        return username;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }
}
