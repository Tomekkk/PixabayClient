package com.tcode.pixabayclient

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.room.withTransaction
import com.tcode.pixabayclient.data.db.ImageEntity
import com.tcode.pixabayclient.data.db.ImagesDatabase
import com.tcode.pixabayclient.data.db.RemoteKey
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import javax.inject.Inject

@HiltAndroidTest
class MainActivityTest {
    private val hiltRule = HiltAndroidRule(this)
    private val composableRule = createAndroidComposeRule<MainActivity>()

    @get:Rule
    val rule: RuleChain = RuleChain.outerRule(hiltRule).around(composableRule)

    @Inject
    lateinit var imagesDatabase: ImagesDatabase

    @Before
    fun setUp() {
        hiltRule.inject()
        runTest {
            imagesDatabase.withTransaction {
                imagesDatabase.getImagesDao().insert(
                    listOf(
                        ImageEntity(
                            imageId = 1L,
                            tags = "tag1",
                            previewURL = "url",
                            previewWidth = 16,
                            previewHeight = 9,
                            largeImageURL = "",
                            imageWidth = 16,
                            imageHeight = 9,
                            downloads = 1,
                            likes = 1,
                            comments = 1,
                            user = "Tom",
                            query = "fruits",
                        ),
                    ),
                )
                imagesDatabase.getRemoteKeysDao().insertAll(
                    listOf(
                        RemoteKey(1, "fruits", null, 1, 1),
                    ),
                )
            }
        }
    }

    @After
    fun tearDown() {
        runTest {
            imagesDatabase.clearAllTables()
        }
    }

    @Test
    fun when_tapping_on_results_item_should_navigate_to_detail_screen() {
        // Given
        with(composableRule.onNodeWithText("tag1")) {
            assertExists()
            // When
            performClick()
        }

        // Then
        composableRule.onNodeWithTag("DetailsScreenTag").assertExists()
    }
}
