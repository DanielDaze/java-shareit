package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    @NotEmpty
    private String text;

    private long id;

    private long itemId;

    private String authorName;

    private LocalDateTime created;
}
