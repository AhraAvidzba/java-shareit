package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {"db.name=shareitt",})
class ShareItTests2 {

    @Test
    void contextLoads() {
    }

}
