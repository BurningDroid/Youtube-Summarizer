package com.youknow.yts.ui.history

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.youknow.yts.data.local.entity.SummaryEntity

@Composable
fun HistoryPane(
    vm: HistoryViewModel = viewModel { HistoryViewModel() },
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "History")
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = ""
                        )
                    }
                },
                contentColor = MaterialTheme.colors.onPrimary
            )
        }
    ) {
        val list: List<SummaryEntity> = vm.list
        LazyColumn {
            items(list) { item ->
                SummaryItem(
                    item = item
                )
            }
        }
    }
}

@Composable
private fun SummaryItem(
    item: SummaryEntity
) {
    println(item.thumbnailUrl)
    Row {
        AsyncImage(
            model = item.thumbnailUrl,
            contentDescription = null,
            modifier = Modifier.width(100.dp)
        )

        Text(
            text = item.title
        )
    }
}