package org.openchat.delivery.endpoint;

import com.eclipsesource.json.JsonObject;
import org.openchat.delivery.HexagonalRequest;
import org.openchat.delivery.HexagonalResponse;
import org.openchat.domain.entity.Post;

import java.util.UUID;

public class TimelineEndPoint implements EndPoint {

    public HexagonalResponse hit(HexagonalRequest request) {
        return runUseCase(request);
    }

    private HexagonalResponse runUseCase(HexagonalRequest request) {
        Post newPost = createPost(request);
        return new HexagonalResponse(201, "application/json", serializePost(newPost));
    }

    private Post createPost(HexagonalRequest request) {
        return new Post(
            UUID.randomUUID().toString(),
            request.params.get(":userid"),
            "Hello, I'm Alice",
            "2018-01-10T11:30:00Z"
        );
    }

    private String serializePost(Post newPost) {
        return new JsonObject()
            .add("postId", newPost.id)
            .add("userId", newPost.userId)
            .add("text", newPost.text)
            .add("dateTime", newPost.dateTime)
            .toString();
    }

}
