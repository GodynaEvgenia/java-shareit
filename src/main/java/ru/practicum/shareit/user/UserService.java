package ru.practicum.shareit.user;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
public class UserService {
    private UserRepository repository;
    final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository repository, UserMapper userMapper) {
        this.repository = repository;
        this.userMapper = userMapper;
    }

    public User findById(long userId) {
        return repository.findById(userId).get();
    }

    public List<User> getAll() {
        return repository.findAll();
    }

    @Transactional
    public UserDto create(UserDto userDto) {
        User user = userMapper.toModel(userDto);
        User savedUser = repository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Transactional
    public UserDto update(Long userId, UserDto userDto) {
        User exUser = repository.findById(userId).get();
        User user = userMapper.toModelForUpdate(userDto, exUser);
        user.setId(userId);
        User savedUser = repository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = repository.findById(userId).get();
        repository.delete(user);
    }
}
