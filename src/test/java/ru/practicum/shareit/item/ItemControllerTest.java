package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingIdOutDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.User;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {
    @Autowired
    ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    ItemService itemService;

    private ItemDto itemDto;

    @BeforeEach
    void setUp() {
        itemDto = new ItemDto();
        itemDto.setOwner(1L);
        itemDto.setName("Отвертка");
        itemDto.setDescription("Крестовая");
        itemDto.setAvailable(true);
        itemDto.setId(1L);
        itemDto.setRequestId(null);
    }

    @SneakyThrows
    @Test
    void saveItem() {
        when(itemService.saveItem(any(), anyLong()))
                .thenReturn(itemDto);

        String savedItem = mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertThat(mapper.writeValueAsString(itemDto), equalTo(savedItem));
    }

    @SneakyThrows
    @Test
    void patchItem() {
        when(itemService.patchItem(any(), anyLong()))
                .thenReturn(itemDto);

        String savedItem = mvc.perform(patch("/items/{id}", itemDto.getId())
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertThat(mapper.writeValueAsString(itemDto), equalTo(savedItem));
    }

    @SneakyThrows
    @Test
    void getItemById() {
        when(itemService.getItemById(anyLong(), anyLong()))
                .thenReturn(ItemMapper.toItemWithBookAndCommentsDto(
                        ItemMapper.toItem(itemDto, new User()),
                        new BookingIdOutDto(),
                        new BookingIdOutDto(),
                        List.of(new CommentDto())));

        mvc.perform(get("/items/{id}", itemDto.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())));
    }

    @SneakyThrows
    @Test
    void getItemsOfUser() {
    }

    @SneakyThrows
    @Test
    void searchItems() {
    }

    @SneakyThrows
    @Test
    void saveComment() {
    }
}