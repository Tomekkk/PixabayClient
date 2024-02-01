package com.tcode.pixabayclient.data

import androidx.paging.testing.asSnapshot
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DefaultImagesRepositoryTest {
    @Test
    fun `when getImagesStream invoked should call service with given query and first page params`() =
        runTest {
            // given
            val imagesDataSource = mockk<ImagesDataSource>(relaxed = true)
            val uniqueIdProvider = mockk<UniqueIdProvider>(relaxed = true)
            val objectUnderTest = DefaultImagesRepository(imagesDataSource, uniqueIdProvider)
            // when
            objectUnderTest.getImagesStream("query").asSnapshot()
            // then
            coVerify { imagesDataSource.searchImages("query", 1) }
        }
}
