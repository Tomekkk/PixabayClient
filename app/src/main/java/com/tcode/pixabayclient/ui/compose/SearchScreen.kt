package com.tcode.pixabayclient.ui.compose

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
        topBar = {
            SearchBarWithHistory(
                onSearch = viewModel::onSearch,
                onQueryChanged = viewModel::onQueryChanged,
                queryStream = viewModel.queryStream,
                history = viewModel.queriesHistoryStream,
                modifier = modifier.padding(8.dp),
            )
        },
        bottomBar = {
            PixabayFooter(
                modifier =
                    Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
            )
        },
    ) { padding ->
        SearchResults(
            viewModel.images,
            modifier
                .padding(padding)
                .fillMaxSize(),
            onImageClick,
        )
    }
}
