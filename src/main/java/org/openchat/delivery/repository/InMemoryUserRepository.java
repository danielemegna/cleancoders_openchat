package org.openchat.delivery.repository;

import org.openchat.domain.User;
import org.openchat.domain.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
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
}
