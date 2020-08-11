package com.exfaust.mynews.di.modules

import android.content.Context
import androidx.room.Room
import com.exfaust.mynews.db.AppDatabase
import com.exfaust.mynews.db.DbConstants
import com.exfaust.mynews.db.dao.NewsDao
import dagger.Module
import dagger.Provides

@Module
class DbModule {

    @Provides
    fun provideDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder<AppDatabase>(
            context,
            AppDatabase::class.java,
            DbConstants.DB_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideBookBasketItemDao(db: AppDatabase): NewsDao {
        return db.getNewsDao
    }
}