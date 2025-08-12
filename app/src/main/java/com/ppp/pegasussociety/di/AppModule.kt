package com.ppp.pegasussociety.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.google.mlkit.common.sdkinternal.SharedPrefManager
import com.ppp.pegasussociety.ApiInterface.AllApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .callTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideAppRetrofit(client: OkHttpClient): Retrofit {
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
/*
    @Provides
    @Singleton
    fun provideConverters(): Converters {
        return Converters()
    }

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        converters: Converters
    ): ScreenTimeDatabase {
        return Room.databaseBuilder(
            context,
            ScreenTimeDatabase::class.java,
            "screen_time_db"
        )
            .addTypeConverter(converters) // ✅ Required for Room to handle LocalDateTime, List<String>
            .build()
    }

    @Provides
    @Singleton
    fun provideDao(db: ScreenTimeDatabase): ScreenTimeDao = db.screenTimeDao()*/

    @Provides
    @Singleton
    fun provideSharedPrefManager(@ApplicationContext context: Context): SharedPrefManager {
        return SharedPrefManager(context)
    }

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }
}