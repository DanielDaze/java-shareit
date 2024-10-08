package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import ru.practicum.shareit.user.dto.User;

import java.time.LocalDateTime;

@Data
public class ItemRequest {
    private long id;

    @NotBlank
    private String description;

    private User requestor;

    private LocalDateTime created;
}