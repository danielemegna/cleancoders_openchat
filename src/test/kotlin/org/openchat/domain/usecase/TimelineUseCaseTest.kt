package org.openchat.domain.usecase

import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.junit.Assert.assertNull
import org.junit.Test
import org.mockito.Matchers
import org.mockito.Mockito.*
import org.openchat.domain.entity.Post
import org.openchat.domain.repository.PostRepository
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


class TimelineUseCaseTest {

    private val postRepository = mock(PostRepository::class.java)
    private val useCase = TimelineUseCase(postRepository)

    @Test
    fun callStoreOnRepositorySettingDateTimeOnPublish() {
        useCase.publish(Post("userId", "Post text"))

        verify(postRepository).store(Matchers.argThat(object : BaseMatcher<Post>() {
            override fun matches(param: Any?): Boolean {
                val actual = param as Post
                return actual.userId == "userId" &&
                    actual.text == "Post text" &&
                    actual.dateTime != null
            }

            override fun describeTo(p0: Description?) {
                println("Post do not match!")
            }
        }))
    }

    @Test
    fun returnPostFilledWithIdAndDateOnPublish() {
        `when`(postRepository.store(any())).thenReturn("postUUID")
        val toBePublished = Post("userId", "Post text")
        assertNull(toBePublished.id)
        assertNull(toBePublished.dateTime)

        val published = useCase.publish(toBePublished)

        assertNotNull(published.id)
        assertNotNull(published.dateTime)
        assertEquals("Post text", published.text)
    }
}