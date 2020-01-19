package uk.co.diegobarle.funarticles.repositories

import androidx.lifecycle.LiveData
import org.jetbrains.annotations.TestOnly
import uk.co.diegobarle.funarticles.data.insertData
import uk.co.diegobarle.funarticles.data.resultLiveData
import uk.co.diegobarle.funarticles.model.Article
import uk.co.diegobarle.funarticles.model.FakeArticleDao
import uk.co.diegobarle.funarticles.network.DataResult
import javax.inject.Inject

class FakeArticlesRep @Inject constructor(private val dao: FakeArticleDao): IArticlesRep{

    override fun getArticles(): LiveData<DataResult<List<Article>>>
            = resultLiveData { dao.getArticles() }

    override fun getArticle(id: Long): LiveData<DataResult<Article>>
            = resultLiveData { dao.getArticle(id)}

    @TestOnly
    fun saveArticle(vararg article: Article){
        insertData{ dao.insertAll(article.toList()) }
    }

    @TestOnly
    fun getArticlesError(): LiveData<DataResult<List<Article>>>
            = resultLiveData { throw Exception("Error mock") }

    @TestOnly
    fun getArticleError(): LiveData<DataResult<Article>>
            = resultLiveData { throw Exception("Error mock") }
}