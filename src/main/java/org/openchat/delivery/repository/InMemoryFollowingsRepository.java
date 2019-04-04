package org.openchat.delivery.repository;

import org.openchat.domain.entity.Following;
import org.openchat.domain.repository.FollowingsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<Following> getByFollowerId(String userId) {
        return followings.stream()
            .filter(f -> f.followerId.equals(userId))
            .collect(Collectors.toList());
    }
}
