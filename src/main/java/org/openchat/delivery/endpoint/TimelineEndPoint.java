package org.openchat.delivery.endpoint;

import com.eclipsesource.json.JsonObject;
import org.openchat.delivery.HexagonalRequest;
import org.openchat.delivery.HexagonalResponse;

import java.util.UUID;

public class TimelineEndPoint implements EndPoint {

    public HexagonalResponse hit(HexagonalRequest hexagonalRequest) {
        String responseBody = new JsonObject()
            .add("postId", UUID.randomUUID().toString())
            .add("userId", hexagonalRequest.params.get(":userid"))
            .add("text", "Hello, I'm Alice")
            .add("dateTime", "2018-01-10T11:30:00Z")
            .toString();

        return new HexagonalResponse(201, "application/json", responseBody);
    }

}
