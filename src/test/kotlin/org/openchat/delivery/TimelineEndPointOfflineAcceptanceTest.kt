package org.openchat.delivery

import com.eclipsesource.json.Json
import integration.APITestSuit
import org.hamcrest.Matchers.matchesPattern
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Test
import org.openchat.delivery.endpoint.TimelineEndPoint
import org.openchat.delivery.repository.InMemoryPostRepository
import org.openchat.delivery.repository.InMemoryUserRepository
import org.openchat.domain.entity.User
import org.openchat.domain.usecase.TimelineUseCase

class TimelineEndPointOfflineAcceptanceTest {

    private val userRepository = InMemoryUserRepository()
    private val aliceUUID = userRepository.add(User("Alice", "anyPassword", "About Alice"))

    private val endPoint = TimelineEndPoint(TimelineUseCase(InMemoryPostRepository()))

    @Test
    fun `submit new post`() {
        val hexagonalRequest = HexagonalRequest(
            """{ "text": "Hello, I'm Alice" }""",
            mapOf(":userid" to aliceUUID),
            "POST"
        )

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

    @Test
    fun `get empty timeline`() {
        val hexagonalRequest = HexagonalRequest("", mapOf(":userid" to aliceUUID), "GET")

        val hexagonalResponse = endPoint.hit(hexagonalRequest)

        assertEquals(200, hexagonalResponse.statusCode)
        assertEquals("application/json", hexagonalResponse.contentType)
        assertEquals("[]", hexagonalResponse.responseBody)
    }

    @Test
    fun `submit posts and get timeline in reverse chronological order`() {
        endPoint.hit(HexagonalRequest("""{ "text": "Alice first post" }""", mapOf(":userid" to aliceUUID), "POST"))
        Thread.sleep(42)
        endPoint.hit(HexagonalRequest("""{ "text": "Alice second post" }""", mapOf(":userid" to aliceUUID), "POST"))

        val getTimelineRequest = HexagonalRequest("", mapOf(":userid" to aliceUUID), "GET")
        val hexagonalResponse = endPoint.hit(getTimelineRequest)

        assertEquals(200, hexagonalResponse.statusCode)
        assertEquals("application/json", hexagonalResponse.contentType)
        Json.parse(hexagonalResponse.responseBody).asArray().let {
            assertEquals(2, it.size())
            val first = it[0].asObject()
            assertThat(first.getString("postId", ""), matchesPattern(APITestSuit.UUID_PATTERN))
            assertEquals(aliceUUID, first.getString("userId", ""))
            assertEquals("Alice second post", first.getString("text", ""))
            assertThat(first.getString("dateTime", ""), matchesPattern(APITestSuit.DATE_PATTERN))
            val second = it[1].asObject()
            assertThat(second.getString("postId", ""), matchesPattern(APITestSuit.UUID_PATTERN))
            assertEquals(aliceUUID, second.getString("userId", ""))
            assertEquals("Alice first post", second.getString("text", ""))
            assertThat(second.getString("dateTime", ""), matchesPattern(APITestSuit.DATE_PATTERN))
        }
    }

    @Test
    fun `remember published post ids`() {
        val submitPostResponse = endPoint.hit(HexagonalRequest(
            """{ "text": "Hello, I'm Alice" }""",
            mapOf(":userid" to aliceUUID),
            "POST"
        ))
        val newPostId = Json.parse(submitPostResponse.responseBody).asObject().getString("postId", "")

        val getTimelineRequest = HexagonalRequest("", mapOf(":userid" to aliceUUID), "GET")
        val getTimelineResponse = endPoint.hit(getTimelineRequest)

        val timelinePostId = Json.parse(getTimelineResponse.responseBody).asArray()[0].asObject().getString("postId", "")
        assertEquals(newPostId, timelinePostId)
    }

    @Test
    fun `cannot submit posts with inappropriate language`() {
        val hexagonalRequest = HexagonalRequest(
            """{ "text": "I love orange" }""",
            mapOf(":userid" to aliceUUID),
            "POST"
        )

        val hexagonalResponse = endPoint.hit(hexagonalRequest)

        assertEquals(400, hexagonalResponse.statusCode)
        assertEquals("text/plain", hexagonalResponse.contentType)
        assertEquals("Post contains inappropriate language.", hexagonalResponse.responseBody)
    }
}