package org.openchat.delivery;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import org.openchat.domain.entity.User;
import org.openchat.domain.usecase.UserUseCase;


public class UsersEndPoint implements EndPoint {

    private final UserUseCase useCase;

    public UsersEndPoint(UserUseCase useCase) {
        this.useCase = useCase;
    }

    public HexagonalResponse hit(HexagonalRequest request) {
        try {
            String responseBody = runUseCase(request);
            return new HexagonalResponse(201, "application/json", responseBody);
        } catch (UserUseCase.UsernameAlreadyInUseException ex) {
            return new HexagonalResponse(400, "text/plain", "Username already in use.");
        }
    }

    private String runUseCase(HexagonalRequest request) {
        User newUser = parseUser(request);
        String newUserUUID = useCase.register(newUser);
        return serializeUser(newUser, newUserUUID);
    }

    private String serializeUser(User user, String userUUID) {
        return new JsonObject()
            .add("id", userUUID)
            .add("username", user.username)
            .add("about", user.about)
            .toString();
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
