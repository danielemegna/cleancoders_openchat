package org.openchat;

import org.openchat.delivery.HexagonalRequest;
import org.openchat.delivery.HexagonalResponse;
import org.openchat.delivery.LoginEndPoint;
import org.openchat.delivery.UsersEndPoint;
import org.openchat.delivery.repository.InMemoryUserRepository;
import org.openchat.domain.usecase.LoginUseCase;
import org.openchat.domain.usecase.UserUseCase;
import spark.Request;
import spark.Response;

import static spark.Spark.*;

public class Routes {

    private UsersEndPoint usersEndPoint;
    private LoginEndPoint loginEndPoint;

    public void create() {
        InMemoryUserRepository userRepository = new InMemoryUserRepository();

        usersEndPoint = new UsersEndPoint(
            new UserUseCase(userRepository)
        );

        loginEndPoint = new LoginEndPoint(
            new LoginUseCase(userRepository)
        );

        swaggerRoutes();
        openchatRoutes();
    }

    private void openchatRoutes() {
        get("status", (req, res) -> "OpenChat: OK!");
    }

    private void swaggerRoutes() {
        post("users", this::usersApi);
        post("login", this::loginApi);
        options("users/:userId/timeline", (req, res) -> "OK");
        options("followings", (req, res) -> "OK");
        options("followings/:userId/followees", (req, res) -> "OK");
        options("users/:userId/wall", (req, res) -> "OK");
    }

    private String loginApi(Request req, Response res) {
        HexagonalRequest hexagonalRequest = new HexagonalRequest(req.body());
        HexagonalResponse hexagonalResponse = loginEndPoint.hit(hexagonalRequest);
        res.status(hexagonalResponse.statusCode);
        res.type(hexagonalResponse.contentType);
        return hexagonalResponse.responseBody;
    }

    private String usersApi(Request req, Response res) {
        HexagonalRequest hexagonalRequest = new HexagonalRequest(req.body());
        HexagonalResponse hexagonalResponse = usersEndPoint.hit(hexagonalRequest);
        res.status(hexagonalResponse.statusCode);
        res.type(hexagonalResponse.contentType);
        return hexagonalResponse.responseBody;
    }
}
