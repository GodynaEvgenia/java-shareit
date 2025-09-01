package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private  UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void findById() {
    }

    @Test
    void getAll() {
        List<User> expectedUser = List.of(new User());
        Mockito.when(userService.getAll()).thenReturn(expectedUser);
        List<User> response = userService.getAll();

        assertEquals(expectedUser, response);
    }

    @Test
    void create() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}