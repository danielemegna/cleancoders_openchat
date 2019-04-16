package org.openchat.domain.usecase;

import org.openchat.domain.entity.Post;
import org.openchat.domain.repository.PostRepository;
import org.openchat.domain.repository.UserRepository;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TimelineUseCase {

    private final static Set<String> INNAPROPRIATE_WORDS = new HashSet<String>() {{
        add("orange");
        add("oranges");
        add("ice cream");
        add("ice creams");
        add("elephant");
        add("elephants");
    }};

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public TimelineUseCase(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public Post publish(Post toBePublished) {
        checkUser(toBePublished);
        checkLanguage(toBePublished);

        Post postWithDate = new Post(toBePublished.userId, toBePublished.text, ZonedDateTime.now());
        String postId = postRepository.store(postWithDate);
        return new Post(postId, postWithDate.userId, postWithDate.text, postWithDate.dateTime);
    }

    private void checkUser(Post toBePublished) {
        if (!userRepository.getById(toBePublished.userId).isPresent())
            throw new NotExistingUserPublishAttemptException();
    }

    private void checkLanguage(Post toBePublished) {
        boolean containsInappropriateWords = INNAPROPRIATE_WORDS.stream().anyMatch(
            word -> toBePublished.text.toUpperCase().contains(word.toUpperCase())
        );

        if (containsInappropriateWords) throw new InappropriateLanguageException();
    }

    public List<Post> getPostsByUser(String userId) {
        return postRepository.getByUserId(userId);
    }

    public class InappropriateLanguageException extends RuntimeException {
    }

    public class NotExistingUserPublishAttemptException extends RuntimeException {
    }
}
