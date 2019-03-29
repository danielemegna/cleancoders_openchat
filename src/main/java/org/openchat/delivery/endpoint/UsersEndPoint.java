package org.openchat.delivery.endpoint;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import org.openchat.delivery.HexagonalRequest;
import org.openchat.delivery.HexagonalResponse;
import org.openchat.domain.entity.User;
import org.openchat.domain.usecase.UserUseCase;

import java.util.List;

public class UsersEndPoint implements EndPoint {

    private final UserUseCase useCase;

    public UsersEndPoint(UserUseCase useCase) {
        this.useCase = useCase;
    }

    public HexagonalResponse hit(HexagonalRequest request) {
        try {
            return runUseCase(request);
        } catch (UserUseCase.UsernameAlreadyInUseException ex) {
            return new HexagonalResponse(400, "text/plain", "Username already in use.");
        }
    }

    private HexagonalResponse runUseCase(HexagonalRequest request) {
        if (request.method.equals("GET")) {
            List<User> users = useCase.getAll();
            return new HexagonalResponse(200, "application/json", serialize(users));
        }

        User newUser = parseUserFrom(request);
        String newUserUUID = useCase.register(newUser);
        return new HexagonalResponse(201, "application/json", serialize(newUser, newUserUUID));
    }

    private String serialize(List<User> users) {
        JsonArray responseArray = new JsonArray();
        users.stream()
            .map(this::userToJsonObject)
            .forEach(responseArray::add);

        return responseArray.toString();
    }

    private String serialize(User user, String userUUID) {
        return userToJsonObject(user, userUUID).toString();
    }

    private JsonObject userToJsonObject(User user) {
        return userToJsonObject(user, user.id);
    }

    private JsonObject userToJsonObject(User user, String userUUID) {
        return new JsonObject()
            .add("id", userUUID)
            .add("username", user.username)
            .add("about", user.about);
    }

    private User parseUserFrom(HexagonalRequest request) {
        JsonObject userJson = Json.parse(request.body).asObject();
        return new User(
            userJson.getString("username", ""),
            userJson.getString("password", ""),
            userJson.getString("about", "")
        );
    }

}
