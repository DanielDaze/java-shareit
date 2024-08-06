package ru.practicum.shareit.item.comment;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    private String text;

    private long id;

    private long itemId;

    private String authorName;

    private LocalDateTime created;
}
