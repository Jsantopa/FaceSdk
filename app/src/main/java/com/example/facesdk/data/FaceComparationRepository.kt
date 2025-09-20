package com.example.facesdk.data

import android.content.Context
import android.graphics.Bitmap
import com.regula.facesdk.FaceSDK
import com.regula.facesdk.enums.ImageType
import com.regula.facesdk.model.MatchFacesImage
import com.regula.facesdk.request.MatchFacesRequest


class FaceComparisonRepository(private val context: Context) {
    fun compareFaces(first: Bitmap, second: Bitmap, onResult: (Int) -> Unit) {
        val firstImage = MatchFacesImage(first, ImageType.LIVE)
        val secondImage = MatchFacesImage(second, ImageType.LIVE)
        val request = MatchFacesRequest(listOf(firstImage, secondImage))

        FaceSDK.Instance().matchFaces(context, request) { response ->
            val similarityFloat = response?.results?.getOrNull(0)?.similarity?.toFloat() ?: 0f
            val similarityPercent = (similarityFloat * 100).toInt()
            onResult(similarityPercent)
        }
    }
}