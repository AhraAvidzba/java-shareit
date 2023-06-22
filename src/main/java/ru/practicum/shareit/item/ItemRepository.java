package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByOwnerId(Long ownerId);

    @Query("select it " +
            "from Item as it " +
            "where (lower(it.name) like lower(CONCAT('%', ?1, '%')) " +
            "or lower(it.description) like lower(CONCAT('%', ?1, '%'))) " +
            "and it.available = true ")
    List<Item> findAllByNameOrDescription(String text);
}
