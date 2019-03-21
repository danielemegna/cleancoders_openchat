package org.openchat.delivery;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import org.openchat.domain.User;
import org.openchat.domain.repository.UserRepository;


public class UsersEndPoint {

    private final UserRepository userRepository;

    public UsersEndPoint(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public HexagonalResponse hit(HexagonalRequest request) {
        try {
            User newUser = parseUser(request);
            String newUserUUID = register(newUser);
            String responseBody = new JsonObject()
                .add("id", newUserUUID)
                .add("username", newUser.username)
                .add("about", newUser.about)
                .toString();
            return new HexagonalResponse(201, "application/json", responseBody);
        } catch (RuntimeException ex) {
            return new HexagonalResponse(400, "text/plain", ex.getMessage());
        }
    }

    private String register(User newUser) {
        if (userRepository.isUsernameUsed(newUser.username)) {
            throw new RuntimeException("Username already in use.");
        } else {
            return userRepository.add(newUser);
        }
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
