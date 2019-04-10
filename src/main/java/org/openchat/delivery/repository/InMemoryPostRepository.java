package org.openchat.delivery.repository;

import org.openchat.domain.entity.Post;
import org.openchat.domain.repository.PostRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.Comparator.comparing;
import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.toList;

public class InMemoryPostRepository implements PostRepository {

    private final List<Post> posts = new ArrayList<>();

    @Override
    public String store(Post post) {
        String newPostId = UUID.randomUUID().toString();
        posts.add(new Post(newPostId, post.userId, post.text, post.dateTime));
        return newPostId;
    }

    @Override
    public List<Post> getByUserId(String userId) {
        return posts.stream()
            .filter(p -> p.userId.equals(userId))
            .sorted(comparing(p -> p.dateTime, reverseOrder()))
            .collect(toList());
    }
}
