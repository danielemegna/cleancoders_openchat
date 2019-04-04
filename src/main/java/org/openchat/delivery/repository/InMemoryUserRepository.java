package org.openchat.delivery.repository;

import org.openchat.domain.entity.User;
import org.openchat.domain.repository.UserRepository;

import java.util.*;

public class InMemoryUserRepository implements UserRepository {

    private List<User> store = new ArrayList<>();

    @Override
    public boolean isUsernameUsed(String username) {
        return store.stream().anyMatch(u -> u.username.equals(username));
    }

    @Override
    public String add(User newUser) {
        User storedUser = new User(UUID.randomUUID().toString(), newUser.username, newUser.password, newUser.about);
        store.add(storedUser);
        return storedUser.id;
    }

    @Override
    public Optional<User> getByCredentials(String username, String password) {
        return store.stream()
            .filter(u -> u.username.equals(username) && u.password.equals(password))
            .findFirst();
    }

    @Override
    public List<User> getAll() {
        return Collections.unmodifiableList(store);
    }

    @Override
    public Optional<User> getById(String followerId) {
        return store.stream()
            .filter(u -> u.id.equals(followerId))
            .findFirst();
    }
}
