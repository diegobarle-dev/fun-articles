package uk.co.diegobarle.funarticles.repositories

import uk.co.diegobarle.funarticles.data.resultLiveData
import uk.co.diegobarle.funarticles.model.ArticleDao
import uk.co.diegobarle.funarticles.network.requests.FetchArticles
import uk.co.diegobarle.funarticles.network.requests.GetArticle
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class ArticlesRep@Inject constructor(private val dao: ArticleDao) : IArticlesRep {
    @Inject
    protected lateinit var fetchArticles: FetchArticles

    @Inject
    protected lateinit var getArticle: GetArticle

    override fun getArticles() = resultLiveData(
        databaseQuery = { dao.getArticles() },
        networkCall = { fetchArticles.execute(FetchArticles.FetchArticlesRequest()) },
        saveCallResult = { dao.insertAll(it.result) }
    )

    override fun getArticle(id: Long) = resultLiveData(
        databaseQuery = { dao.getArticle(id) },
        networkCall = { getArticle.execute(GetArticle.GetArticleRequest(id)) },
        saveCallResult = { dao.insert(it.result) }
    )

}