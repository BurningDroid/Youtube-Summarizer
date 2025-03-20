package com.youknow.yts

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import com.youknow.yts.ui.MainPane
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        MainPane()
    }
}