package org.openchat.delivery

import com.eclipsesource.json.Json
import integration.APITestSuit
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.matchesPattern
import org.junit.Before
import org.junit.Test
import org.openchat.delivery.repository.InMemoryUserRepository
import org.openchat.domain.entity.User
import org.openchat.domain.usecase.LoginUseCase
import kotlin.test.assertEquals

class LoginEndPointOfflineAcceptanceTest {

    private val userRepository = InMemoryUserRepository()
    private val endPoint = LoginEndPoint(LoginUseCase(userRepository))

    @Before
    fun setUp() {
        userRepository.add(User("Antony", "dj48sh", "About Antony"))
    }

    @Test
    fun `login with valid credentials`() {
        val hexagonalRequest = HexagonalRequest("""{
          "username": "Antony",
          "password": "dj48sh"
        }""")

        val hexagonalResponse = endPoint.hit(hexagonalRequest)

        assertEquals(200, hexagonalResponse.statusCode)
        assertEquals("application/json", hexagonalResponse.contentType)
        Json.parse(hexagonalResponse.responseBody).asObject().let {
            assertThat(it.getString("id", ""), matchesPattern(APITestSuit.UUID_PATTERN))
            assertEquals("Antony", it.getString("username", ""))
            assertEquals("About Antony", it.getString("about", ""))
        }
    }
}