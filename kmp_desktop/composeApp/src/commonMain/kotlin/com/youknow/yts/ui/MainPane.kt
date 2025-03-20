package com.youknow.yts.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MainPane(
    vm: MainViewModel = viewModel { MainViewModel() }
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
    ) {
        val uiState = vm.uiState

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
        ) {
            when (uiState) {
                is UiState.Processing -> LoadingUi(uiState.step)

                is UiState.Result -> ResultUi(uiState)

                UiState.Input -> {}
            }
        }

        InputUi(
            input = vm.youtubeUrl,
            uiState = uiState,
            onInput = vm::onInputUrl,
            onClickRun = vm::onClickRun
        )
    }
}

@Composable
private fun InputUi(
    input: String,
    uiState: UiState,
    onInput: (String) -> Unit,
    onClickRun: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = input,
            onValueChange = onInput,
            modifier = Modifier.width(300.dp),
            label = { Text(text = "Youtube URL") },
            placeholder = { Text(text = "Input Youtube URL...") },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
        )

        Button(
            onClick = onClickRun,
            enabled = uiState !is UiState.Processing,
        ) {
            Text("Run")
        }
    }
}

@Composable
private fun LoadingUi(step: ProcessStep) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
        ) {
            when(step) {
                ProcessStep.DOWNLOAD_VIDEO -> {
                    ProcessingItem(text = "영상을 다운로드 받고 있습니다...", done = false)
                }
                ProcessStep.STT -> {
                    ProcessingItem(text = "영상을 다운로드 받고 있습니다...", done = true)
                    ProcessingItem(text = "오디오를 텍스트로 변환 중 입니다...", done = false)
                }
                ProcessStep.SUMMARIZE -> {
                    ProcessingItem(text = "영상을 다운로드 받고 있습니다...", done = true)
                    ProcessingItem(text = "오디오 스크립트를 추출 중 입니다...", done = true)
                    ProcessingItem(text = "스크립트를 요약 정리하는 중 입니다...", done = false)
                }
            }
        }
    }
}

@Composable
private fun ProcessingItem(
    text: String,
    done: Boolean
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.h6
        )

        if (done) {
            Icon(
                imageVector = Icons.Default.Done,
                modifier = Modifier.size(24.dp),
                contentDescription = "",
                tint = Color(0xFF25720A)
            )
        } else {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun ResultUi(uiState: UiState.Result) {
    OutlinedTextField(
        value = uiState.result,
        onValueChange = {},
        modifier = Modifier.fillMaxSize().padding(16.dp),
        readOnly = true,
    )
}
