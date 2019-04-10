package org.openchat.delivery.endpoint;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import org.openchat.delivery.HexagonalRequest;
import org.openchat.delivery.HexagonalResponse;
import org.openchat.domain.entity.Post;
import org.openchat.domain.usecase.TimelineUseCase;

public class TimelineEndPoint implements EndPoint {

    private final TimelineUseCase usecase;

    public TimelineEndPoint(TimelineUseCase usecase) {
        this.usecase = usecase;
    }

    public HexagonalResponse hit(HexagonalRequest request) {
        return runUseCase(request);
    }

    private HexagonalResponse runUseCase(HexagonalRequest request) {
        Post toBeCreated = parsePostFrom(request);
        Post newPost = usecase.storePost(toBeCreated);
        return new HexagonalResponse(201, "application/json", serialize(newPost));
    }

    private Post parsePostFrom(HexagonalRequest request) {
        JsonObject postJson = Json.parse(request.body).asObject();
        return new Post(
            request.params.get(":userid"),
            postJson.getString("text", ""),
            "2018-01-10T11:30:00Z"
        );
    }

    private String serialize(Post newPost) {
        return new JsonObject()
            .add("postId", newPost.id)
            .add("userId", newPost.userId)
            .add("text", newPost.text)
            .add("dateTime", newPost.dateTime)
            .toString();
    }

}
