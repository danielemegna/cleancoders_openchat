package org.openchat.delivery.endpoint;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import org.openchat.delivery.HexagonalRequest;
import org.openchat.delivery.HexagonalResponse;
import org.openchat.domain.entity.Following;
import org.openchat.domain.usecase.FollowingsUseCase;

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
            return new HexagonalResponse(200, "application/json", "[]");
        }

        Following followingToCreate = parse(request);
        useCase.create(followingToCreate);
        return new HexagonalResponse(201, "text/plain", "Following created.");
    }

    private Following parse(HexagonalRequest request) {
        JsonObject followingJson = Json.parse(request.body).asObject();
        return new Following(
            followingJson.getString("followerId", ""),
            followingJson.getString("followeeId", "")
        );
    }
}
