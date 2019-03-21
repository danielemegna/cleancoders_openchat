package org.openchat.delivery;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import org.openchat.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UsersEndPoint {

    private List<String> registeredUsers = new ArrayList<>();

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
        if (registeredUsers.contains(newUser.username)) {
            throw new RuntimeException("Username already in use.");
        } else {
            registeredUsers.add(newUser.username);
            return UUID.randomUUID().toString();
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
