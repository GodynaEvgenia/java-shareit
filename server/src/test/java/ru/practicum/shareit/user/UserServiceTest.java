package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserMapper userMapper = new UserMapper();
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, userMapper);
    }

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


    @Test
    void create() {
        UserDto userToSave = new UserDto();
        User user = new User();
        when(userRepository.save(user)).thenReturn(user);
        UserDto actualUserDto = userService.create(userToSave);

        verify(userRepository).save(user);
    }

    @Test
    void getAll_whenFound() {
        List<User> userList = new ArrayList<>();
        when(userRepository.findAll()).thenReturn(userList);
        List<User> expectedUserList = userService.getAll();

        assertEquals(userList, expectedUserList);
    }

    @Test
    void update() {
        UserDto userToSave = new UserDto();
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        UserDto actualUserDto = userService.update(user.getId(), userToSave);

        verify(userRepository).save(user);
    }

    @Test
    void deleteUser() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteUser(1L);
        verify(userRepository).delete(user);
    }

    @Test
    public void testDeleteNotExistingUser() {
        when(userRepository.findById(1L)).thenThrow(NoSuchElementException.class);
        assertThrows(NoSuchElementException.class, () -> userService.deleteUser(1L));
    }

    @Test
    public void testUserMapper_toModelForUpdate() {
        User user = new User();
        user.setId(1L);
        user.setName("name");
        user.setEmail("email@email");
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("name2");
        userDto.setEmail("email@email2");

        User savedUser = userMapper.toModelForUpdate(userDto, user);
        assertEquals(savedUser, user);
    }
}