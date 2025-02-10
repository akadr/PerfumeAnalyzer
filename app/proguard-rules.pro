# Compose için kurallar
-dontwarn androidx.compose.**
-keep class androidx.compose.** { *; }
-keepclassmembers class androidx.compose.** { *; }

# CameraX için kurallar
-keep class androidx.camera.** { *; }
-dontwarn androidx.camera.**
-keepclassmembers class androidx.camera.** { *; }

# Coil için kurallar
-keep class coil.** { *; }
-dontwarn coil.**

# Navigation için kurallar
-keepnames class androidx.navigation.** { *; }
-keepnames class * extends androidx.navigation.NavDestination { *; }

# Kotlin Serialization için kurallar
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

# Coroutines için kurallar
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# Uygulama modelleri için kurallar
-keep class com.example.perfumeanalyzer.data.** { *; }
-keep class com.example.perfumeanalyzer.model.** { *; }

# Genel kurallar
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception

# Android lifecycle components
-keep class * extends androidx.lifecycle.ViewModel {
    <init>();
}
-keep class * extends androidx.lifecycle.AndroidViewModel {
    <init>(android.app.Application);
}
-keepclassmembers class * implements androidx.lifecycle.LifecycleObserver {
    <init>(...);
}
-keepclassmembers class * implements androidx.lifecycle.LifecycleOwner {
    androidx.lifecycle.Lifecycle getLifecycle();
}

# Reflection kullanımı için
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# Enum'lar için
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Parcelable implementasyonları için
-keepclassmembers class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator CREATOR;
}

# Native metodlar için
-keepclasseswithmembernames class * {
    native <methods>;
}

# View constructors için
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

# Custom Application sınıfı için
-keep public class * extends android.app.Application

# R sınıfı için
-keep class **.R
-keep class **.R$* {
    <fields>;
} 