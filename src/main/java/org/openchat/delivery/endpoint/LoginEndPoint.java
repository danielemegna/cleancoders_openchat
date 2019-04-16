package org.openchat.delivery.endpoint;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import org.openchat.delivery.HexagonalRequest;
import org.openchat.delivery.HexagonalResponse;
import org.openchat.domain.entity.User;
import org.openchat.domain.usecase.LoginUseCase;

public class LoginEndPoint implements EndPoint {

    private final LoginUseCase useCase;

    public LoginEndPoint(LoginUseCase useCase) {
        this.useCase = useCase;
    }

    public HexagonalResponse hit(HexagonalRequest request) {
        try {
            return runUseCase(request);
        } catch (LoginUseCase.InvalidCredentialsException ex) {
            return new HexagonalResponse(404, "text/plain", "Invalid credentials.");
        }
    }

    private HexagonalResponse runUseCase(HexagonalRequest request) {
        User userToLogin = parseUser(request);
        User loggedUser = useCase.login(userToLogin);
        return new HexagonalResponse(200, "application/json", serializeUser(loggedUser));
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
