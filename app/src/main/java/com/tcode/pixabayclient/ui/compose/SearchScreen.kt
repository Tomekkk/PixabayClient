package com.tcode.pixabayclient.ui.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tcode.pixabayclient.ui.SearchViewModel

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
    onImageClick: (Long) -> Unit,
) {
    Scaffold(
        bottomBar = {
            PixabayFooter(
                modifier =
                    Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
            )
        },
    ) { padding ->
        Box(modifier = modifier.padding(padding)) {
            SearchResults(viewModel.images, modifier.fillMaxSize(), onImageClick)
            SearchBarWithHistory(
                onSearch = viewModel::onSearch,
                onQueryChanged = viewModel::onQueryChanged,
                queryStream = viewModel.queryStream,
                history = viewModel.queriesHistoryStream,
                modifier = modifier.padding(16.dp),
            )
        }
    }
}
