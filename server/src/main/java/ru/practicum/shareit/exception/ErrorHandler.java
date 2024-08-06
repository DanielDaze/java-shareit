package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public Error handleDuplicatedDataException(final DuplicatedDataException e) {
        log.error("Во время регистрации пользователь передал данные, которые были использованы до него");
        return new Error(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error handleNoSuchDataException(final NoSuchDataException e) {
        log.error("Пользователь попытался найти несуществующий объект");
        return new Error(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Error handleWrongUserException(final WrongUserException e) {
        log.error("Пользователь не имеет доступ к изменяемому объекту");
        return new Error(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error handleItemUnavailableException(final ItemUnavailableException e) {
        log.error("Пользователь попытался забронировать предмет, который недоступен!");
        return new Error(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error handleDateConflictException(final DateConflictException e) {
        log.error("Пользователь неверно ввел даты брони!");
        return new Error(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error handleIllegalArgumentException(final IllegalArgumentException e) {
        return new Error("Unknown state: UNSUPPORTED_STATUS");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public Error handleJdbcSQLIntegrityConstraintViolationException(final SQLIntegrityConstraintViolationException e) {
        log.error("Пользователь пытался создать конфликтующий объект");
        return new Error(e.getMessage());
    }
}
