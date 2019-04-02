package org.openchat.delivery.repository;

import org.openchat.domain.entity.Following;
import org.openchat.domain.repository.FollowingsRepository;

import java.util.ArrayList;
import java.util.List;

public class InMemoryFollowingsRepository implements FollowingsRepository {

    private final List<Following> followings = new ArrayList<>();

    @Override
    public boolean alreadyExists(Following followingToCreate) {
        return followings.contains(followingToCreate);
    }

    @Override
    public void store(Following followingToCreate) {
        followings.add(followingToCreate);
    }
}
