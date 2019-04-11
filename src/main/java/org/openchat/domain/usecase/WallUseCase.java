package org.openchat.domain.usecase;

import org.openchat.domain.entity.Post;
import org.openchat.domain.repository.PostRepository;

import java.util.List;

public class WallUseCase {
    private final PostRepository postRepository;

    public WallUseCase(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> getWallPostsByUser(String userId) {
        return postRepository.getByUserId(userId);
    }
}
