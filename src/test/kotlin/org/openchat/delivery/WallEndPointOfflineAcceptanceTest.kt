package org.openchat.delivery

import com.eclipsesource.json.Json
import integration.APITestSuit
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.matchesPattern
import org.junit.Assert.assertEquals
import org.junit.Test
import org.openchat.delivery.endpoint.WallEndPoint
import org.openchat.delivery.repository.InMemoryPostRepository
import org.openchat.delivery.repository.InMemoryUserRepository
import org.openchat.domain.entity.Post
import org.openchat.domain.entity.User
import org.openchat.domain.usecase.WallUseCase

class WallEndPointOfflineAcceptanceTest {

    private val userRepository = InMemoryUserRepository()
    private val aliceUUID = userRepository.add(User("Alice", "anyPassword", "About Alice"))
    private val lucyUUID = userRepository.add(User("Lucy", "anyPassword", "About Lucy"))
    private val carlUUID = userRepository.add(User("Carl", "anyPassword", "About Carl"))
    private val danielUUID = userRepository.add(User("Daniel", "anyPassword", "About Daniel"))

    private val postRepository = InMemoryPostRepository()

    private val endpoint = WallEndPoint(WallUseCase(postRepository))

    @Test
    fun `get wall without posts`() {
        val hexagonalRequest = HexagonalRequest("", mapOf(":userid" to aliceUUID), "GET")

        val hexagonalResponse = endpoint.hit(hexagonalRequest)

        assertEquals(200, hexagonalResponse.statusCode)
        assertEquals("application/json", hexagonalResponse.contentType)
        assertEquals("[]", hexagonalResponse.responseBody)
    }

    @Test
    fun `wall should show my posts`() {
        postRepository.store(Post(aliceUUID, "Alice post 1"))
        postRepository.store(Post(aliceUUID, "Alice post 2"))
        val hexagonalRequest = HexagonalRequest("", mapOf(":userid" to aliceUUID), "GET")

        val hexagonalResponse = endpoint.hit(hexagonalRequest)

        assertEquals(200, hexagonalResponse.statusCode)
        assertEquals("application/json", hexagonalResponse.contentType)
        Json.parse(hexagonalResponse.responseBody).asArray().let {
            assertEquals(2, it.size())
            val first = it[0].asObject()
            assertThat(first.getString("postId", ""), matchesPattern(APITestSuit.UUID_PATTERN))
            assertEquals(aliceUUID, first.getString("userId", ""))
            assertEquals("Alice post 2", first.getString("text", ""))
            assertThat(first.getString("dateTime", ""), matchesPattern(APITestSuit.DATE_PATTERN))
            val second = it[1].asObject()
            assertEquals("Alice post 1", second.getString("text", ""))
        }
    }
}