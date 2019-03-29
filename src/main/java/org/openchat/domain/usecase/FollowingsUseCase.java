package org.openchat.domain.usecase;

import org.openchat.domain.entity.Following;

import java.util.ArrayList;
import java.util.List;

public class FollowingsUseCase {
    private List<Following> store = new ArrayList<>();

    public void create(Following followingToCreate) {
        if (store.contains(followingToCreate))
            throw new FollowingAlreadyExist();
        store.add(followingToCreate);
    }

    public class FollowingAlreadyExist extends RuntimeException {
    }
}
