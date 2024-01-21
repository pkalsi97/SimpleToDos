package com.pk.SimpleToDos.exception;
import com.pk.SimpleToDos.model.responses.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.pk.SimpleToDos.exception.AppErrors.*;

@RestControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler({AppException.class})
    public ResponseEntity<?> handleAppException(AppException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ex.getErrorCode().name())
                .message(ex.getMessage())
                .build();

        switch (ex.getErrorCode()) {
            case INVALID_VALUE:
                return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
            case RESOURCE_NOT_FOUND:
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            case INTERNAL_SERVER_ERROR:
                return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
            case DUPLICATE_FOUND:
                return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
            default:
                return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
