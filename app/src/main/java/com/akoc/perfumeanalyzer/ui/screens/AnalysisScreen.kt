package com.akoc.perfumeanalyzer.ui.screens

import android.content.Intent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.akoc.perfumeanalyzer.ui.theme.GlassWhite
import com.akoc.perfumeanalyzer.ui.theme.MysticGold
import kotlinx.coroutines.delay
import kotlin.random.Random

enum class AnalysisState {
    Loading,
    Questions,
    Analysis,
    Result
}

data class Question(
    val text: String,
    val options: List<String>
)

data class Tuple4<T1, T2, T3, T4>(
    val first: T1,
    val second: T2,
    val third: T3,
    val fourth: T4
)

@Composable
fun AnalysisScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentState by remember { mutableStateOf(AnalysisState.Loading) }
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var answers by remember { mutableStateOf(listOf<String>()) }
    
    val questions = remember {
        listOf(
            Question(
                "Bu koku size hangi dönemi hatırlatıyor?",
                listOf(
                    "Çocukluk yılları",
                    "Gençlik dönemi",
                    "Aile büyükleriyle geçen zamanlar",
                    "Yakın geçmiş"
                )
            ),
            Question(
                "Bu koku size nasıl bir mekan hissettiriyor?",
                listOf(
                    "Eski bir kitapçı",
                    "Huzurlu bir ev",
                    "Gizli bir bahçe",
                    "Deniz kenarı"
                )
            ),
            Question(
                "Bu koku size hangi duyguyu çağrıştırıyor?",
                listOf(
                    "Nostalji ve özlem",
                    "Huzur ve dinginlik",
                    "Tutku ve heyecan",
                    "Neşe ve mutluluk"
                )
            )
        )
    }

    fun generateMysticAnalysis(userAnswers: List<String>): Tuple4<String, String, String, String> {
        val (period, place, emotion) = userAnswers

        // Genel analiz
        val generalAnalysis = when (emotion) {
            "Nostalji ve özlem" -> "Parfümünüz, geçmişin derinliklerinden gelen bir hikaye anlatıyor. Zamanın tozlu sayfalarında saklı kalmış, ancak hala canlılığını koruyan anıları canlandırıyor. Her notası, özlemle karışık tatlı bir hüzün taşıyor."
            "Huzur ve dinginlik" -> "Bu koku, ruhunuza dokunan sakin bir melodi gibi. İçinizdeki fırtınaları dindiren, sizi güvenli bir limana taşıyan notalar barındırıyor. Adeta bir zen bahçesinin huzurunu taşıyor."
            "Tutku ve heyecan" -> "Parfümünüz, içinizdeki ateşi yansıtıyor. Her notası tutkulu bir dans gibi birbirini takip ediyor. Canlı ve dinamik karakteri, hayata olan tutkunuzu ortaya çıkarıyor."
            else -> "Bu koku, hayatın tüm güzelliklerini kutlayan bir şenlik gibi. Neşeli ve pozitif enerjisiyle etrafınıza mutluluk saçıyor. Her notası, yaşamın tadını çıkarmanın önemini hatırlatıyor."
        }

        // Karakter profili
        val characterProfile = when (place) {
            "Eski bir kitapçı" -> "Entelektüel ve düşünceli bir ruha sahipsiniz. Bilgelik arayışınız ve derin düşünme eğiliminiz, karakterinizin temel taşlarını oluşturuyor. Tıpkı eski kitapların sayfaları gibi, her deneyimden bir şeyler öğrenmeye açıksınız."
            "Huzurlu bir ev" -> "Sıcak ve samimi bir karakteriniz var. İnsanları rahatlatma ve güven verme yeteneğiniz öne çıkıyor. Çevrenizdekiler için güvenli bir liman olmayı başarıyorsunuz."
            "Gizli bir bahçe" -> "Gizemli ve derin bir kişiliğe sahipsiniz. İç dünyanız zengin, düşünceleriniz özgün. Doğayla ve kendi benliğinizle kurduğunuz bağ, size özel bir karakter kazandırıyor."
            else -> "Özgür ruhlu ve maceracı bir yapınız var. Deniz gibi sürekli hareket halinde, yeni keşiflere açık bir karaktersiniz. Spontane ve eğlenceli kişiliğiniz dikkat çekiyor."
        }

        // Özel anı analizi
        val specialMoment = when (period) {
            "Çocukluk yılları" -> listOf(
                "Annemin Çamaşır Günü: Balkonda kuruyan çamaşırların arasında koşarken, temiz sabun kokusu ve ıhlamur ağacının tatlı esintisi... Güneşin altında dans eden beyaz çarşaflar arasında saklambaç oynardık.",
                "Dedemin Bahçesi: Mis gibi kokan domates fideleri, taze çilek kokusu ve dedemin ellerindeki toprak kokusu... Her sabah erkenden kalkar, çiçekleri sular, sebzeleri toplardık.",
                "Okul Yolu: Sonbahar sabahlarında ıslak yaprakların kokusu, elimde taze simit, sıcacık bir mont... Arkadaşlarla paylaşılan kahkahalar ve masum sırlar.",
                "Bayram Sabahları: Taze pişmiş baklavanın kokusu, kolonya şişeleri, yeni elbiseler... Büyüklerin elini öpmeye giderken heyecanla çarpan kalpler.",
                "Yaz Tatili: Deniz tuzu, güneş kremi ve dondurma kokuları... Kumsalda oynarken geçen saatler, akşamüstü sahilde yapılan yürüyüşler."
            ).random()

            "Gençlik dönemi" -> listOf(
                "İlk Randevu: Heyecandan titreyen eller, özenle seçilmiş parfüm, kafede içilen ilk kahve... Zaman durmuş gibiydi, sadece kalp atışları sayılıyordu.",
                "Üniversite Kampüsü: Kütüphanedeki eski kitapların kokusu, kantin sohbetleri, bahar şenliklerinin coşkusu... Hayallerin ve ideallerin filizlendiği o özel günler.",
                "Mezuniyet Balosu: Smokinlerin ve uzun elbiselerin şıklığı, dans pistinin büyüsü... O gece herkes kendi masalının kahramanıydı.",
                "İlk İş Günü: Yeni takım elbisenin verdiği özgüven, ofis koridorlarındaki kahve kokusu... Hayata atılmanın gururu ve heyecanı.",
                "Arkadaş Buluşmaları: Favori kafenin sıcak atmosferi, bitmeyen sohbetler, paylaşılan hayaller... Her buluşma yeni bir macera gibiydi."
            ).random()

            "Aile büyükleriyle geçen zamanlar" -> listOf(
                "Büyükannemin Mutfağı: Tarçın ve karanfil kokulu dolaplardaki baharatlar, taze pişen kurabiyelerin kokusu... Her yemek bir sevgi gösterisiydi.",
                "Bayram Ziyaretleri: Eski konakların ahşap kokusu, gül şerbeti ikramları, dantel örtülü sehpalar... Aile büyüklerinin anlattığı hikayelerle dolan odalar.",
                "Pazar Kahvaltıları: Taze demlenmiş çayın buğusu, ev yapımı reçeller, sıcak ekmek kokusu... Ailenin bir araya geldiği o özel sabahlar.",
                "Ramazan Akşamları: İftar sofralarının bereketi, teravih namazı dönüşü içilen ıhlamurlar... Manevi atmosferin sardığı ev ortamı.",
                "Yaz Tatili Buluşmaları: Bahçedeki meyve ağaçlarının kokusu, akşamüstü çay sohbetleri... Kuşakları birleştiren o değerli anlar."
            ).random()

            else -> listOf(
                "Şehir Gezintisi: Yağmur sonrası ıslak kaldırımlar, sahil kenarındaki tuzlu rüzgar... Şehrin karmaşasında bulunan huzur köşeleri.",
                "Sanat Galerisi: Yeni açılan bir serginin heyecanı, ahşap parkeler, taze kahve kokusu... Sanatla iç içe geçen zamanın büyüsü.",
                "Kitapçı Keşfi: Raflardaki kitapların kağıt kokusu, sessizce gezinen insanlar... Her kitabın yeni bir dünyaya açılan kapı olduğu anlar.",
                "Park Yürüyüşü: Sonbahar yapraklarının hışırtısı, bankta dinlenirken içilen kahve... Şehrin gürültüsünden uzak, huzur dolu dakikalar.",
                "Müzik Dinletisi: Konser salonunun atmosferi, klasik müziğin ruhu okşayan notaları... Sanatın en saf halinin yaşandığı özel akşamlar."
            ).random()
        }

        // Müzik önerisi
        val musicSuggestion = when (emotion) {
            "Nostalji ve özlem" -> "Klasik Türk Müziği'nin zarif makamları size eşlik edebilir. Özellikle akşam saatlerinde, Münir Nurettin Selçuk'un 'Endülüs'te Raks' veya Safiye Ayla'nın 'Sevenler Ağlarmış' gibi eserlerini dinleyebilirsiniz."
            "Huzur ve dinginlik" -> "Ludovico Einaudi'nin minimal piyano eserleri veya geleneksel Japon müziğinin sakin melodileri ruhunuza iyi gelecektir. 'Experience' veya 'Nuvole Bianche' gibi parçalar özellikle önerilir."
            "Tutku ve heyecan" -> "Flamenko gitarın ateşli ritimleri veya Latin caz füzyonları size eşlik edebilir. Paco de Lucia'nın virtüöz performansları veya Buena Vista Social Club'ın tutkulu melodileri tam size göre."
            else -> "Pozitif ve neşeli akustik folk müzik, ruhunuzu dans ettirmeye hazır. Jack Johnson, Jason Mraz gibi sanatçıların hayat dolu melodileri veya modern Türk folk müziğinin neşeli örnekleri size eşlik edebilir."
        }

        return Tuple4(
            generalAnalysis,
            characterProfile,
            specialMoment,
            musicSuggestion
        )
    }

    LaunchedEffect(currentState) {
        when (currentState) {
            AnalysisState.Loading -> {
                delay(2000)
                currentState = AnalysisState.Questions
            }
            AnalysisState.Analysis -> {
                delay(3000)
                currentState = AnalysisState.Result
            }
            else -> {}
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A14))
    ) {
        when (currentState) {
            AnalysisState.Loading -> {
                LoadingScreen()
            }
            AnalysisState.Questions -> {
                QuestionScreen(
                    question = questions[currentQuestionIndex],
                    onAnswerSelected = { answer ->
                        answers = answers + answer
                        if (currentQuestionIndex < questions.size - 1) {
                            currentQuestionIndex++
                        } else {
                            currentState = AnalysisState.Analysis
                        }
                    }
                )
            }
            AnalysisState.Analysis -> {
                AnalyzingScreen()
            }
            AnalysisState.Result -> {
                ResultScreen(
                    onBackClick = onBackClick,
                    analysis = generateMysticAnalysis(answers)
                )
            }
        }
    }
}

@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A14)),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = MysticGold,
            modifier = Modifier.size(64.dp)
        )
    }
}

@Composable
private fun AnalyzingScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            color = MysticGold,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Anılarınız Analiz Ediliyor...",
            color = MysticGold,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Kokular ve duygular arasındaki bağlar keşfediliyor",
            color = GlassWhite.copy(alpha = 0.7f),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun QuestionScreen(
    question: Question,
    onAnswerSelected: (String) -> Unit
) {
    // Animasyonlar için state'ler
    val questionAlpha = remember { Animatable(0f) }
    val questionScale = remember { Animatable(0.8f) }
    val optionsOffsetY = remember { Animatable(100f) }
    
    // Partiküller için state'ler
    val particles = remember { List(20) { Offset(Random.nextFloat() * 1000, Random.nextFloat() * 2000) } }
    val particleOffsets = particles.map { initialOffset ->
        remember { Animatable(initialOffset, Offset.VectorConverter) }
    }

    // Giriş animasyonları
    LaunchedEffect(Unit) {
        questionAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(1000)
        )
        questionScale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
        optionsOffsetY.animateTo(
            targetValue = 0f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
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

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Arka plan partikülleri
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            particleOffsets.forEach { offset ->
                drawCircle(
                    color = MysticGold.copy(alpha = 0.2f),
                    radius = (2..5).random().dp.toPx(),
                    center = offset.value
                )
            }
        }

        // Soru ve seçenekler
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Soru başlığı
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
                    .alpha(questionAlpha.value)
                    .scale(questionScale.value)
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
                Text(
                    text = question.text,
                    color = GlassWhite,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            // Seçenekler
            question.options.forEachIndexed { index, option ->
                Button(
                    onClick = { onAnswerSelected(option) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .alpha(questionAlpha.value)
                        .offset(y = optionsOffsetY.value.dp)
                        .scale(questionScale.value),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1A1A2E)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 8.dp,
                        pressedElevation = 4.dp
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                    ) {
                        Text(
                            text = option,
                            color = GlassWhite,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }

                // Seçenekler arasına dekoratif çizgi
                if (index < question.options.size - 1) {
                    Box(
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .width(100.dp)
                            .height(1.dp)
                            .alpha(0.3f)
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        MysticGold,
                                        Color.Transparent
                                    )
                                )
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun ResultScreen(
    onBackClick: () -> Unit,
    analysis: Tuple4<String, String, String, String>
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // Paylaşma işlevi
    val shareAnalysis: () -> Unit = {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Parfüm Analizi")
            putExtra(Intent.EXTRA_TEXT, """
                🌟 Parfüm Analizi 🌟
                
                ${analysis.first}
                
                👤 Karakter Yansıması:
                ${analysis.second}
                
                💫 Özel Anı:
                ${analysis.third}
                
                🎵 Müzik Önerisi:
                ${analysis.fourth}
                
                #PerfumeAnalyzer
            """.trimIndent())
        }
        context.startActivity(Intent.createChooser(shareIntent, "Analizi Paylaş"))
    }

    // Partiküller için state'ler
    val particles = remember { List(30) { Offset(Random.nextFloat() * 1000, Random.nextFloat() * 2000) } }
    val particleOffsets = particles.map { initialOffset ->
        remember { Animatable(initialOffset, Offset.VectorConverter) }
    }

    // Arka plan gradyanı için animasyon
    val infiniteTransition = rememberInfiniteTransition(label = "background")
    val gradientAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "gradient"
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Animasyonlu arka plan
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .blur(50.dp)
        ) {
            rotate(gradientAngle) {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF2C1810),
                            Color(0xFF1A1A2E),
                            Color(0xFF0A0A14)
                        ),
                        center = Offset(size.width / 2, size.height / 2),
                        radius = size.width
                    ),
                    center = Offset(size.width / 2, size.height / 2),
                    radius = size.width
                )
            }
        }

        // Partiküller
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

        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            particleOffsets.forEach { offset ->
                drawCircle(
                    color = MysticGold.copy(alpha = 0.2f),
                    radius = (2..5).random().dp.toPx(),
                    center = offset.value
                )
            }
        }

        // İçerik
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // Header bölümü
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
                        onClick = onBackClick,
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Geri",
                            tint = GlassWhite
                        )
                    }

                    Text(
                        text = "Parfüm Analizi",
                        color = MysticGold,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1A1A2E)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Genel İzlenim",
                        color = MysticGold,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    Text(
                        text = analysis.first,
                        color = GlassWhite,
                        fontSize = 16.sp,
                        lineHeight = 24.sp
                    )
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1A1A2E)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Karakter Yansıması",
                        color = MysticGold,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    Text(
                        text = analysis.second,
                        color = GlassWhite,
                        fontSize = 16.sp,
                        lineHeight = 24.sp
                    )
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1A1A2E)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Özel Anı Analizi",
                        color = MysticGold,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    Text(
                        text = analysis.third,
                        color = GlassWhite,
                        fontSize = 16.sp,
                        lineHeight = 24.sp
                    )
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1A1A2E)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Müzik Önerisi",
                        color = MysticGold,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    Text(
                        text = analysis.fourth,
                        color = GlassWhite,
                        fontSize = 16.sp,
                        lineHeight = 24.sp
                    )
                }
            }

            val nightUseProgress = remember { mutableFloatStateOf(0f) }
            val seasonalProgress = remember { mutableFloatStateOf(0f) }
            val personalityMatchProgress = remember { mutableFloatStateOf(0f) }
            
            LaunchedEffect(Unit) {
                nightUseProgress.floatValue = (55..95).random() / 100f
                seasonalProgress.floatValue = (45..98).random() / 100f
                personalityMatchProgress.floatValue = (70..95).random() / 100f
            }

            AnimatedMetricCard(
                title = "Gece Kullanımı",
                value = "${(nightUseProgress.floatValue * 100).toInt()}%",
                progress = nightUseProgress.floatValue,
                description = when {
                    nightUseProgress.floatValue > 0.8f -> "Gece kullanımı için mükemmel bir seçim"
                    nightUseProgress.floatValue > 0.6f -> "Gece kullanımına oldukça uygun"
                    else -> "Gece kullanımı için orta düzeyde uygun"
                }
            )

            AnimatedMetricCard(
                title = "Mevsimsel Uyum",
                value = "${(seasonalProgress.floatValue * 100).toInt()}%",
                progress = seasonalProgress.floatValue,
                description = when {
                    seasonalProgress.floatValue > 0.8f -> "Bu mevsim için ideal bir seçim"
                    seasonalProgress.floatValue > 0.6f -> "Bu mevsimde rahatlıkla kullanılabilir"
                    else -> "Bu mevsimde dikkatli kullanılmalı"
                }
            )

            AnimatedMetricCard(
                title = "Kişilik Uyumu",
                value = "${(personalityMatchProgress.floatValue * 100).toInt()}%",
                progress = personalityMatchProgress.floatValue,
                description = when {
                    personalityMatchProgress.floatValue > 0.8f -> "Kişiliğinizle mükemmel uyum sağlıyor"
                    personalityMatchProgress.floatValue > 0.6f -> "Kişiliğinizle iyi uyum sağlıyor"
                    else -> "Kişiliğinizle farklı bir tarz"
                }
            )

            // Butonlar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onBackClick,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MysticGold
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Yeni Analiz",
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                Button(
                    onClick = shareAnalysis,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1A1A2E)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Analizi Paylaş",
                        color = GlassWhite,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }

        // Yüzen Paylaş Butonu
        FloatingActionButton(
            onClick = shareAnalysis,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MysticGold,
            contentColor = Color.Black
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Paylaş"
            )
        }
    }
}

@Composable
private fun AnimatedMetricCard(
    title: String,
    value: String,
    progress: Float,
    description: String
) {
    val cardAlpha = remember { Animatable(0f) }
    val cardOffset = remember { Animatable(-100f) }

    LaunchedEffect(Unit) {
        cardAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(1000)
        )
        cardOffset.animateTo(
            targetValue = 0f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .alpha(cardAlpha.value)
            .offset(x = cardOffset.value.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A1A2E)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    color = GlassWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = value,
                    color = MysticGold,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape)
                    .background(Color.DarkGray)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .height(8.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    MysticGold,
                                    MysticGold.copy(alpha = 0.7f)
                                )
                            )
                        )
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = description,
                color = GlassWhite.copy(alpha = 0.7f),
                fontSize = 14.sp
            )
        }
    }
} 