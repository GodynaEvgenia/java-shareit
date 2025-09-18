package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
class UserControllerTest {

    @BeforeEach
    public void setUp() {
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testFindById_UserExists() throws Exception {
        long userId = 1L;
        UserDto userDto = new UserDto();
        userDto.setId(userId);
        userDto.setName("John Doe");

        when(userService.findById(userId)).thenReturn(userDto);

        mockMvc.perform(get("/users/{userId}" + userId, userId))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateUser() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setName("John");
        userDto.setEmail("john@example.com");

        UserDto createdUser = new UserDto();
        createdUser.setId(1L);
        createdUser.setName("John");
        createdUser.setEmail("john@example.com");

        when(userService.create(any(UserDto.class))).thenReturn(createdUser);

        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());

        verify(userService, times(1)).create(userDto);
    }

    @Test
    void getAll() {
        List<User> expectedUser = List.of(new User());
        when(userService.getAll()).thenReturn(expectedUser);
        List<User> response = userService.getAll();

        assertEquals(expectedUser, response);
    }

    @Test
    void create() {
    }

    @Test
    void update() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setName("John");
        userDto.setEmail("john@example.com");

        UserDto createdUser = new UserDto();
        createdUser.setId(1L);
        createdUser.setName("John");
        createdUser.setEmail("john@example.com");

        when(userService.update(any(Long.class), any(UserDto.class))).thenReturn(createdUser);

        mockMvc.perform(patch("/users/1", 1L)
                        .content(objectMapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());

        verify(userService, times(1)).update(1L, userDto);
    }

    @Test
    void deleteUser() throws Exception {
        Long userId = 1L;

        mockMvc.perform(delete("/users/{userId}", userId))
                .andExpect(status().isOk());
    }
}