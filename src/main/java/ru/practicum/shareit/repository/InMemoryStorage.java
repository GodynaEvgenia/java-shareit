package ru.practicum.shareit.repository;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryStorage {
    private static Long id;
    private HashMap<Long, User> users;
    private HashMap<Long, Item> items;
    private Set<String> emails;
    private final Validator validator;

    public InMemoryStorage() {
        id = 1L;
        users = new HashMap<>();
        items = new HashMap<>();
        emails = new HashSet<>();
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public Long getId() {
        id += 1;
        return id;
    }

    public User findUserById(Long userId) {
        isUserExists(userId);
        return users.get(userId);
    }

    public User createUser(User user) {
        final Long id = getId();
        emailIsUnique(user.getEmail());
        log.info(emails.toString());
        user.setId(id);
        users.put(id, user);
        return user;
    }

    private boolean emailIsUnique(String email) {
        if (!emails.add(email)) {
            throw new RuntimeException("Conflict");
        }
        return true;
    }

    public User updateUser(Long userId, User user) {
        User savedUser = new User();
        emailIsUnique(user.getEmail());
        savedUser.setId(userId);
        savedUser.setName(user.getName());
        savedUser.setEmail(user.getEmail());
        users.put(userId, savedUser);
        return savedUser;
    }

    public void deleteUser(Long userId) {
        users.remove(userId);
    }

    private boolean isUserExists(Long userId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("Not found");
        }
        return true;
    }

    public Item createItem(Item item) {
        isUserExists(item.getOwner().getId());
        final Long id = getId();
        item.setId(id);
        items.put(id, item);
        return item;
    }

    public Item updateItem(Item item) {
        isUserExists(item.getOwner().getId());
        items.put(id, item);
        return item;
    }

    public List<Item> getAllItems(Long ownerId) {
        List<Item> ar = new ArrayList<>(items.values());
        return ar.stream().filter(item -> item.getOwner().getId().equals(ownerId)).toList();
    }

    public Item getItem(Long itemId) {
        return items.get(itemId);
    }

    public List<Item> search(String text) {
        List<Item> ar = new ArrayList<>(items.values());
        log.info("search " + ar.toString());
        return ar.stream()
                .filter(item -> item.getAvailable() == true)
                .filter(item -> item.getName().toUpperCase().contains(text.toUpperCase())
                        || item.getDescription().toUpperCase().contains(text.toUpperCase()))
                .toList();
    }
}
