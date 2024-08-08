package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoSuchDataException extends RuntimeException {
    public NoSuchDataException() {
    }

    public NoSuchDataException(String message) {
        super(message);
    }
}
