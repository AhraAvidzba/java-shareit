package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDto {
    private Long id;

    private String text;

    private Long itemId;

    private Long userId;

    private LocalDateTime created = LocalDateTime.now();

    private String authorName;
}
