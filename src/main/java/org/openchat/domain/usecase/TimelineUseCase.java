package org.openchat.domain.usecase;

import org.openchat.domain.entity.Post;

import java.util.UUID;

public class TimelineUseCase {
    public Post storePost(Post toBeCreated) {
        return new Post(
            UUID.randomUUID().toString(),
            toBeCreated.userId,
            toBeCreated.text,
            toBeCreated.dateTime
        );
    }
}
