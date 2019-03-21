package org.openchat.delivery;

public class HexagonalResponse {
    public final int statusCode;
    public final String contentType;
    public final String responseBody;

    public HexagonalResponse(int statusCode, String contentType, String responseBody) {
        this.statusCode = statusCode;
        this.contentType = contentType;
        this.responseBody = responseBody;
    }
}
