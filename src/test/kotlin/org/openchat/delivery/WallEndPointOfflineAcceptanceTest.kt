package org.openchat.delivery

import com.eclipsesource.json.Json
import integration.APITestSuit
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.matchesPattern
import org.junit.Assert.assertEquals
import org.junit.Test
import org.openchat.delivery.endpoint.WallEndPoint
import org.openchat.delivery.repository.InMemoryFollowingsRepository
import org.openchat.delivery.repository.InMemoryPostRepository
import org.openchat.delivery.repository.InMemoryUserRepository
import org.openchat.domain.entity.Following
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
    private val followingRepository = InMemoryFollowingsRepository()

    private val endpoint = WallEndPoint(WallUseCase(
        postRepository, followingRepository
    ))

    @Test
    fun `get wall without posts`() {
        val hexagonalResponse = getAliceWall()

        assertEquals(200, hexagonalResponse.statusCode)
        assertEquals("application/json", hexagonalResponse.contentType)
        assertEquals("[]", hexagonalResponse.responseBody)
    }

    @Test
    fun `wall should show my posts`() {
        submitPost(aliceUUID, "Alice post 1")
        submitPost(aliceUUID, "Alice post 2")

        val hexagonalResponse = getAliceWall()

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

    @Test
    fun `wall should show followed user's posts`() {
        createFollowing(aliceUUID, lucyUUID)
        createFollowing(aliceUUID, danielUUID)
        submitPost(danielUUID, "Daniel post 1")
        submitPost(lucyUUID, "Lucy post 1")
        submitPost(danielUUID, "Daniel post 2")

        val hexagonalResponse = getAliceWall()

        assertEquals(200, hexagonalResponse.statusCode)
        assertEquals("application/json", hexagonalResponse.contentType)
        Json.parse(hexagonalResponse.responseBody).asArray().let {
            assertEquals(3, it.size())
            assertEquals("Daniel post 2", it[0].asObject().getString("text", ""))
            assertEquals("Lucy post 1", it[1].asObject().getString("text", ""))
            assertEquals("Daniel post 1", it[2].asObject().getString("text", ""))
        }
    }

    @Test
    fun `wall should show my posts and followed user's ones`() {
        createFollowing(aliceUUID, lucyUUID)
        createFollowing(aliceUUID, danielUUID)
        submitPost(danielUUID, "Daniel post 1")
        submitPost(aliceUUID, "Alice post 1")
        submitPost(lucyUUID, "Lucy post 1")
        submitPost(carlUUID, "Carl post 1")
        submitPost(aliceUUID, "Alice post 2")
        submitPost(carlUUID, "Carl post 2")
        submitPost(danielUUID, "Daniel post 2")

        val hexagonalResponse = getAliceWall()

        assertEquals(200, hexagonalResponse.statusCode)
        assertEquals("application/json", hexagonalResponse.contentType)
        Json.parse(hexagonalResponse.responseBody).asArray().let {
            assertEquals(5, it.size())
            assertEquals("Daniel post 2", it[0].asObject().getString("text", ""))
            assertEquals("Alice post 2", it[1].asObject().getString("text", ""))
            assertEquals("Lucy post 1", it[2].asObject().getString("text", ""))
            assertEquals("Alice post 1", it[3].asObject().getString("text", ""))
            assertEquals("Daniel post 1", it[4].asObject().getString("text", ""))
        }
    }

    private fun getAliceWall(): HexagonalResponse {
        val hexagonalRequest = HexagonalRequest("", mapOf(":userid" to aliceUUID), "GET")
        return endpoint.hit(hexagonalRequest)
    }

    private fun createFollowing(followerId: String, followedId: String) {
        followingRepository.store(Following(followerId, followedId))
    }

    private fun submitPost(userId: String, text: String) {
        postRepository.store(Post(userId, text))
        Thread.sleep(42)
    }
}