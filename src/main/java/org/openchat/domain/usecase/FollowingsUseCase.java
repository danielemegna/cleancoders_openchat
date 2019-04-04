package org.openchat.domain.usecase;

import org.openchat.domain.entity.Following;
import org.openchat.domain.entity.User;
import org.openchat.domain.repository.FollowingsRepository;
import org.openchat.domain.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FollowingsUseCase {

    private final FollowingsRepository followingsRepository;
    private final UserRepository userRepository;

    public FollowingsUseCase(FollowingsRepository followingsRepository, UserRepository userRepository) {
        this.followingsRepository = followingsRepository;
        this.userRepository = userRepository;
    }

    public void create(Following followingToCreate) {
        if (followingsRepository.alreadyExists(followingToCreate))
            throw new FollowingAlreadyExist();

        followingsRepository.store(followingToCreate);
    }

    public List<User> getFollowedBy(String userId) {
        return followingsRepository.getByFollowerId(userId).stream()
            .map(f -> userRepository.getById(f.followeeId))
            .map(Optional::get)
            .collect(Collectors.toList());
    }

    public class FollowingAlreadyExist extends RuntimeException {
    }
}
