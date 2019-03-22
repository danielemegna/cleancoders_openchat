package org.openchat.delivery.repository;

import org.openchat.domain.entity.User;
import org.openchat.domain.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
}
