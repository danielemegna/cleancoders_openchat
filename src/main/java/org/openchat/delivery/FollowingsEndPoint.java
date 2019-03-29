package org.openchat.delivery;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import org.openchat.domain.entity.Following;
import org.openchat.domain.usecase.FollowingsUseCase;

public class FollowingsEndPoint implements EndPoint {

    private final FollowingsUseCase useCase;

    public FollowingsEndPoint(FollowingsUseCase useCase) {
        this.useCase = useCase;
    }

    public HexagonalResponse hit(HexagonalRequest request) {
        try {
            Following followingToCreate = parse(request);
            useCase.create(followingToCreate);
            return new HexagonalResponse(201, "text/plain", "Following created.");
        } catch (FollowingsUseCase.FollowingAlreadyExist ex) {
            return new HexagonalResponse(400, "text/plain", "Following already exist.");
        }
    }

    private Following parse(HexagonalRequest request) {
        JsonObject followingJson = Json.parse(request.body).asObject();
        return new Following(
            followingJson.getString("followerId", ""),
            followingJson.getString("followeeId", "")
        );
    }
}
