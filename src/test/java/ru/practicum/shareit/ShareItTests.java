package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@TestPropertySource(properties = {"db.name=shareit",})
class ShareItTests {
    MockMvc mvc;

    @Test
    void contextLoads() {
    }

}
