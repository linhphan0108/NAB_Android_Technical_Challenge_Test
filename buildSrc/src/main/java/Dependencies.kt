import Versions.lifecycle_version
import Versions.retrofit_version

object Configs {
    const val applicationId = "com.linhphan.weatherforecast"
    const val compileSdkVersion = 31
    const val minSdkVersion = 21
    const val targetSdkVersion = 31
    const val versionCode = 1
    const val versionName = "1.0.0"
}

object Versions{
    const val hilt_version = "2.40.1"
    const val retrofit_version = "2.9.0"
    const val lifecycle_version = "2.4.0"
}

object TopLevelDependencies{
    const val gradleBuildTool = "com.android.tools.build:gradle:7.0.3"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.31"
    const val hiltGradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt_version}"
}

object Androidx{
    const val coreKtx = "androidx.core:core-ktx:1.7.0"
    const val appCompat = "androidx.appcompat:appcompat:1.3.1"
    const val material = "com.google.android.material:material:1.4.0"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.4"
    const val androidCoroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2"
    const val viewModelLifecycle = "androidx.lifecycle:lifecycle-viewmodel-ktx:${lifecycle_version}"
    const val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:${lifecycle_version}"
}

object TestDependencies{
    const val junit4 = "junit:junit:4.+"
    const val junitExt = "androidx.test.ext:junit:1.1.3"
    const val espresso = "androidx.test.espresso:espresso-core:3.4.0"
}

object Others{
    //network dependencies
    const val retrofit = "com.squareup.retrofit2:retrofit:$retrofit_version"
    const val retrofitGsonConverter = "com.squareup.retrofit2:converter-gson:$retrofit_version"
    const val okhttpInterceptor = "com.squareup.okhttp3:logging-interceptor:4.9.2"

    // hilt dependency injection
    const val hiltAndroid = "com.google.dagger:hilt-android:${Versions.hilt_version}"
    const val hiltAndroidCompiler =  "com.google.dagger:hilt-android-compiler:${Versions.hilt_version}"
}