package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void findById_whenUserFound() {
        long userId = 0L;
        User expectedUser = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));
        UserDto expectedUserDto = userMapper.toDto(expectedUser);

        UserDto actualUserDto = userService.findById(userId);

        assertEquals(actualUserDto, expectedUserDto);
    }

    @Test
    void findById_whenUserNotFound() {
        long userId = 0L;
        User expectedUser = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userService.findById(userId));
    }

/*
    @Test
    void create() {
        UserDto userToSave = new UserDto();
        userToSave.setId(1L);
        userToSave.setName("Test Name");
        userToSave.setEmail("test@example.com");
        // Заполнение других полей, если необходимо
        User user = new User();
        when(userRepository.save(user)).thenReturn(user);

        UserDto actualUserDto = userService.create(userToSave);

        verify(userRepository).save(user);
    }*/

    @Test
    void update() {
    }

    @Test
    void deleteUser() {
    }
}