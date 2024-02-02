package com.tcode.pixabayclient.ui.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.navigation.compose.hiltViewModel
import com.tcode.pixabayclient.R
import com.tcode.pixabayclient.ui.SearchViewModel
import kotlinx.coroutines.flow.StateFlow

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    onImageClick: (Long) -> Unit,
) {
    Scaffold(topBar = {
        SearchTopBar(
            onSearch = viewModel::onSearch,
            onQueryChanged = viewModel::onQueryChanged,
            query = viewModel.query,
        )
    }) { padding ->
        SearchResults(viewModel.images, Modifier.padding(padding), onImageClick)
    }
}

@Composable
fun SearchTopBar(
    onSearch: (String) -> Unit,
    onQueryChanged: (String) -> Unit,
    query: StateFlow<String>,
) {
    val text = query.collectAsState().value
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    TextField(
        value = text,
        onValueChange = { onQueryChanged(it) },
        label = { Text(stringResource(R.string.search)) },
        leadingIcon = {
            Icon(
                Icons.Filled.Search,
                contentDescription = stringResource(R.string.search_icon),
            )
        },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions =
            KeyboardActions(onSearch = {
                onSearch(text.trim())
                // Hide the keyboard after submitting the search
                keyboardController?.hide()
                // or hide keyboard
                focusManager.clearFocus()
            }),
    )
}
