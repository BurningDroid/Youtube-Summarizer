package com.youknow.yts.service

import java.io.File

object FileManager {
    fun clear() {
        delete("temp.txt")
        delete("temp.wav")
    }

    private fun delete(fileName: String) {
        val outputFile = File(fileName)
        if (outputFile.exists()) {
            outputFile.delete()
        }
    }
}