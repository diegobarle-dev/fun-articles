package uk.co.diegobarle.funarticles.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uk.co.diegobarle.funarticles.model.Article
import uk.co.diegobarle.funarticles.model.ArticleDao

@Database(entities = [Article::class], version = 1, exportSchema = false)
abstract class FunArticlesDB : RoomDatabase() {

    abstract fun articleDao(): ArticleDao

    companion object {

        @Volatile
        private var instance: FunArticlesDB? = null

        fun getInstance(context: Context): FunArticlesDB {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): FunArticlesDB {
            return Room.databaseBuilder(context, FunArticlesDB::class.java, "fun_articles_db")
                .build()
        }
    }
}