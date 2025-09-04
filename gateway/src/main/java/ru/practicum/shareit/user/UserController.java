package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

@Slf4j
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private UserClient userClient;

    @Autowired
    public UserController(UserClient userClient) {
        this.userClient = userClient;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> get(@PathVariable long userId) {
        return userClient.findById(userId);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid UserDto user) {
        log.info("создание пользователя, user = {}", user.toString());
        //UserDto createdUser = userClient.create(user);
        //log.info("пользователь создан, user={}", createdUser.toString());
        return userClient.create(user);
    }

    @PatchMapping("/{userId}")
    public /*UserDto*/ResponseEntity<Object> update(@PathVariable Long userId,
                                                    @RequestBody UserDto userDto) {
        log.info("update user, userDto={}=", userDto);
        return userClient.update(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Long userId) {
        userClient.delete(userId);
    }
}

