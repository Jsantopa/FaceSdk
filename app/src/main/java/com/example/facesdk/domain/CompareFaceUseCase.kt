package com.example.facesdk.domain

import android.graphics.Bitmap
import com.example.facesdk.data.FaceComparisonRepository

class CompareFacesUseCase(private val repository: FaceComparisonRepository) {
    operator fun invoke(first: Bitmap, second: Bitmap, onResult: (Int) -> Unit) {
        repository.compareFaces(first, second, onResult)
    }
}