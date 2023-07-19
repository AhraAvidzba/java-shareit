package ru.practicum.shareit.item;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.user.User;

import javax.persistence.*;

@Entity
@Table(name = "items")
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @EqualsAndHashCode.Exclude
    private String name;

    @Column(name = "description")
    @EqualsAndHashCode.Exclude
    private String description;

    @Column(name = "available")
    @EqualsAndHashCode.Exclude
    private Boolean available;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    private User owner;

    @Column(name = "request_id")
    @EqualsAndHashCode.Exclude
    private Long requestId;
}
