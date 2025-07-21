package com.ppp.pegasussociety.di

import android.app.Application
import android.content.Context
import com.google.mlkit.common.sdkinternal.SharedPrefManager
import com.ppp.pegasussociety.ApiInterface.AllApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val AppBaseUrl = "https://childhood-unplugged-production.up.railway.app/api/"
  //  private const val JudgeBaseUrl = "https://judge.me/" // dummy, required by Retrofit

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .callTimeout(30, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    fun provideAppRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(AppBaseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAllApi(appRetrofit: Retrofit): AllApi {
        return appRetrofit.create(AllApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSharedPrefManager(context: Context): SharedPrefManager {
        return SharedPrefManager(context)
    }

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }
}