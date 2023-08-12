package com.aptech.coursemanagementserver.exceptions.exceptionsHandler;

import java.io.FileNotFoundException;
import java.nio.file.NoSuchFileException;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.aptech.coursemanagementserver.dtos.ErrorDetails;
import com.aptech.coursemanagementserver.enums.AntType;
import com.aptech.coursemanagementserver.exceptions.BadRequestException;
import com.aptech.coursemanagementserver.exceptions.InvalidFileExtensionException;
import com.aptech.coursemanagementserver.exceptions.InvalidTokenException;
import com.aptech.coursemanagementserver.exceptions.IsExistedException;
import com.aptech.coursemanagementserver.exceptions.ResourceNotFoundException;
import com.aptech.coursemanagementserver.exceptions.UserNotFoundException;

@ControllerAdvice // Allows handling exceptions across the whole application in one global
                  // handling component
public class GlobalExceptionHandler {
        // Handle global Exception
        // @ExceptionHandler(Exception.class)
        // public ResponseEntity<ErrorDetails> handleInternalException(Exception
        // resourceNotFoundException,
        // WebRequest webRequest) {
        // ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
        // resourceNotFoundException.getMessage(),
        // webRequest.getDescription(false),
        // HttpStatus.INTERNAL_SERVER_ERROR.toString());

        // return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
        // }

        // 400 BadRequest
        @ExceptionHandler(BadRequestException.class)
        public ResponseEntity<ErrorDetails> handleAllException(BadRequestException badRequestException,
                        WebRequest webRequest) {
                ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), AntType.error,
                                badRequestException.getMessage(),
                                webRequest.getDescription(false), HttpStatus.BAD_REQUEST.toString());
                return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
        }

        // 401 Unauthorized - Invalid Token
        @ExceptionHandler(InvalidTokenException.class)
        public ResponseEntity<ErrorDetails> handleInvalidTokenException(InvalidTokenException invalidTokenException,
                        WebRequest webRequest) {
                ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), AntType.error,
                                invalidTokenException.getMessage(),
                                webRequest.getDescription(false), invalidTokenException.getStatus().toString());
                return new ResponseEntity<>(errorDetails, invalidTokenException.getStatus());
        }

        // 403 Forbidden - Handle Security Authenticate
        @ExceptionHandler(AccessDeniedException.class)
        public ResponseEntity<ErrorDetails> handleAccessDeniedExceptions(AccessDeniedException t) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorDetails.builder()
                                .details(t.getMessage())
                                .type(AntType.error)
                                .statusCode("403")
                                .timestamp(LocalDateTime.now())
                                .message(t.getMessage()).build());
        }

        // 404 Not Found
        @ExceptionHandler(UserNotFoundException.class)
        public final ResponseEntity<ErrorDetails> handleUserNotFoundException(Exception ex, WebRequest request)
                        throws Exception {
                ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), AntType.error,
                                ex.getMessage(), request.getDescription(false), HttpStatus.NOT_FOUND.toString());

                return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ErrorDetails> handleResourceNotFoundException(
                        ResourceNotFoundException resourceNotFoundException, WebRequest webRequest) {
                ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), AntType.error,
                                resourceNotFoundException.getMessage(),
                                webRequest.getDescription(false), HttpStatus.NOT_FOUND.toString());

                return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
        }

        // 409 Conflict - Is existed
        @ExceptionHandler(IsExistedException.class)
        public ResponseEntity<ErrorDetails> handleExistedException(IsExistedException isExistedException,
                        WebRequest webRequest) {
                ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), AntType.warning,
                                isExistedException.getMessage(),
                                webRequest.getDescription(false), HttpStatus.CONFLICT.toString());
                return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
        }

        // 415 Unsupported MediaType - Is existed
        @ExceptionHandler(InvalidFileExtensionException.class)
        public ResponseEntity<ErrorDetails> handleInvalidFileExtensionException(
                        InvalidFileExtensionException invalidFileExtensionException,
                        WebRequest webRequest) {
                ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), AntType.warning,
                                invalidFileExtensionException.getMessage(),
                                webRequest.getDescription(false), HttpStatus.UNSUPPORTED_MEDIA_TYPE.toString());
                return new ResponseEntity<>(errorDetails, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }

        // IO Exception
        @ExceptionHandler(FileNotFoundException.class)
        public ResponseEntity<ErrorDetails> handleFileNotFoundException(
                        FileNotFoundException fileNotFoundException, WebRequest webRequest) {
                ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), AntType.error,
                                "URL cannot be resolved in the file system for checking its content length",
                                webRequest.getDescription(false), HttpStatus.NOT_FOUND.toString());

                return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(NoSuchFileException.class)
        public ResponseEntity<ErrorDetails> handleNoSuchFileException(
                        FileNotFoundException noSuchFileException, WebRequest webRequest) {
                ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), AntType.error,
                                noSuchFileException.getMessage(),
                                webRequest.getDescription(false), HttpStatus.NOT_FOUND.toString());

                return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
        }

}
