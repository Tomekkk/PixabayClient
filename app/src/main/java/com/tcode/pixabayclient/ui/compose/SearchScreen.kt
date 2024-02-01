package com.tcode.pixabayclient.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.tcode.pixabayclient.data.ImageDto
import com.tcode.pixabayclient.ui.SearchViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
) {
    SearchResults(imagesStream = viewModel.images)
}

@Composable
fun SearchResults(imagesStream: Flow<PagingData<ImageDto>>) {
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
                ImageCell(image = images[index]!!)
            }
        },
        modifier = Modifier.fillMaxSize(),
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImageCell(image: ImageDto) {
    Column {
        GlideImage(
            model = image.previewURL,
            contentScale = ContentScale.Crop,
            contentDescription = image.tags,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
        )
        Text(
            text = image.user,
            textAlign = TextAlign.Center,
            maxLines = 1,
            style = MaterialTheme.typography.titleMedium,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .wrapContentWidth(Alignment.CenterHorizontally),
        )
    }
}

@Preview
@Composable
fun SearchResultsPreview(
    @PreviewParameter(SearchResultsPreviewParamProvider::class) imagesStream: Flow<PagingData<ImageDto>>,
) {
    Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
        SearchResults(imagesStream)
    }
}

private class SearchResultsPreviewParamProvider :
    PreviewParameterProvider<Flow<PagingData<ImageDto>>> {
    override val values: Sequence<Flow<PagingData<ImageDto>>> =
        sequenceOf(
            flowOf(
                PagingData.from(
                    (0..10L).map {
                        ImageDto(
                            id = it,
                            tags = "tags $it",
                            previewURL = "",
                            largeImageURL = "",
                            downloads = it,
                            likes = it,
                            comments = it,
                            userId = it,
                            user = "user$it",
                        )
                    },
                ),
            ),
        )
}
