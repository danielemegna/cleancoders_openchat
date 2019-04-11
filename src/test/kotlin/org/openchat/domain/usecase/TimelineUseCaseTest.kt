package org.openchat.domain.usecase

import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.junit.Assert.assertNull
import org.junit.Test
import org.mockito.Matchers
import org.mockito.Mockito.*
import org.openchat.domain.entity.Post
import org.openchat.domain.repository.PostRepository
import kotlin.test.assertNotNull


class TimelineUseCaseTest {

    private val postRepository = mock(PostRepository::class.java)
    private val useCase = TimelineUseCase(postRepository)

    @Test
    fun callStoreOnRepository() {
        val postToBePublished = Post("userId", "Post text")

        useCase.publish(postToBePublished)

        verify(postRepository).store(Matchers.argThat(object : BaseMatcher<Post>() {
            override fun matches(p0: Any?): Boolean {
                val actual = p0 as Post
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
    fun returnPostFilledWithIdAndDate() {
        `when`(postRepository.store(any())).thenReturn("postUUID")
        val toBePublished = Post("userId", "Post text")
        assertNull(toBePublished.id)
        assertNull(toBePublished.dateTime)

        val published = useCase.publish(toBePublished)

        assertNotNull(published.id)
        assertNotNull(published.dateTime)
    }
}