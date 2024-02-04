package com.tcode.pixabayclient.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.tcode.pixabayclient.R
import com.tcode.pixabayclient.domain.ImageResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
fun SearchResults(
    imagesStream: Flow<PagingData<ImageResult>>,
    modifier: Modifier = Modifier,
    onImageClick: (Long) -> Unit = {},
) {
    val images = imagesStream.collectAsLazyPagingItems()
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(160.dp),
        verticalItemSpacing = 4.dp,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        content = {
            items(
                images.itemCount,
                key = images.itemKey { it.id },
            ) { index ->
                ImageCell(image = images[index]!!, onImageClick = onImageClick)
            }
        },
        modifier = modifier,
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImageCell(
    image: ImageResult,
    onImageClick: (Long) -> Unit,
) {
    Box(modifier = Modifier.clickable { onImageClick(image.imageId) }) {
        GlideImage(
            model = image.previewURL,
            contentScale = ContentScale.None,
            contentDescription = stringResource(R.string.image_by, image.user),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .aspectRatio(image.aspectRatio),
        )

        Text(
            text = stringResource(R.string.image_by, image.user),
            maxLines = 1,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.inverseOnSurface,
            modifier =
                Modifier
                    .align(Alignment.TopStart)
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            0F to Color.Black.copy(alpha = 0.8F),
                            .5F to Color.Black.copy(alpha = 0.5F),
                            1F to Color.Transparent,
                        ),
                    )
                    .padding(start = 4.dp, end = 4.dp, bottom = 8.dp, top = 4.dp),
        )

        Text(
            text = image.tags,
            maxLines = 1,
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.End,
            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            0F to Color.Transparent,
                            .5F to Color.Black.copy(alpha = 0.5F),
                            1F to Color.Black.copy(alpha = 0.8F),
                        ),
                    )
                    .padding(start = 4.dp, end = 4.dp, bottom = 4.dp, top = 8.dp),
            color = MaterialTheme.colorScheme.inverseOnSurface,
        )
    }
}

@Preview
@Composable
fun SearchResultsPreview(
    @PreviewParameter(SearchResultsPreviewParamProvider::class) imagesStream: Flow<PagingData<ImageResult>>,
) {
    Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
        SearchResults(imagesStream)
    }
}

private class SearchResultsPreviewParamProvider :
    PreviewParameterProvider<Flow<PagingData<ImageResult>>> {
    override val values: Sequence<Flow<PagingData<ImageResult>>> =
        sequenceOf(
            flowOf(
                PagingData.from(
                    (0..10L).map {
                        ImageResult(
                            id = it,
                            imageId = it,
                            tags = "tag1, tag2",
                            previewURL = "",
                            aspectRatio = 16F / 9,
                            user = "user$it",
                        )
                    },
                ),
            ),
        )
}
