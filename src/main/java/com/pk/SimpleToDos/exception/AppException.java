package com.pk.SimpleToDos.exception;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
public class AppException extends Exception {
    private AppErrors errorCode;
    private String message;

    public AppException(AppErrors errorCode) {
        this.errorCode = errorCode;
    }

    public AppException(String message, AppErrors errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }
}
