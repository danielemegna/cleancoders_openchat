package org.openchat.delivery

import com.eclipsesource.json.Json
import integration.APITestSuit
import org.hamcrest.Matchers.matchesPattern
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Test
import org.openchat.delivery.endpoint.TimelineEndPoint
import org.openchat.delivery.repository.InMemoryUserRepository
import org.openchat.domain.entity.User

class TimelineEndPointOfflineAcceptanceTest {

    private val userRepository = InMemoryUserRepository()

    private val aliceUUID = userRepository.add(User("Alice", "anyPassword", "About Alice"))
    private val endPoint = TimelineEndPoint()

    @Test
    fun submitNewPost() {
        val hexagonalRequest = HexagonalRequest("""{
            "text": "Hello, I'm Alice"
        }""", mapOf(":userid" to aliceUUID), "POST")

        val hexagonalResponse = endPoint.hit(hexagonalRequest)

        assertEquals(201, hexagonalResponse.statusCode)
        assertEquals("application/json", hexagonalResponse.contentType)
        Json.parse(hexagonalResponse.responseBody).asObject().let {
            assertThat(it.getString("postId", ""), matchesPattern(APITestSuit.UUID_PATTERN))
            assertEquals(aliceUUID, it.getString("userId", ""))
            assertEquals("Hello, I'm Alice", it.getString("text", ""))
            assertThat(it.getString("dateTime", ""), matchesPattern(APITestSuit.DATE_PATTERN))
        }
    }
}