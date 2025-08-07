package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.repository.InMemoryStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
public class UserService {
    private InMemoryStorage inMemoryStorage;
    final UserMapper userMapper;

    @Autowired
    public UserService(InMemoryStorage inMemoryStorage, UserMapper userMapper) {
        this.inMemoryStorage = inMemoryStorage;
        this.userMapper = userMapper;
    }

    public User findById(long userId) {
        return inMemoryStorage.findUserById(userId);
    }

    public List<User> getAll() {
        return null;
    }

    public UserDto create(UserDto userDto) {
        User user = userMapper.toModel(userDto);
        User savedUser = inMemoryStorage.createUser(user);
        return userMapper.toDto(savedUser);
    }

    public UserDto update(Long usereId, UserDto userDto) {
        User user = userMapper.toModel(userDto);
        User savedUser = inMemoryStorage.updateUser(usereId, user);
        return userMapper.toDto(savedUser);
    }

    public void deleteUser(Long userId) {
        inMemoryStorage.deleteUser(userId);
    }
}
