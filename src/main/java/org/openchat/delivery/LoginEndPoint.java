package org.openchat.delivery;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import org.openchat.domain.entity.User;
import org.openchat.domain.usecase.LoginUseCase;

public class LoginEndPoint {

    private final LoginUseCase useCase;

    public LoginEndPoint(LoginUseCase useCase) {
        this.useCase = useCase;
    }

    public HexagonalResponse hit(HexagonalRequest request) {
        String responseBody = runUseCase(request);
        return new HexagonalResponse(
            200, "application/json", responseBody
        );
    }

    private String runUseCase(HexagonalRequest request) {
        User userToLogin = parseUser(request);
        User loggedUser = useCase.login(userToLogin);
        return serializeUser(loggedUser);
    }

    private String serializeUser(User user) {
        return new JsonObject()
            .add("id", user.id)
            .add("username", user.username)
            .add("about", user.about)
            .toString();
    }

    private User parseUser(HexagonalRequest request) {
        JsonObject userJson = Json.parse(request.body).asObject();
        return new User(
            userJson.getString("username", ""),
            userJson.getString("password", "")
        );
    }
}
