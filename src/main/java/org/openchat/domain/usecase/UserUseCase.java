package org.openchat.domain.usecase;

import org.openchat.domain.entity.User;
import org.openchat.domain.repository.UserRepository;

public class UserUseCase {
    private final UserRepository userRepository;

    public UserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String register(User newUser) {
        if (userRepository.isUsernameUsed(newUser.username))
            throw new UsernameAlreadyInUseException();

        return userRepository.add(newUser);
    }

    public class UsernameAlreadyInUseException extends RuntimeException {
    }
}
