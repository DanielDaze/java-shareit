package ru.practicum.shareit;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.*;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleMethodArgumentNotValidException(final ConstraintViolationException e) {
        log.error("Пользователь передал неверные данные для создания объекта");
        return e.getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleDuplicatedDataException(final DuplicatedDataException e) {
        log.error("Во время регистрации пользователь передал данные, которые были использованы до него");
        return e.getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNoSuchDataException(final NoSuchDataException e) {
        log.error("Пользователь попытался найти несуществующий объект");
        return e.getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleWrongUserException(final WrongUserException e) {
        log.error("Пользователь не имеет доступ к изменяемому объекту");
        return e.getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleItemUnavailableException(final ItemUnavailableException e) {
        log.error("Пользователь попытался забронировать предмет, который недоступен!");
        return e.getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleDateConflictException(final DateConflictException e) {
        log.error("Пользователь неверно ввел даты брони!");
        return e.getMessage();
    }
}
