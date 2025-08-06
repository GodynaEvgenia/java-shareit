package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.InMemoryStorage;

import java.util.List;

@Service
public class UserService {
    private InMemoryStorage inMemoryStorage;

    @Autowired
    public UserService(InMemoryStorage inMemoryStorage) {
        this.inMemoryStorage = inMemoryStorage;
    }

    public User findById(long userId) {
        return inMemoryStorage.findUserById(userId);
    }

    public List<User> getAll() {
        return null;
    }

    public User create(User user) {
        return inMemoryStorage.createUser(user);
    }

    public User update(Long usereId, User user) {

        return inMemoryStorage.updateUser(usereId, user);
    }

    public void deleteUser(Long userId){
        inMemoryStorage.deleteUser(userId);
    }
}
