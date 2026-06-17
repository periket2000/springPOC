package es.mybi.demo;

import es.mybi.demo.core.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = DemoApplication.class)
@TestPropertySource(locations="classpath:test.properties")
@MemoryDatabaseConfig
class DemoApplicationTests {

    @Autowired
    UserService userService;

    @Test
    void findUserByIdNull() {
        assertNull(userService.getUser(1L));
    }

    @Test
    void findUserByIdNotNull() {
        assertNotNull(userService.getUser(41L));
        assertEquals(userService.getUser(41L).getFirst_name().toLowerCase(), "lydia");
    }
}

