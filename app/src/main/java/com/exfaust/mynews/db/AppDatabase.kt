package com.exfaust.mynews.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.exfaust.mynews.data.model.Article
import com.exfaust.mynews.db.dao.NewsDao

@Database(
    entities = [Article::class],
    version = DbConstants.CURRENT_VERSION,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract val getNewsDao: NewsDao
}