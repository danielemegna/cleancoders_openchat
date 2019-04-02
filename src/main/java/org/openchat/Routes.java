package org.openchat;

import org.openchat.delivery.HexagonalRequest;
import org.openchat.delivery.HexagonalResponse;
import org.openchat.delivery.endpoint.EndPoint;
import org.openchat.delivery.endpoint.FollowingsEndPoint;
import org.openchat.delivery.endpoint.LoginEndPoint;
import org.openchat.delivery.endpoint.UsersEndPoint;
import org.openchat.delivery.repository.InMemoryFollowingsRepository;
import org.openchat.delivery.repository.InMemoryUserRepository;
import org.openchat.domain.repository.FollowingsRepository;
import org.openchat.domain.usecase.FollowingsUseCase;
import org.openchat.domain.usecase.LoginUseCase;
import org.openchat.domain.usecase.UserUseCase;
import spark.Request;
import spark.Response;

import static spark.Spark.*;

public class Routes {

    private UsersEndPoint usersEndPoint;
    private LoginEndPoint loginEndPoint;
    private FollowingsEndPoint followingsEndPoint;

    public void create() {
        InMemoryUserRepository userRepository = new InMemoryUserRepository();
        FollowingsRepository followingsRepository = new InMemoryFollowingsRepository();

        usersEndPoint = new UsersEndPoint(
            new UserUseCase(userRepository)
        );

        loginEndPoint = new LoginEndPoint(
            new LoginUseCase(userRepository)
        );

        followingsEndPoint = new FollowingsEndPoint(
            new FollowingsUseCase(followingsRepository)
        );

        swaggerRoutes();
        openchatRoutes();
    }

    private void openchatRoutes() {
        get("status", (req, res) -> "OpenChat: OK!");
        post("users", (req, res) -> handleWith(usersEndPoint, req, res));
        post("login", (req, res) -> handleWith(loginEndPoint, req, res));
        get("users", (req, res) -> handleWith(usersEndPoint, req, res));
        post("followings", (req, res) -> handleWith(followingsEndPoint, req, res));
        get("followings/:userId/followees", (req, res) -> handleWith(followingsEndPoint, req, res));
    }

    private void swaggerRoutes() {
        options("users", (req, res) -> "OK");
        options("login", (req, res) -> "OK");
        options("users/:userId/timeline", (req, res) -> "OK");
        options("followings", (req, res) -> "OK");
        options("followings/:userId/followees", (req, res) -> "OK");
        options("users/:userId/wall", (req, res) -> "OK");
    }

    private String handleWith(EndPoint endPoint, Request sparkRequest, Response sparkResponse) {
        HexagonalResponse hexagonalResponse = endPoint.hit(
            new HexagonalRequest(
                sparkRequest.body(),
                sparkRequest.params(),
                sparkRequest.requestMethod()
            )
        );

        sparkResponse.status(hexagonalResponse.statusCode);
        sparkResponse.type(hexagonalResponse.contentType);
        return hexagonalResponse.responseBody;
    }
}
