package org.openchat.domain.usecase;

import org.openchat.domain.entity.Post;
import org.openchat.domain.repository.PostRepository;

import java.time.ZonedDateTime;
import java.util.List;

public class TimelineUseCase {

    private final PostRepository postRepository;

    public TimelineUseCase(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post publish(Post toBePublished) {
        Post postWithDate = new Post(toBePublished.userId, toBePublished.text, ZonedDateTime.now());
        String postId = postRepository.store(postWithDate);
        return new Post(postId, postWithDate.userId, postWithDate.text, postWithDate.dateTime);
    }

    public List<Post> getPostsByUser(String userId) {
        return postRepository.getByUserId(userId);
    }
}
