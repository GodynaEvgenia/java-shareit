package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public Optional<UserDto> findById(@PathVariable long userId) {
        return Optional.ofNullable(userService.findById(userId));
    }

    @GetMapping()
    public List<User> getAll() {
        return userService.getAll();
    }

    @PostMapping
    public UserDto create(@RequestBody @Valid UserDto user) {
        log.info("создание пользователя, user = {}", user.toString());
        UserDto createdUser = userService.create(user);
        log.info("пользователь создан, user={}", createdUser.toString());
        return createdUser;
    }

    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable Long userId,
                          @RequestBody UserDto userDto) {
        log.info("update user, userDto={}=", userDto);
        return userService.update(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }
}
