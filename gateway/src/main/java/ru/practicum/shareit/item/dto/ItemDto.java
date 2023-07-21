package ru.practicum.shareit.item.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.validations.Create;
import ru.practicum.shareit.validations.NotBlankOrNull;
import ru.practicum.shareit.validations.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@EqualsAndHashCode
public class ItemDto {
    private Long id;
    @NotBlank(groups = Create.class)
    @NotBlankOrNull(groups = Update.class)
    private String name;
    @NotBlank(groups = Create.class)
    private String description;
    @NotNull(groups = Create.class)
    private Boolean available;
    private Long owner;
    private Long requestId;
}
