package com.rontaxi.util

import android.webkit.URLUtil
import java.io.File


fun getFileNameFromUrl(fileUrl: String): String {

    return URLUtil.guessFileName(
        fileUrl,
        null,
        null
    )
}

fun getFileNameFromPath(filePath: String): String {

    val file = File(filePath)
    return file.name
}