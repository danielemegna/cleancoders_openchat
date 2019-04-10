package org.openchat.domain.usecase;

import org.openchat.domain.entity.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.Comparator.comparing;
import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.toList;

public class TimelineUseCase {

    private List<Post> posts = new ArrayList<>();

    public Post publish(Post toBeCreated) {
        Post newPost = new Post(
            UUID.randomUUID().toString(),
            toBeCreated.userId,
            toBeCreated.text,
            toBeCreated.dateTime
        );

        posts.add(newPost);
        return newPost;
    }

    public List<Post> getPostByUser(String userId) {
        return posts.stream()
            .filter(p -> p.userId.equals(userId))
            .sorted(comparing(p -> p.dateTime, reverseOrder()))
            .collect(toList());
    }
}
