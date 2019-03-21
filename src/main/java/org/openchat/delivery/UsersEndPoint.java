package org.openchat.delivery;

import spark.Request;
import spark.Response;

public class UsersEndPoint {
    public Object hit(Request req, Response res) {
        res.status(201);
        res.type("application/json");

        return "{\n" +
            "  \"id\":\"3b230a0a-4a8d-4dd6-80f7-338f0be2f67f\",\n" +
            "  \"username\":\"Lucy\",\n" +
            "  \"about\":\"About Lucy\"\n" +
            "}";
    }
}
