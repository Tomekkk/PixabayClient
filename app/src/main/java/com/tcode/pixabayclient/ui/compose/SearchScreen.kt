package com.tcode.pixabayclient.ui.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tcode.pixabayclient.ui.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
    onImageClick: (Long) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        bottomBar = {
            PixabayFooter(
                modifier =
                    Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    containerColor = MaterialTheme.colorScheme.onErrorContainer,
                    contentColor = MaterialTheme.colorScheme.onError,
                    snackbarData = data,
                )
            }
        },
    ) { padding ->
        Box(modifier.padding(padding)) {
            SearchResults(
                modifier =
                    modifier.fillMaxSize(),
                imagesStream = viewModel.images,
                onImageClick = onImageClick,
                snackbarHostState = snackbarHostState,
                contentPadding = PaddingValues(top = SearchBarDefaults.InputFieldHeight + 16.dp),
            )

            SearchBarWithHistory(
                onSearch = viewModel::onSearch,
                onQueryChanged = viewModel::onQueryChanged,
                queryStream = viewModel.queryStream,
                history = viewModel.queriesHistoryStream,
                modifier = modifier.padding(8.dp),
            )
        }
    }
}
