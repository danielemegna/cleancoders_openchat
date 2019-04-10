package org.openchat.domain.repository;

import org.openchat.domain.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    boolean isUsernameUsed(String username);

    String add(User newUser);

    Optional<User> getByCredentials(String username, String password);

    List<User> getAll();

    Optional<User> getById(String followerId);
}
