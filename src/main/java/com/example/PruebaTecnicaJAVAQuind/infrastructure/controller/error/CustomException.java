package com.example.PruebaTecnicaJAVAQuind.infrastructure.controller.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {
    @Getter
    private HttpStatus status;
    private String message;

    public CustomException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
