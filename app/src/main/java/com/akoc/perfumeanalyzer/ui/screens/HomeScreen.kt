package com.akoc.perfumeanalyzer.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.akoc.perfumeanalyzer.R
import com.akoc.perfumeanalyzer.ui.theme.*
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    onNavigateToCamera: () -> Unit,
    onNavigateToHoroscope: () -> Unit
) {
    var showWelcome by remember { mutableStateOf(true) }
    var currentTipIndex by remember { mutableStateOf(0) }
    
    val tips = listOf(
        "Parfümünüzü iyi aydınlatılmış bir ortamda fotoğraflayın",
        "Şişenin etiketinin net görünmesine dikkat edin",
        "Parfüm şişesini yakından çekin",
        "Fotoğraf çekerken sabit durun",
        "Parfüm şişesini ortalayın"
    )

    // Animasyonlar
    val welcomeScale by animateFloatAsState(
        targetValue = if (showWelcome) 1.2f else 1f,
        animationSpec = tween(1000),
        label = "welcome"
    )

    val welcomeAlpha by animateFloatAsState(
        targetValue = if (showWelcome) 1f else 0f,
        animationSpec = tween(1000),
        label = "welcome_alpha"
    )

    val iconScale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "icon_scale"
    )

    val tipAlpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "tip_alpha"
    )

    // Yıldız efekti için
    class StarState {
        var scale = mutableStateOf(1f)
        var alpha = mutableStateOf(1f)
        val x = (0..100).random().toFloat()
        val y = (0..100).random().toFloat()
    }

    val stars = remember { List(20) { StarState() } }

    stars.forEachIndexed { index, star ->
        star.scale.value = animateFloatAsState(
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = (1000..3000).random(),
                    delayMillis = (0..1000).random()
                ),
                repeatMode = RepeatMode.Reverse
            ),
            label = "star_$index"
        ).value

        star.alpha.value = animateFloatAsState(
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = (1000..2000).random(),
                    delayMillis = (0..1000).random()
                ),
                repeatMode = RepeatMode.Reverse
            ),
            label = "star_alpha_$index"
        ).value
    }

    // İpuçlarını otomatik değiştir
    LaunchedEffect(Unit) {
        delay(2000)
        showWelcome = false
        
        while (true) {
            delay(4000)
            currentTipIndex = (currentTipIndex + 1) % tips.size
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        PurpleGradientStart,
                        PurpleGradientEnd,
                        ShadowBlack
                    )
                )
            )
    ) {
        // Yıldız efekti
        stars.forEach { star ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                Text(
                    text = "✧",
                    color = ShimmerGold.copy(alpha = star.alpha.value * 0.5f),
                    fontSize = 12.sp,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .offset(
                            x = (star.x * 3).dp,
                            y = (star.y * 7).dp
                        )
                        .scale(0.8f + (star.scale.value * 0.4f))
                )
            }
        }

        // Arkaplan deseni
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            MysticGold.copy(alpha = 0.05f),
                            Color.Transparent
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Hoş geldiniz mesajı
            if (showWelcome) {
                Text(
                    text = "⚜ Hoş Geldiniz ⚜",
                    color = ShimmerGold,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .scale(welcomeScale)
                        .alpha(welcomeAlpha)
                        .padding(top = 48.dp)
                )
            }

            // Logo ve ipucu
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .padding(vertical = 32.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    MysticGold.copy(alpha = 0.1f),
                                    Color.Transparent
                                )
                            ),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.perfume_outline),
                        contentDescription = "Parfüm",
                        tint = ShimmerGold,
                        modifier = Modifier
                            .size(200.dp)
                            .scale(0.9f + (iconScale * 0.1f))
                    )
                }

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = DeepPurple.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = tips[currentTipIndex],
                        color = LavenderMist,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .alpha(tipAlpha)
                            .padding(16.dp)
                    )
                }

                // Özellikler
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Feature(
                        icon = "🔍",
                        text = "Hızlı Analiz",
                        color = RosePink
                    )
                    Feature(
                        icon = "⭐",
                        text = "Doğru Sonuç",
                        color = ShimmerGold
                    )
                    Feature(
                        icon = "🎯",
                        text = "Kolay Kullanım",
                        color = LavenderMist
                    )
                }
            }

            // Butonlar
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                Button(
                    onClick = onNavigateToCamera,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ShimmerGold
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 8.dp
                    )
                ) {
                    Text(
                        text = "◉ Parfümünüzü Analiz Edin",
                        color = ShadowBlack,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onNavigateToHoroscope,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PalePurple
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 8.dp
                    )
                ) {
                    Text(
                        text = "✧ Parfüm Burcunuzu Öğrenin",
                        color = ShadowBlack,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun Feature(
    icon: String,
    text: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        Text(
            text = icon,
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = text,
            color = color,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
} 