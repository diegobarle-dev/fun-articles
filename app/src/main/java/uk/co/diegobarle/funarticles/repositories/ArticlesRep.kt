package uk.co.diegobarle.funarticles.repositories

import androidx.lifecycle.LiveData
import uk.co.diegobarle.funarticles.data.resultLiveData
import uk.co.diegobarle.funarticles.model.ArticleDao
import uk.co.diegobarle.funarticles.network.requests.FetchArticles
import uk.co.diegobarle.funarticles.network.requests.GetArticle
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class ArticlesRep@Inject constructor(private val dao: ArticleDao){
    @Inject
    protected lateinit var fetchArticles: FetchArticles

    @Inject
    protected lateinit var getArticle: GetArticle

    fun getArticles() = resultLiveData(
        databaseQuery = { dao.getArticles() },
        networkCall = { fetchArticles.execute(FetchArticles.FetchArticlesRequest()) },
        saveCallResult = { dao.insertAll(it.result) }
    )

    fun getArticle(id: Long) = resultLiveData(
        databaseQuery = { dao.getArticle(id) },
        networkCall = { getArticle.execute(GetArticle.GetArticleRequest(id)) },
        saveCallResult = { dao.insert(it.result) }
    )

}