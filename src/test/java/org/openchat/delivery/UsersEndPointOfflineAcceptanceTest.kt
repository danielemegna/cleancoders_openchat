package org.openchat.delivery

import com.eclipsesource.json.Json
import integration.APITestSuit
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.matchesPattern
import org.junit.Test
import org.openchat.delivery.endpoint.UsersEndPoint
import org.openchat.delivery.repository.InMemoryUserRepository
import org.openchat.domain.entity.User
import org.openchat.domain.usecase.UserUseCase
import kotlin.test.assertEquals

class UsersEndPointOfflineAcceptanceTest {

    private val userRepository = InMemoryUserRepository()
    private val usersEndPoint = UsersEndPoint(UserUseCase(userRepository))

    @Test
    fun `register new user`() {
        val hexagonalRequest = HexagonalRequest("""{
          "username": "Lucy",
          "password": "alki324d",
          "about": "About Lucy"
        }""")

        val hexagonalResponse = usersEndPoint.hit(hexagonalRequest)

        assertEquals(201, hexagonalResponse.statusCode)
        assertEquals("application/json", hexagonalResponse.contentType)
        Json.parse(hexagonalResponse.responseBody).asObject().let {
            assertThat(it.getString("id", ""), matchesPattern(APITestSuit.UUID_PATTERN))
            assertEquals("Lucy", it.getString("username", ""))
            assertEquals("About Lucy", it.getString("about", ""))
        }
    }

    @Test
    fun `register already present user`() {
        userRepository.add(User("Lucy", "any", "any"))
        val hexagonalRequest = HexagonalRequest("""{
          "username": "Lucy",
          "password": "any",
          "about": "any"
        }""")

        val hexagonalResponse = usersEndPoint.hit(hexagonalRequest)

        assertEquals(400, hexagonalResponse.statusCode)
        assertEquals("text/plain", hexagonalResponse.contentType)
        assertEquals("Username already in use.", hexagonalResponse.responseBody)
    }

    @Test
    fun `get users with no users registered`() {
        val hexagonalRequest = HexagonalRequest("", "GET")

        val hexagonalResponse = usersEndPoint.hit(hexagonalRequest)

        assertEquals(200, hexagonalResponse.statusCode)
        assertEquals("application/json", hexagonalResponse.contentType)
        assertEquals("[]", hexagonalResponse.responseBody)
    }

    @Test
    fun `get registered users`() {
        userRepository.add(User("Lucy", "any", "About Lucy"))
        userRepository.add(User("Carl", "any", "About Carl"))
        val hexagonalRequest = HexagonalRequest("", "GET")

        val hexagonalResponse = usersEndPoint.hit(hexagonalRequest)

        assertEquals(200, hexagonalResponse.statusCode)
        assertEquals("application/json", hexagonalResponse.contentType)
        Json.parse(hexagonalResponse.responseBody).asArray().let {
            assertEquals(2, it.size())
            val firstUser = it[0].asObject()
            assertThat(firstUser.getString("id", ""), matchesPattern(APITestSuit.UUID_PATTERN))
            assertEquals("Lucy", firstUser.getString("username", ""))
            val secondUser = it[1].asObject()
            assertThat(secondUser.getString("id", ""), matchesPattern(APITestSuit.UUID_PATTERN))
            assertEquals("About Carl", secondUser.getString("about", ""))
        }
    }
}