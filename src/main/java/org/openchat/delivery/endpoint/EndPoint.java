package org.openchat.delivery.endpoint;

import org.openchat.delivery.HexagonalRequest;
import org.openchat.delivery.HexagonalResponse;

public interface EndPoint {
    HexagonalResponse hit(HexagonalRequest request);
}
