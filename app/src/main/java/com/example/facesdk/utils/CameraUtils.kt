package com.example.facesdk.utils

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object CameraUtils {
    fun createImageFileUri(context: Context): Uri {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val photoFile = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "IMG_$timeStamp.jpg")
        return FileProvider.getUriForFile(context, "${context.packageName}.provider", photoFile)
    }
}