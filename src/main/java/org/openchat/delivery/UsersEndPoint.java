package org.openchat.delivery;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
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
            JsonArray responseArray = new JsonArray();
            users.stream()
                .map(this::serializeUser)
                .forEach(responseArray::add);

            return new HexagonalResponse(200, "application/json", responseArray.toString());
        }

        User newUser = parseUser(request);
        String newUserUUID = useCase.register(newUser);
        String responseBody = serializeUser(newUser, newUserUUID).toString();
        return new HexagonalResponse(201, "application/json", responseBody);
    }

    private JsonObject serializeUser(User user) {
        return serializeUser(user, user.id);
    }

    private JsonObject serializeUser(User user, String userUUID) {
        return new JsonObject()
            .add("id", userUUID)
            .add("username", user.username)
            .add("about", user.about);
    }

    private User parseUser(HexagonalRequest request) {
        JsonObject userJson = Json.parse(request.body).asObject();
        return new User(
            userJson.getString("username", ""),
            userJson.getString("password", ""),
            userJson.getString("about", "")
        );
    }

}
