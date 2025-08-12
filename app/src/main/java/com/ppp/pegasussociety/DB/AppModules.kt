/*
package com.ppp.pegasussociety.DB

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Hilt module to provide the database dependencies.
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Provides a singleton instance of the Room database.
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ScreenTimeDatabase {
        return Room.databaseBuilder(
            context,
            ScreenTimeDatabase::class.java,
            "screen_time_database"
        ).build()
    }

    // Provides the DAO by getting it from the database instance.
    @Provides
    @Singleton
    fun provideScreenTimeDao(database: ScreenTimeDatabase): ScreenTimeDao {
        return database.screenTimeDao()
    }
}
*/
