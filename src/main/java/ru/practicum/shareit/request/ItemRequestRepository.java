package ru.practicum.shareit.request;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    public List<ItemRequest> findByRequesterId(Long userId, Sort sort);

    public List<ItemRequest> findByRequesterIdNot(Long userId, Pageable page);

}
