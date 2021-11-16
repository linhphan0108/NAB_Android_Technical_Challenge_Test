package com.linhphan.data.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.linhphan.data.BuildConfig
import com.linhphan.data.R
import com.linhphan.data.remote.Services
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

private const val READ_TIME_OUT = 60L
private const val WRITE_TIME_OUT = 60L
private const val CONNECT_TIME_OUT = 60L
@Module
@InstallIn(SingletonComponent::class)
class RemoteModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        val builder = GsonBuilder()
        return builder.create()
    }

    @Provides
    @Singleton
    fun provideConverterFactory(gson: Gson): Converter.Factory {
        return GsonConverterFactory.create(gson)
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    @Provides
    @Singleton
    @Named("SSLCertificate")
    fun provideSSLCertificate(context: Context): List<Pair<String,String>> {
        val domain = context.getString(R.string.domain)
        return listOf(
            Pair(domain, context.getString(R.string.cert1)),
            Pair(domain, context.getString(R.string.cert2)),
            Pair(domain, context.getString(R.string.cert3))
        )
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        @Named("SSLCertificate") sslCertificate: List<Pair<String, String>>
    ): OkHttpClient {
        val cp = CertificatePinner.Builder().apply {
            sslCertificate.forEach {
                this.add(it.first, it.second)
            }
        }.build()
        val builder = OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
            .certificatePinner(cp)
            .addInterceptor{chain ->
                val builder =  chain.request().newBuilder().apply {
                    addHeader("Content-Type", "application/json")
                    addHeader("Accept", "application/json")
                }
                chain.proceed(builder.build())
            }
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(httpLoggingInterceptor)
        }
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, factory: Converter.Factory): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BuildConfig.DOMAIN)
            .addConverterFactory(factory)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): Services {
        return retrofit.create(Services::class.java)
    }
}