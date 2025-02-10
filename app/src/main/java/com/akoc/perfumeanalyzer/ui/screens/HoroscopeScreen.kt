package com.akoc.perfumeanalyzer.ui.screens

import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.akoc.perfumeanalyzer.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun HoroscopeScreen(
    onNavigateBack: () -> Unit
) {
    var burçAdı by remember { mutableStateOf("") }
    var seçiliBurç by remember { mutableStateOf<String?>(null) }
    var analizSonucu by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    
    // Burç sembolleri
    val burçSembolleri = listOf("♈", "♉", "♊", "♋", "♌", "♍", "♎", "♏", "♐", "♑", "♒", "♓")
    var currentSymbolIndex by remember { mutableStateOf(0) }
    
    // Dönen burç animasyonu
    val rotation by animateFloatAsState(
        targetValue = currentSymbolIndex * 360f,
        animationSpec = tween(1000),
        label = "rotation"
    )

    // Burç sembollerini otomatik değiştir
    LaunchedEffect(Unit) {
        while (true) {
            delay(2000)
            currentSymbolIndex = (currentSymbolIndex + 1) % burçSembolleri.size
        }
    }

    // Burç eşleştirme fonksiyonu
    fun burçBul(ad: String): String? {
        val adLower = ad.lowercase()
        return when {
            adLower.contains("koç") -> "♈ Koç"
            adLower.contains("boğa") -> "♉ Boğa"
            adLower.contains("ikiz") -> "♊ İkizler"
            adLower.contains("yenge") -> "♋ Yengeç"
            adLower.contains("aslan") -> "♌ Aslan"
            adLower.contains("başak") -> "♍ Başak"
            adLower.contains("terazi") -> "♎ Terazi"
            adLower.contains("akrep") -> "♏ Akrep"
            adLower.contains("yay") -> "♐ Yay"
            adLower.contains("oğlak") -> "♑ Oğlak"
            adLower.contains("kova") -> "♒ Kova"
            adLower.contains("balık") -> "♓ Balık"
            else -> null
        }
    }

    // Burç analizi fonksiyonu
    fun burçAnalizi(burç: String): String {
        val analizler = when {
            burç.contains("Koç") -> listOf(
                "🌟 Parfüm Karakteriniz 🌟\n\nGüçlü ve tutkulu karakterinize en çok odunsu ve baharatlı parfümler yakışacaktır. Sizin gibi dinamik bir ruha sahip olanlar için en ideal seçim: Sıcak bahar esintileri ve doğal bergamot notaları.\n\n💫 Kişilik Yansımanız: Lider ruhlu, cesur ve enerjik\n✨ Size Özel Notalar: Doğal baharatlar, Taze bergamot, Sedir ağacı\n🎯 İdeal Parfüm Karakteri: Sıcak-Baharatlı\n\n💝 Özel Not: Sizin gibi güçlü karakterler için özel seçilmiş bu kombinasyon, liderlik özelliğinizi ve enerjinizi yansıtacaktır.",
                "🌟 Parfüm Kişiliğiniz 🌟\n\nEnerjik ve cesur karakterinize yakışacak en ideal parfüm tarzı: Odunsu ve baharatlı notalar. Doğal liderlik özelliğinizi tamamlayacak özel bir seçim.\n\n💫 Kişilik Yansımanız: Dinamik, kararlı ve tutkulu\n✨ Size Özel Notalar: Taze zencefil, Kırmızı biber, Sedir ağacı\n🎯 İdeal Parfüm Karakteri: Odunsu-Aromatik\n\n💝 Özel Not: Bu kombinasyon, doğal liderlik vasfınızı ve güçlü karakterinizi mükemmel şekilde yansıtacak.",
                "🌟 Parfüm Ruh Haliniz 🌟\n\nAteş elementinin enerjisini taşıyan karakterinize en uygun parfüm seçimi: Canlandırıcı ve dinamik notalar. Aktif yaşam tarzınızı tamamlayacak özel bir harmoni.\n\n💫 Kişilik Yansımanız: Enerjik, sportif ve özgüvenli\n✨ Size Özel Notalar: Taze portakal, Yeşil biber, Odunsu notalar\n🎯 İdeal Parfüm Karakteri: Citrus-Aromatik\n\n💝 Özel Not: Bu seçim, sportif ve dinamik yaşam tarzınızı destekleyecek ideal bir kombinasyon sunuyor."
            )
            burç.contains("Boğa") -> listOf(
                "🌟 Parfüm Karakteriniz 🌟\n\nLüks ve konfora düşkün yapınıza en çok toprak tonları ve çiçeksi notalar yakışacaktır. Sizin gibi zarif bir ruha sahip olanlar için özel seçim: Doğal vanilya ve yasemin notaları.\n\n💫 Kişilik Yansımanız: Zarif, kararlı ve lüks sever\n✨ Size Özel Notalar: Taze yasemin, Doğal vanilya, Toprak tonları\n🎯 İdeal Parfüm Karakteri: Çiçeksi-Odunsu\n\n💝 Özel Not: Bu kombinasyon, doğal zarafetinizi ve lüks anlayışınızı mükemmel şekilde yansıtacak.",
                "🌟 Parfüm Kişiliğiniz 🌟\n\nToprak elementinin güvenilir enerjisini taşıyan karakterinize en uygun seçim: Kalıcı ve zarif notalar. Kararlı yapınızı tamamlayacak özel bir harmoni.\n\n💫 Kişilik Yansımanız: Güvenilir, sakin ve şık\n✨ Size Özel Notalar: Taze çiçek özleri, Doğal amber, Odunsu notalar\n🎯 İdeal Parfüm Karakteri: Floral-Oriental\n\n💝 Özel Not: Bu seçim, güvenilir ve zarif karakterinizi destekleyecek ideal bir kombinasyon sunuyor."
            )
            burç.contains("İkizler") -> listOf(
                "🌟 Tam Size Göre Parfüm Analizi 🌟\n\nDeğişken ve sosyal karakterinize hafif ve ferah parfümler eşlik etmeli. Sizin gibi değişken bir ruha sahip olanlar için özel seçim: Narenciye ve yeşil çay notaları.\n\n💫 Kişilik Yansımanız: Değişken, sosyal ve hafif\n✨ Size Özel Notalar: Narenciye, Yeşil Çay, Limon\n🎯 Önerilen Parfüm Tarzı: Aromatik-Bitters",
                "🌟 Size Özel Parfüm Rehberi 🌟\n\nMeraklı ve dinamik yapınıza uygun, modern ve çok yönlü parfümler tercih edin. Modern ruhunuza uygun seçim: Limon ve nane notaları.\n\n💫 Kişilik Yansımanız: Modern, dinamik ve çok yönlü\n✨ Size Özel Notalar: Limon, Nane, Bergamot\n🎯 Önerilen Parfüm Tarzı: Aromatik-Citrus",
                "🌟 Kişisel Parfüm Analizi 🌟\n\nEntelektüel kişiliğinize yakışan, sofistike ve hafif parfümler kullanın. Entelektüel ruhunuza uygun seçim: Bergamot ve lavanta notaları.\n\n💫 Kişilik Yansımanız: Entelektüel, sofistik ve hafif\n✨ Size Özel Notalar: Bergamot, Lavanta, Gül\n🎯 Önerilen Parfüm Tarzı: Aromatik-Floral"
            )
            burç.contains("Yengeç") -> listOf(
                "🌟 Tam Size Göre Parfüm Analizi 🌟\n\nDuygusal ve romantik yapınıza deniz esintili ve pudralı parfümler çok yakışır. Sizin gibi duygusal bir ruha sahip olanlar için özel seçim: Okyanus ve beyaz çiçek notaları.\n\n💫 Kişilik Yansımanız: Duygusal, romantik ve deniz\n✨ Size Özel Notalar: Okyanus, Beyaz Çiçek, Odalı\n🎯 Önerilen Parfüm Tarzı: Deniz-Odalı",
                "🌟 Size Özel Parfüm Rehberi 🌟\n\nHassas ve koruyucu karakterinize sakin ve huzur verici kokular eşlik etmeli. Hassas ruhunuza uygun seçim: Yasemin ve nilüfer notaları.\n\n💫 Kişilik Yansımanız: Hassas, koruyucu ve sakin\n✨ Size Özel Notalar: Yasemin, Nilüfer, Vanilya\n🎯 Önerilen Parfüm Tarzı: Huzur-Odunsu",
                "🌟 Kişisel Parfüm Analizi 🌟\n\nŞefkatli doğanıza uygun, yumuşak ve rahatlatıcı parfümler kullanın. Doğanıza uygun seçim: Vanilya ve sümbül notaları.\n\n💫 Kişilik Yansımanız: Doğal, yumuşak ve rahat\n✨ Size Özel Notalar: Vanilya, Sümbül, Gül\n🎯 Önerilen Parfüm Tarzı: Doğal-Odunsu"
            )
            burç.contains("Aslan") -> listOf(
                "🌟 Tam Size Göre Parfüm Analizi 🌟\n\nGösterişli ve karizmatik yapınıza egzotik ve sıcak parfümler eşlik etmeli. Sizin gibi güçlü bir ruha sahip olanlar için özel seçim: Amber ve sandal ağacı notaları.\n\n💫 Kişilik Yansımanız: Güçlü, karizmatik ve egzotik\n✨ Size Özel Notalar: Amber, Sandal Ağacı, Gül\n🎯 Önerilen Parfüm Tarzı: Odunsu-Sıcak"
            )
            burç.contains("Başak") -> listOf(
                "🌟 Tam Size Göre Parfüm Analizi 🌟\n\nZarif ve titiz karakterinize temiz ve minimal parfümler yakışır. Sizin gibi analitik bir ruha sahip olanlar için özel seçim: Lavanta ve papatya notaları.\n\n💫 Kişilik Yansımanız: Zarif, titiz ve analitik\n✨ Size Özel Notalar: Lavanta, Papatya, Yeşil Çay\n🎯 Önerilen Parfüm Tarzı: Odunsu-Minimal"
            )
            burç.contains("Terazi") -> listOf(
                "🌟 Tam Size Göre Parfüm Analizi 🌟\n\nDengeli ve estetik yapınıza romantik ve sofistike parfümler çok yakışır. Sizin gibi dengeli bir ruha sahip olanlar için özel seçim: Gül ve şakayık notaları.\n\n💫 Kişilik Yansımanız: Dengeli, estetik ve romantik\n✨ Size Özel Notalar: Gül, Şakayık, Yasemin\n🎯 Önerilen Parfüm Tarzı: Odunsu-Romantik"
            )
            burç.contains("Akrep") -> listOf(
                "🌟 Tam Size Göre Parfüm Analizi 🌟\n\nGizemli ve tutkulu karakterinize yoğun ve baştan çıkarıcı parfümler eşlik etmeli. Sizin gibi güçlü bir ruha sahip olanlar için özel seçim: Misk ve patchouli notaları.\n\n💫 Kişilik Yansımanız: Güçlü, yoğun ve baştan çıkarıcı\n✨ Size Özel Notalar: Misk, Patchouli, Oud\n🎯 Önerilen Parfüm Tarzı: Odunsu-Yoğun"
            )
            burç.contains("Yay") -> listOf(
                "🌟 Tam Size Göre Parfüm Analizi 🌟\n\nMaceracı ve özgür ruhunuza egzotik ve canlandırıcı parfümler yakışır. Sizin gibi özgür bir ruha sahip olanlar için özel seçim: Tarçın ve portakal notaları.\n\n💫 Kişilik Yansımanız: Macera, özgür ve egzotik\n✨ Size Özel Notalar: Tarçın, Portakal, Mandalina\n🎯 Önerilen Parfüm Tarzı: Aromatik-Egzotik"
            )
            burç.contains("Oğlak") -> listOf(
                "🌟 Tam Size Göre Parfüm Analizi 🌟\n\nKlasik ve güvenilir yapınıza sofistike ve kalıcı parfümler eşlik etmeli. Sizin gibi klasik bir ruha sahip olanlar için özel seçim: Deri ve sedir ağacı notaları.\n\n💫 Kişilik Yansımanız: Klasik, güvenilir ve sofistik\n✨ Size Özel Notalar: Deri, Sedir, Amber\n🎯 Önerilen Parfüm Tarzı: Odunsu-Klasik"
            )
            burç.contains("Kova") -> listOf(
                "🌟 Tam Size Göre Parfüm Analizi 🌟\n\nÖzgün ve yenilikçi karakterinize modern ve sıra dışı parfümler yakışır. Sizin gibi özgün bir ruha sahip olanlar için özel seçim: Metalik ve ozon notaları.\n\n💫 Kişilik Yansımanız: Özgün, yenilikçi ve modern\n✨ Size Özel Notalar: Metalik, Ozon, Deniz Tuzu\n🎯 Önerilen Parfüm Tarzı: Modern-Sıra Dışı"
            )
            burç.contains("Balık") -> listOf(
                "🌟 Tam Size Göre Parfüm Analizi 🌟\n\nHayalperest ve romantik yapınıza mistik ve büyüleyici parfümler eşlik etmeli. Sizin gibi duygusal bir ruha sahip olanlar için özel seçim: İris ve deniz yosunu notaları.\n\n💫 Kişilik Yansımanız: Duygusal, romantik ve mistik\n✨ Size Özel Notalar: İris, Deniz Yosunu, Yasemin\n🎯 Önerilen Parfüm Tarzı: Odunsu-Mıstik"
            )
            else -> listOf("Lütfen geçerli bir burç giriniz.")
        }
        return analizler.random()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        RoyalPurple,
                        DeepPurple,
                        ShadowBlack
                    )
                )
            )
    ) {
        // Arkaplan efekti
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            StardustGold.copy(alpha = 0.05f),
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
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Üst başlık
            Text(
                text = "Parfüm Burcunuz",
                color = ShimmerGold,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 32.dp)
            )

            // Dönen burç sembolleri
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = burçSembolleri[currentSymbolIndex],
                    color = ShimmerGold,
                    fontSize = 64.sp,
                    modifier = Modifier.rotate(rotation)
                )
            }

            // Burç girişi
            OutlinedTextField(
                value = burçAdı,
                onValueChange = { 
                    burçAdı = it
                    seçiliBurç = burçBul(it)
                    analizSonucu = null // Yeni burç girildiğinde analizi sıfırla
                },
                label = {
                    Text(
                        "Burcunuzu Yazın",
                        color = LavenderMist
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ShimmerGold,
                    unfocusedBorderColor = PalePurple,
                    focusedTextColor = GlassWhite,
                    unfocusedTextColor = GlassWhite
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth()
            )

            // Bulunan burç
            if (seçiliBurç != null) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = DeepPurple.copy(alpha = 0.7f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    Text(
                        text = seçiliBurç!!,
                        color = ShimmerGold,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    )
                }

                // Analiz Butonu
                Button(
                    onClick = { 
                        seçiliBurç?.let { burç ->
                            analizSonucu = burçAnalizi(burç)
                        }
                    },
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
                        text = "✨ Mistik Analizi Göster",
                        color = ShadowBlack,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Analiz Sonucu
            if (analizSonucu != null) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MoonlightPurple.copy(alpha = 0.7f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = analizSonucu!!,
                            color = GlassWhite,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Paylaş Butonu
                        Button(
                            onClick = {
                                val shareIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_TEXT, """
                                        🌟 Parfüm Karakter Analizim 🌟
                                        
                                        ${seçiliBurç}
                                        
                                        ${analizSonucu}
                                        
                                        💫 Kişiye özel parfüm karakteri
                                        ✨ Doğal parfüm notaları
                                        🎯 Profesyonel parfüm analizi
                                        
                                        #PerfumeAnalyzer #ParfümKarakteri #DoğalParfüm
                                    """.trimIndent())
                                }
                                try {
                                    context.startActivity(Intent.createChooser(shareIntent, "Analizi Paylaş"))
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Paylaşım yapılırken bir hata oluştu", Toast.LENGTH_SHORT).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ShimmerGold
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 8.dp
                            )
                        ) {
                            Text(
                                text = "💫 Karakterimi Paylaş",
                                color = ShadowBlack,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Geri dönüş butonu
            Button(
                onClick = onNavigateBack,
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
                    text = "⟲ Ana Sayfaya Dön",
                    color = ShadowBlack,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
} 