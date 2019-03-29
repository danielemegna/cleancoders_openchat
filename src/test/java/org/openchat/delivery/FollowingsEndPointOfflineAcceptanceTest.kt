package org.openchat.delivery

import org.junit.Test
import org.openchat.delivery.endpoint.FollowingsEndPoint
import org.openchat.domain.usecase.FollowingsUseCase
import java.util.*
import kotlin.test.assertEquals

class FollowingsEndPointOfflineAcceptanceTest() {

    companion object {
        private val A_REGISTERED_USER_UUID = UUID.randomUUID().toString();
        private val ANOTHER_REGISTERED_USER_UUID = UUID.randomUUID().toString();
    }

    private val endpoint = FollowingsEndPoint(FollowingsUseCase())

    @Test
    fun `add a following user`() {
        val hexagonalRequest = HexagonalRequest("""{
          "followerId": "$A_REGISTERED_USER_UUID",
          "followeeId": "$ANOTHER_REGISTERED_USER_UUID"
        }""")

        val hexagonalResponse = endpoint.hit(hexagonalRequest)

        assertEquals(201, hexagonalResponse.statusCode)
        assertEquals("text/plain", hexagonalResponse.contentType)
        assertEquals("Following created.", hexagonalResponse.responseBody)
    }

    @Test
    fun `add twice a following should return a 400 error`() {
        val hexagonalRequest = HexagonalRequest("""{
          "followerId": "$A_REGISTERED_USER_UUID",
          "followeeId": "$ANOTHER_REGISTERED_USER_UUID"
        }""")

        endpoint.hit(hexagonalRequest)
        val hexagonalResponse = endpoint.hit(hexagonalRequest)

        assertEquals(400, hexagonalResponse.statusCode)
        assertEquals("text/plain", hexagonalResponse.contentType)
        assertEquals("Following already exist.", hexagonalResponse.responseBody)
    }

    @Test
    fun `get followings of not registered user`() {
        val hexagonalRequest = HexagonalRequest("", mapOf("userId" to UUID.randomUUID().toString()), "GET")

        val hexagonalResponse = endpoint.hit(hexagonalRequest)

        assertEquals(200, hexagonalResponse.statusCode)
        assertEquals("application/json", hexagonalResponse.contentType)
        assertEquals("[]", hexagonalResponse.responseBody)
    }
}