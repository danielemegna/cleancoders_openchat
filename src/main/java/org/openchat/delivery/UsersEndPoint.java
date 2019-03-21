package org.openchat.delivery;

import com.eclipsesource.json.JsonObject;
import spark.Request;
import spark.Response;

public class UsersEndPoint {
    public Object hit(Request req, Response res) {
        res.status(201);
        res.type("application/json");

        return new JsonObject()
            .add("id", "3b230a0a-4a8d-4dd6-80f7-338f0be2f67f")
            .add("username", "Lucy")
            .add("about", "About Lucy")
            .toString();
    }
}
