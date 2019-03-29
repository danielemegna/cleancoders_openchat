package org.openchat.delivery

import org.junit.Test
import org.openchat.delivery.endpoint.FollowingsEndPoint
import org.openchat.domain.usecase.FollowingsUseCase
import java.util.*
import kotlin.test.assertEquals

class FollowingsEndPointOfflineAcceptanceTest {

    private val endpoint = FollowingsEndPoint(FollowingsUseCase())

    @Test
    fun `add a following user`() {
        val hexagonalRequest = HexagonalRequest("""{
          "followerId": "${UUID.randomUUID()}",
          "followeeId": "${UUID.randomUUID()}"
        }""")

        val hexagonalResponse = endpoint.hit(hexagonalRequest)

        assertEquals(201, hexagonalResponse.statusCode)
        assertEquals("text/plain", hexagonalResponse.contentType)
        assertEquals("Following created.", hexagonalResponse.responseBody)
    }

    @Test
    fun `add twice a following should return a 400 error`() {
        val hexagonalRequest = HexagonalRequest("""{
          "followerId": "${UUID.randomUUID()}",
          "followeeId": "${UUID.randomUUID()}"
        }""")

        endpoint.hit(hexagonalRequest)
        val hexagonalResponse = endpoint.hit(hexagonalRequest)

        assertEquals(400, hexagonalResponse.statusCode)
        assertEquals("text/plain", hexagonalResponse.contentType)
        assertEquals("Following already exist.", hexagonalResponse.responseBody)
    }
}