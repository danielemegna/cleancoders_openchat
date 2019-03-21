package org.openchat.delivery;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import org.openchat.domain.User;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UsersEndPoint {

    private List<String> registeredUsers = new ArrayList<>();

    public Object hit(Request req, Response res) {
        User newUser = parseUser(req);
        if (registeredUsers.contains(newUser.username)) {
            res.status(400);
            res.type("text/plain");
            return "Username already in use.";
        } else {
            registeredUsers.add(newUser.username);
            res.status(201);
            res.type("application/json");
            return new JsonObject()
                .add("id", UUID.randomUUID().toString())
                .add("username", newUser.username)
                .add("about", newUser.about)
                .toString();
        }
    }

    private User parseUser(Request req) {
        JsonObject userJson = Json.parse(req.body()).asObject();
        return new User(
            userJson.getString("username", ""),
            userJson.getString("password", ""),
            userJson.getString("about", "")
        );
    }

}
