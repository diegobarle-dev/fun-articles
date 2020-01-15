package uk.co.diegobarle.funarticles.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: Article)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: List<Article>)

    @Query("SELECT * from articles ORDER BY id DESC")
    fun getArticles() : LiveData<List<Article>>

    @Query("SELECT * from articles WHERE id = :id")
    fun getArticle(id: Long) : LiveData<Article>

    @Query("DELETE FROM articles")
    fun deleteAll()
}