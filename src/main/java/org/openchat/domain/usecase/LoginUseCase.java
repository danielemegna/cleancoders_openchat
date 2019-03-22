package org.openchat.domain.usecase;

import org.openchat.domain.entity.User;
import org.openchat.domain.repository.UserRepository;

import java.util.Optional;

public class LoginUseCase {
    private final UserRepository userRepository;

    public LoginUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(User userToLogin) {
        Optional<User> user = userRepository.getByCredentials(userToLogin.username, userToLogin.password);
        return user.get();
    }
}
