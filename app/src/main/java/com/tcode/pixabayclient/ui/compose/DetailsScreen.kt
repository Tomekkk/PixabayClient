package com.tcode.pixabayclient.ui.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.tcode.pixabayclient.R
import com.tcode.pixabayclient.ui.DetailsViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun DetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: DetailsViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
) {
    val imageDetails = viewModel.imageDetails.collectAsState(initial = null).value
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                title = {
                    Row {
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier.align(Alignment.CenterVertically),
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.navigate_back),
                            )
                        }
                        Text(
                            text = stringResource(R.string.image_by, imageDetails?.user ?: ""),
                            style = MaterialTheme.typography.titleLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.align(Alignment.CenterVertically),
                        )
                    }
                },
            )
        },
    ) { padding ->
        imageDetails?.let { details ->
            Column(
                modifier =
                    modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .verticalScroll(rememberScrollState())
                        .padding(padding),
            ) {
                GlideImage(
                    model = details.largeImageURL,
                    contentScale = ContentScale.FillWidth,
                    contentDescription = stringResource(R.string.image_by, details.user),
                    modifier =
                        modifier.wrapContentHeight().aspectRatio(details.aspectRatio),
                )

                Column(modifier = Modifier.padding(16.dp)) {
                    PixabayFooter(modifier = Modifier.fillMaxWidth())

                    Text(
                        text = stringResource(R.string.image_by, details.user),
                        style = MaterialTheme.typography.labelLarge,
                    )

                    Text(
                        text = stringResource(R.string.tags, details.tags),
                        style = MaterialTheme.typography.labelMedium,
                    )

                    Text(
                        text = stringResource(R.string.likes, details.likes),
                        style = MaterialTheme.typography.labelMedium,
                    )

                    Text(
                        text = stringResource(R.string.downloads, details.downloads),
                        style = MaterialTheme.typography.labelMedium,
                    )

                    Text(
                        text = stringResource(R.string.comments, details.comments),
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
            }
        }
    }
}
