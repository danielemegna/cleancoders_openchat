package org.openchat.delivery;

import java.util.Map;

import static java.util.Collections.emptyMap;

public class HexagonalRequest {
    public final String body;
    public final Map<String, String> params;
    public final String method;

    public HexagonalRequest(String body) {
        this(body, emptyMap(), "POST");
    }

    public HexagonalRequest(String body, String method) {
        this(body, emptyMap(), method);
    }

    public HexagonalRequest(String body, Map<String, String> params, String requestMethod) {
        this.body = body;
        this.params = params;
        this.method = requestMethod;
    }
}
