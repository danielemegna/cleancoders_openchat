package org.openchat;

import org.openchat.delivery.UsersEndPoint;

import static spark.Spark.*;

public class Routes {

    private UsersEndPoint usersEndPoint;

    public void create() {
        usersEndPoint = new UsersEndPoint();

        swaggerRoutes();
        openchatRoutes();
    }

    private void openchatRoutes() {
        get("status", (req, res) -> "OpenChat: OK!");
    }

    private void swaggerRoutes() {
        post("users", (req, res) -> usersEndPoint.hit(req, res));
        options("login", (req, res) -> "OK");
        options("users/:userId/timeline", (req, res) -> "OK");
        options("followings", (req, res) -> "OK");
        options("followings/:userId/followees", (req, res) -> "OK");
        options("users/:userId/wall", (req, res) -> "OK");
    }
}
