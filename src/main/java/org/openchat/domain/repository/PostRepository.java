package org.openchat.domain.repository;

import org.openchat.domain.entity.Post;

import java.util.List;

public interface PostRepository {
    String store(Post post);

    List<Post> getByUserId(String userId);
}
