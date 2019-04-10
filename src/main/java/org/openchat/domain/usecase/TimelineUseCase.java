package org.openchat.domain.usecase;

import org.openchat.domain.entity.Post;
import org.openchat.domain.repository.PostRepository;

import java.util.List;

public class TimelineUseCase {

    private final PostRepository postRepository;

    public TimelineUseCase(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post publish(Post toBeCreated) {
        String postId = postRepository.store(toBeCreated);
        return new Post(
            postId,
            toBeCreated.userId,
            toBeCreated.text,
            toBeCreated.dateTime
        );
    }

    public List<Post> getPostByUser(String userId) {
        return postRepository.getByUserId(userId);
    }
}
