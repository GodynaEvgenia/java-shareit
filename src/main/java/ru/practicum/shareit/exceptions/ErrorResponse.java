package ru.practicum.shareit.exceptions;

public class ErrorResponse {
    private final String message;

    public ErrorResponse(String message) {
        this.message = message;
    }

    public String addError(String ms) {
        return this.message + ms;
    }
}
