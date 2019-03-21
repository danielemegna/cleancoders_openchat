package org.openchat;

import org.openchat.delivery.HexagonalRequest;
import org.openchat.delivery.HexagonalResponse;
import org.openchat.delivery.UsersEndPoint;
import org.openchat.delivery.repository.InMemoryUserRepository;
import spark.Request;
import spark.Response;

import static spark.Spark.*;

public class Routes {

    private UsersEndPoint usersEndPoint;

    public void create() {
        usersEndPoint = new UsersEndPoint(new InMemoryUserRepository());

        swaggerRoutes();
        openchatRoutes();
    }

    private void openchatRoutes() {
        get("status", (req, res) -> "OpenChat: OK!");
    }

    private void swaggerRoutes() {
        post("users", this::usersApi);
        options("login", (req, res) -> "OK");
        options("users/:userId/timeline", (req, res) -> "OK");
        options("followings", (req, res) -> "OK");
        options("followings/:userId/followees", (req, res) -> "OK");
        options("users/:userId/wall", (req, res) -> "OK");
    }

    private String usersApi(Request req, Response res) {
        HexagonalRequest hexagonalRequest = new HexagonalRequest(req.body());
        HexagonalResponse hexagonalResponse = usersEndPoint.hit(hexagonalRequest);
        res.status(hexagonalResponse.statusCode);
        res.type(hexagonalResponse.contentType);
        return hexagonalResponse.responseBody;
    }
}
