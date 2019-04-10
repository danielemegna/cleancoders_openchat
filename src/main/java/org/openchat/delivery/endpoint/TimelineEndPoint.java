package org.openchat.delivery.endpoint;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import org.openchat.delivery.HexagonalRequest;
import org.openchat.delivery.HexagonalResponse;
import org.openchat.domain.entity.Post;
import org.openchat.domain.usecase.TimelineUseCase;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TimelineEndPoint implements EndPoint {

    private final TimelineUseCase usecase;

    public TimelineEndPoint(TimelineUseCase usecase) {
        this.usecase = usecase;
    }

    public HexagonalResponse hit(HexagonalRequest request) {
        return runUseCase(request);
    }

    private HexagonalResponse runUseCase(HexagonalRequest request) {
        if (request.method.equals("GET")) {
            String userId = request.params.get(":userid");
            List<Post> posts = usecase.getPostByUser(userId);
            return new HexagonalResponse(200, "application/json", serialize(posts));
        }

        Post toBePublished = parsePostFrom(request);
        Post publishedPost = usecase.publish(toBePublished);
        return new HexagonalResponse(201, "application/json", serialize(publishedPost));
    }

    private Post parsePostFrom(HexagonalRequest request) {
        JsonObject postJson = Json.parse(request.body).asObject();
        return new Post(
            request.params.get(":userid"),
            postJson.getString("text", "")
        );
    }

    private String serialize(Post post) {
        return postToJsonObject(post).toString();
    }

    private String serialize(List<Post> posts) {
        JsonArray responseArray = new JsonArray();
        posts.stream()
            .map(this::postToJsonObject)
            .forEach(responseArray::add);

        return responseArray.toString();
    }

    private JsonObject postToJsonObject(Post newPost) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX");
        ZonedDateTime zonedDateTime = newPost.dateTime.withZoneSameInstant(ZoneOffset.UTC);
        return new JsonObject()
            .add("postId", newPost.id)
            .add("userId", newPost.userId)
            .add("text", newPost.text)
            .add("dateTime", dateTimeFormatter.format(zonedDateTime));
    }

}
