package com.naumoff.rnc.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ValidatorErrorResponse {

    private LocalDateTime timestamp = LocalDateTime.now();
    private int status = 400;
    private String error = "Bad Request";
    private List<ValidationError> errors = new ArrayList<>();

    public void addError(String field, String message) {
        errors.add(new ValidationError(field, message));
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public static class ValidationError {
        private String field;
        private String message;

        public ValidationError(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
