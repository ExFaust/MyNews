package com.exfaust.mynews.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.exfaust.mynews.data.model.Article
import io.reactivex.Completable
import io.reactivex.Maybe

@Dao
interface NewsDao {
    // Добавление в бд
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(news: List<Article>): Completable

    // Получение всех итемов из бд
    @Query("SELECT * FROM article ORDER BY publishedAt DESC")
    fun getArticles(): Maybe<List<Article>>

    //TODO нет явной стратегии для кеширования и нет очищения старых записей

    // Удаление бд
    @Query("DELETE FROM article")
    fun clear(): Completable
}