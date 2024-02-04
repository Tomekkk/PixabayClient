package com.tcode.pixabayclient.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tcode.pixabayclient.R
import com.tcode.pixabayclient.domain.PreviousQuery
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarWithHistory(
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit,
    onQueryChanged: (String) -> Unit,
    queryStream: StateFlow<String>,
    history: StateFlow<List<PreviousQuery>>,
) {
    var active by remember { mutableStateOf(false) }
    val historyList = history.collectAsState()
    val query = queryStream.collectAsState().value

    DockedSearchBar(
        query = query,
        modifier = modifier,
        onQueryChange = onQueryChanged,
        onSearch = {
            onSearch(query)
            active = false
        },
        active = active,
        onActiveChange = {
            active = it
        },
        placeholder = {
            Text(text = stringResource(id = R.string.search))
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(id = R.string.search_icon),
            )
        },
        trailingIcon = {
            if (active || query.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription =
                        if (query.isNotEmpty()) {
                            stringResource(id = R.string.clear)
                        } else {
                            stringResource(
                                id = R.string.close,
                            )
                        },
                    modifier =
                        Modifier.clickable {
                            if (query.isNotEmpty()) {
                                onQueryChanged("")
                            } else {
                                active = false
                            }
                        },
                )
            }
        },
    ) {
        LazyColumn(content = {
            historyList.value.forEach {
                item {
                    HistoryItem(historicalQuery = it.query) {
                        onQueryChanged(it)
                        onSearch(it)
                        active = false
                    }
                }
            }
        })
    }
}

@Composable
fun HistoryItem(
    historicalQuery: String,
    onQuerySelected: (String) -> Unit,
) {
    Row(
        modifier =
            Modifier.padding(all = 16.dp).fillMaxWidth().clickable {
                onQuerySelected(historicalQuery)
            },
    ) {
        Icon(
            imageVector = Icons.Default.History,
            contentDescription = "History",
            modifier = Modifier.padding(end = 8.dp),
        )
        Text(text = historicalQuery)
    }
}
