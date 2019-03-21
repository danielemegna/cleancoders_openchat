package org.openchat.delivery;

import com.eclipsesource.json.JsonObject;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.List;

public class UsersEndPoint {

    private List<String> registeredUsers = new ArrayList<>();

    public Object hit(Request req, Response res) {
        if (registeredUsers.contains("Lucy")) {
            res.status(400);
            res.type("text/plain");
            return "Username already in use.";
        } else {
            registeredUsers.add("Lucy");
            res.status(201);
            res.type("application/json");

            return new JsonObject()
                .add("id", "3b230a0a-4a8d-4dd6-80f7-338f0be2f67f")
                .add("username", "Lucy")
                .add("about", "About Lucy")
                .toString();
        }
    }

}
