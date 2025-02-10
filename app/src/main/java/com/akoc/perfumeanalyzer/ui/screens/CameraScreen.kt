package com.akoc.perfumeanalyzer.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.akoc.perfumeanalyzer.R
import com.akoc.perfumeanalyzer.ui.theme.GlassWhite
import com.akoc.perfumeanalyzer.ui.theme.MysticGold
import java.io.File
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

@Composable
fun CameraScreen(
    onImageCaptured: (Uri) -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    var hasStoragePermission by remember {
        mutableStateOf(
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_MEDIA_IMAGES
                ) == PackageManager.PERMISSION_GRANTED
            } else {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            }
        )
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        hasCameraPermission = isGranted
    }

    val storagePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        hasStoragePermission = isGranted
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onImageCaptured(it) }
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
        if (!hasStoragePermission) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                storagePermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
            } else {
                storagePermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    if (hasCameraPermission) {
        var imageCapture: ImageCapture? by remember { mutableStateOf(null) }
        var preview: Preview? by remember { mutableStateOf(null) }
        var camera: ProcessCameraProvider? by remember { mutableStateOf(null) }
        var previewView: PreviewView? by remember { mutableStateOf(null) }

        LaunchedEffect(previewView) {
            if (previewView == null) return@LaunchedEffect
            
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            cameraProviderFuture.addListener({
                try {
                    val cameraProvider = cameraProviderFuture.get()
                    preview = Preview.Builder().build()
                    imageCapture = ImageCapture.Builder()
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .build()

                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                    cameraProvider.unbindAll()
                    camera = cameraProvider
                    camera?.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageCapture
                    )

                    preview?.setSurfaceProvider(previewView!!.surfaceProvider)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, ContextCompat.getMainExecutor(context))
        }

        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(
                factory = { ctx ->
                    PreviewView(ctx).apply {
                        this.scaleType = PreviewView.ScaleType.FILL_CENTER
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        previewView = this
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            // Karanlık overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF0A0A14).copy(alpha = 0.7f))
            )

            // Partiküller için state'ler
            val particles = remember { List(20) { Offset(Random.nextFloat() * 1000, Random.nextFloat() * 2000) } }
            val particleOffsets = particles.map { initialOffset ->
                remember { Animatable(initialOffset, Offset.VectorConverter) }
            }

            // Partiküller animasyonu
            LaunchedEffect(Unit) {
                particleOffsets.forEach { offset ->
                    offset.animateTo(
                        targetValue = Offset(
                            Random.nextFloat() * 1000,
                            Random.nextFloat() * 2000
                        ),
                        animationSpec = infiniteRepeatable(
                            animation = tween(
                                durationMillis = (3000..8000).random(),
                                easing = LinearEasing
                            ),
                            repeatMode = RepeatMode.Reverse
                        )
                    )
                }
            }

            // Partiküller
            val density = LocalDensity.current
            val particleRadius = with(density) { (2..5).random().dp.toPx() }
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                particleOffsets.forEach { offset ->
                    drawCircle(
                        color = MysticGold.copy(alpha = 0.2f),
                        radius = particleRadius,
                        center = offset.value
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Üst kısım
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF2C1810).copy(alpha = 0.8f),
                                    Color.Transparent
                                )
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(16.dp)
                ) {
                    Column {
                        IconButton(
                            onClick = onNavigateBack,
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Geri",
                                tint = GlassWhite
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Parfüm şişesi ikonu ve metin
                Icon(
                    painter = painterResource(id = R.drawable.perfume_outline),
                    contentDescription = null,
                    tint = MysticGold.copy(alpha = 0.8f),
                    modifier = Modifier
                        .size(120.dp)
                        .padding(bottom = 24.dp)
                )

                Text(
                    text = "Parfüm şişesini ortalayın",
                    color = MysticGold,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Net ve yakın bir fotoğraf çekin",
                    color = GlassWhite.copy(alpha = 0.7f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                // Kamera ve galeri butonları
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Galeri butonu
                    Button(
                        onClick = {
                            if (hasStoragePermission) {
                                galleryLauncher.launch("image/*")
                            } else {
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                                    storagePermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                                } else {
                                    storagePermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1A1A2E)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                            .padding(end = 8.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = "Galeriden Seç",
                                tint = GlassWhite
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Galeriden Seç",
                                color = GlassWhite,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    // Kamera butonu
                    Button(
                        onClick = {
                            val photoFile = File(
                                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                                "perfume_${System.currentTimeMillis()}.jpg"
                            )

                            val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

                            imageCapture?.takePicture(
                                outputOptions,
                                ContextCompat.getMainExecutor(context),
                                object : ImageCapture.OnImageSavedCallback {
                                    override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                                        try {
                                            val savedUri = FileProvider.getUriForFile(
                                                context,
                                                "${context.packageName}.provider",
                                                photoFile
                                            )
                                            onImageCaptured(savedUri)
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                            Toast.makeText(
                                                context,
                                                "Fotoğraf kaydedilemedi: ${e.localizedMessage}",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    }

                                    override fun onError(exception: ImageCaptureException) {
                                        exception.printStackTrace()
                                    }
                                }
                            )
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MysticGold
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                            .padding(start = 8.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Camera,
                                contentDescription = "Fotoğraf Çek",
                                tint = Color.Black
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Fotoğraf Çek",
                                color = Color.Black,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
} 