package org.openchat.domain.repository;

import org.openchat.domain.entity.Following;

import java.util.List;

public interface FollowingsRepository {
    boolean alreadyExists(Following followingToCreate);

    void store(Following followingToCreate);

    List<Following> getByFollowerId(String userId);
}
