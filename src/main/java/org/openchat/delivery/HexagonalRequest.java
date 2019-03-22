package org.openchat.delivery;

public class HexagonalRequest {
    public final String body;
    public final String method;

    public HexagonalRequest(String body, String method) {
        this.body = body;
        this.method = method;
    }
}
