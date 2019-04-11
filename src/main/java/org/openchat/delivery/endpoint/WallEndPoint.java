package org.openchat.delivery.endpoint;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import org.openchat.delivery.HexagonalRequest;
import org.openchat.delivery.HexagonalResponse;
import org.openchat.domain.entity.Post;
import org.openchat.domain.usecase.WallUseCase;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class WallEndPoint implements EndPoint {

    private final WallUseCase useCase;

    public WallEndPoint(WallUseCase useCase) {
        this.useCase = useCase;
    }

    @Override
    public HexagonalResponse hit(HexagonalRequest request) {
        String userId = request.params.get(":userid");
        List<Post> posts = useCase.getWallPostsByUser(userId);
        return new HexagonalResponse(200, "application/json", serialize(posts));
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
