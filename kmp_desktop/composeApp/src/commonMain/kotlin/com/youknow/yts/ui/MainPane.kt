package com.youknow.yts.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MainPane(
    vm: MainViewModel = viewModel { MainViewModel() }
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = vm.youtubeUrl,
            onValueChange = vm::onInputUrl,
            modifier = Modifier.width(300.dp),
            label = {
                Text(text = "Youtube URL")
            },
            placeholder = {
                Text(text = "Input Youtube URL...")
            },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
        )

        Button(
            onClick = {

            }
        ) {
            Text("Run")
        }
    }
}