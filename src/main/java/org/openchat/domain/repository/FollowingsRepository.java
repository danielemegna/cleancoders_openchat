package org.openchat.domain.repository;

import org.openchat.domain.entity.Following;

public interface FollowingsRepository {
    boolean alreadyExists(Following followingToCreate);

    void store(Following followingToCreate);
}
