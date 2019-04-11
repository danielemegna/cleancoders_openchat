package org.openchat.delivery

import com.eclipsesource.json.Json
import org.junit.Test
import org.openchat.delivery.endpoint.FollowingsEndPoint
import org.openchat.delivery.repository.InMemoryFollowingsRepository
import org.openchat.delivery.repository.InMemoryUserRepository
import org.openchat.domain.entity.Following
import org.openchat.domain.entity.User
import org.openchat.domain.usecase.FollowingsUseCase
import java.util.*
import kotlin.test.assertEquals

class FollowingsEndPointOfflineAcceptanceTest {

    private val userRepository = InMemoryUserRepository()
    private val followingsRepository = InMemoryFollowingsRepository()
    private val endpoint = FollowingsEndPoint(FollowingsUseCase(followingsRepository, userRepository))

    private val lucyUUID = userRepository.add(User("Lucy", "anyPassword", "About Lucy"))
    private val carlUUID = userRepository.add(User("Carl", "anyPassword", "About Carl"))
    private val danielUUID = userRepository.add(User("Daniel", "anyPassword", "About Daniel"))


    @Test
    fun `add a following user`() {
        val hexagonalRequest = HexagonalRequest("""{
          "followerId": "$lucyUUID",
          "followeeId": "$carlUUID"
        }""")

        val hexagonalResponse = endpoint.hit(hexagonalRequest)

        assertEquals(201, hexagonalResponse.statusCode)
        assertEquals("text/plain", hexagonalResponse.contentType)
        assertEquals("Following created.", hexagonalResponse.responseBody)
    }

    @Test
    fun `add twice a following should return a 400 error`() {
        val hexagonalRequest = HexagonalRequest("""{
          "followerId": "$lucyUUID",
          "followeeId": "$carlUUID"
        }""")

        endpoint.hit(hexagonalRequest)
        val hexagonalResponse = endpoint.hit(hexagonalRequest)

        assertEquals(400, hexagonalResponse.statusCode)
        assertEquals("text/plain", hexagonalResponse.contentType)
        assertEquals("Following already exist.", hexagonalResponse.responseBody)
    }

    @Test
    fun `get followings of not registered user`() {
        val hexagonalRequest = HexagonalRequest("", mapOf(":userid" to UUID.randomUUID().toString()), "GET")

        val hexagonalResponse = endpoint.hit(hexagonalRequest)

        assertEquals(200, hexagonalResponse.statusCode)
        assertEquals("application/json", hexagonalResponse.contentType)
        assertEquals("[]", hexagonalResponse.responseBody)
    }

    @Test
    fun `get followings of user`() {
        followingsRepository.store(Following(lucyUUID, carlUUID))
        followingsRepository.store(Following(lucyUUID, danielUUID))
        val hexagonalRequest = HexagonalRequest("", mapOf(":userid" to lucyUUID), "GET")

        val hexagonalResponse = endpoint.hit(hexagonalRequest)

        assertEquals(200, hexagonalResponse.statusCode)
        assertEquals("application/json", hexagonalResponse.contentType)
        Json.parse(hexagonalResponse.responseBody).asArray().let {
            assertEquals(2, it.size())
            val firstUser = it[0].asObject()
            assertEquals(carlUUID, firstUser.getString("id", ""))
            assertEquals("Carl", firstUser.getString("username", ""))
            assertEquals("About Carl", firstUser.getString("about", ""))
            val secondUser = it[1].asObject()
            assertEquals(danielUUID, secondUser.getString("id", ""))
            assertEquals("Daniel", secondUser.getString("username", ""))
            assertEquals("About Daniel", secondUser.getString("about", ""))
        }
    }
}