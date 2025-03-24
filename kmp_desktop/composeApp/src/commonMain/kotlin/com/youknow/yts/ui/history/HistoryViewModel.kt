package com.youknow.yts.ui.history

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youknow.yts.data.local.entity.SummaryEntity
import com.youknow.yts.data.local.getRoomDatabase
import com.youknow.yts.data.local.source.SummaryDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val summaryDao: SummaryDao = getRoomDatabase().getSummaryDao()
) : ViewModel() {

    var list: List<SummaryEntity> by mutableStateOf(emptyList())
        private set

    init {
        viewModelScope.launch(Dispatchers.IO) {
            list = summaryDao.getAll()
        }
    }

}