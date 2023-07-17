package ru.practicum.shareit.item;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ToString
@Getter
@Setter
@EqualsAndHashCode
public class Item {
    private Long id;

    @NotBlank
    @EqualsAndHashCode.Exclude
    private String name;

    @NotBlank
    @EqualsAndHashCode.Exclude
    private String description;

    @NotNull
    @EqualsAndHashCode.Exclude
    private Boolean available;

    @NotNull
    @EqualsAndHashCode.Exclude
    private User owner;

    @EqualsAndHashCode.Exclude
    private Long requestId;
}
