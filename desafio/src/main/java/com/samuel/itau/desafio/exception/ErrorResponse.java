package com.samuel.itau.desafio.exception;

import java.time.OffsetDateTime;
import java.util.Map;

public class ErrorResponse {
    private final int status;
    private final String message;
    private final Map<String, String> errors;
    private final OffsetDateTime timestamp;

    public ErrorResponse(int status, String message, Map<String, String> errors, OffsetDateTime timestamp) {
        this.status = status;
        this.message = message;
        this.errors = errors;
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }
}