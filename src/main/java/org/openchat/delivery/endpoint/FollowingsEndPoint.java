package org.openchat.delivery.endpoint;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import org.openchat.delivery.HexagonalRequest;
import org.openchat.delivery.HexagonalResponse;
import org.openchat.domain.entity.Following;
import org.openchat.domain.entity.User;
import org.openchat.domain.usecase.FollowingsUseCase;

import java.util.List;

public class FollowingsEndPoint implements EndPoint {

    private final FollowingsUseCase useCase;

    public FollowingsEndPoint(FollowingsUseCase useCase) {
        this.useCase = useCase;
    }

    public HexagonalResponse hit(HexagonalRequest request) {
        try {
            return runUseCase(request);
        } catch (FollowingsUseCase.FollowingAlreadyExist ex) {
            return new HexagonalResponse(400, "text/plain", "Following already exist.");
        }
    }

    private HexagonalResponse runUseCase(HexagonalRequest request) {
        if (request.method.equals("GET") && !request.params.isEmpty()) {
            String userId = request.params.get(":userid");
            List<User> followed = useCase.getFollowedBy(userId);
            return new HexagonalResponse(200, "application/json", serialize(followed));
        }

        Following followingToCreate = parse(request);
        useCase.create(followingToCreate);
        return new HexagonalResponse(201, "text/plain", "Following created.");
    }

    private String serialize(List<User> users) {
        JsonArray responseArray = new JsonArray();
        users.stream()
            .map(this::toJsonObject)
            .forEach(responseArray::add);

        return responseArray.toString();
    }

    private JsonObject toJsonObject(User user) {
        return new JsonObject()
            .add("id", user.id)
            .add("username", user.username)
            .add("about", user.about);
    }

    private Following parse(HexagonalRequest request) {
        JsonObject followingJson = Json.parse(request.body).asObject();
        return new Following(
            followingJson.getString("followerId", ""),
            followingJson.getString("followeeId", "")
        );
    }
}
