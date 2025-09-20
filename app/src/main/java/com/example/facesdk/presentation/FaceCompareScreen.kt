package com.example.facesdk.presentation

import android.graphics.Bitmap
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.facesdk.domain.CompareFacesUseCase
import com.example.facesdk.data.FaceComparisonRepository
import android.content.Intent
import android.provider.MediaStore
import android.content.ContentValues
import android.net.Uri
import android.os.Environment
import com.example.facesdk.utils.CameraUtils

@Composable
fun FaceCompareScreen() {
    val context = LocalContext.current
    val repository = FaceComparisonRepository(context)
    val compareFacesUseCase = CompareFacesUseCase(repository)

    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var galleryBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var resultText by remember { mutableStateOf("Resultado: -") }
    var currentPhotoUri by remember { mutableStateOf<Uri?>(null)}

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            currentPhotoUri?.let { uri ->
                val inputStream = context.contentResolver.openInputStream(uri)
                capturedBitmap = android.graphics.BitmapFactory.decodeStream(inputStream)
            }
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            try {
                val photoUri = CameraUtils.createImageFileUri(context)
                currentPhotoUri = photoUri
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                cameraLauncher.launch(intent)

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Error al crear archivo de foto", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val inputStream = context.contentResolver.openInputStream(it)
            galleryBitmap = android.graphics.BitmapFactory.decodeStream(inputStream)
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("FaceSdk Demo", fontSize = 22.sp)

        Spacer(Modifier.height(16.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            BitmapImageBox(capturedBitmap, "No hay captura", Modifier.weight(1f).height(180.dp))
            Spacer(Modifier.width(12.dp))
            BitmapImageBox(galleryBitmap, "No hay imagen de galería", Modifier.weight(1f).height(180.dp))
        }

        Spacer(Modifier.height(16.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = { cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA) }) {
                Text("Capturar")
            }
            Button(onClick = { galleryLauncher.launch("image/*") }) {
                Text("Galería")
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            if (capturedBitmap != null && galleryBitmap != null) {
                compareFacesUseCase(capturedBitmap!!, galleryBitmap!!) { similarity ->
                    resultText = "Similitud: $similarity%"
                }
            } else {
                resultText = "Necesitas ambas imágenes."
            }
        }) {
            Text("Comparar")
        }

        Spacer(Modifier.height(16.dp))
        Text(resultText, fontSize = 18.sp)

        Spacer(Modifier.height(16.dp))
        Button(onClick = {
            capturedBitmap = null
            galleryBitmap = null
            resultText = "Resultado: -"
        }) {
            Text("Reiniciar")
        }
    }
}

@Composable
fun BitmapImageBox(bitmap: Bitmap?, placeholderText: String, modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        if (bitmap != null) {
            Image(bitmap = bitmap.asImageBitmap(), contentDescription = placeholderText, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
        } else {
            Text(placeholderText)
        }
    }
}
