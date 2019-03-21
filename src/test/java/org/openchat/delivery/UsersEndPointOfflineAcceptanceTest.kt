package org.openchat.delivery

import com.eclipsesource.json.Json
import integration.APITestSuit
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.matchesPattern
import org.junit.Test
import kotlin.test.assertEquals

class UsersEndPointOfflineAcceptanceTest {

    private val usersEndPoint = UsersEndPoint()

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
        val hexagonalRequest = HexagonalRequest("""{
          "username": "Lucy",
          "password": "alki324d",
          "about": "About Lucy"
        }""")

        usersEndPoint.hit(hexagonalRequest)
        val hexagonalResponse = usersEndPoint.hit(hexagonalRequest)

        assertEquals(400, hexagonalResponse.statusCode)
        assertEquals("text/plain", hexagonalResponse.contentType)
        assertEquals("Username already in use.", hexagonalResponse.responseBody)
    }
}