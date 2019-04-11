package org.openchat.domain.usecase;

import org.openchat.domain.entity.Post;
import org.openchat.domain.repository.FollowingsRepository;
import org.openchat.domain.repository.PostRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.Comparator.reverseOrder;

public class WallUseCase {
    private final PostRepository postRepository;
    private final FollowingsRepository followingsRepository;

    public WallUseCase(PostRepository postRepository, FollowingsRepository followingsRepository) {
        this.postRepository = postRepository;
        this.followingsRepository = followingsRepository;
    }

    public List<Post> getWallPostsByUser(String userId) {
        return Stream.concat(
            followingsRepository.getByFollowerId(userId).stream()
                .flatMap(f -> postRepository.getByUserId(f.followeeId).stream()),
            postRepository.getByUserId(userId).stream()
        ).sorted(comparing(p -> p.dateTime, reverseOrder()))
            .collect(Collectors.toList());
    }
}
