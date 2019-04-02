package org.openchat.domain.usecase;

import org.openchat.domain.entity.Following;
import org.openchat.domain.repository.FollowingsRepository;

public class FollowingsUseCase {

    private final FollowingsRepository followingsRepository;

    public FollowingsUseCase(FollowingsRepository followingsRepository) {
        this.followingsRepository = followingsRepository;
    }

    public void create(Following followingToCreate) {
        if (followingsRepository.alreadyExists(followingToCreate))
            throw new FollowingAlreadyExist();

        followingsRepository.store(followingToCreate);
    }

    public class FollowingAlreadyExist extends RuntimeException {
    }
}
