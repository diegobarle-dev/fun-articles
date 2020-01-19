package uk.co.diegobarle.funarticles.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject

class FakeArticleDao @Inject constructor(): ArticleDao{

    private val articles = mutableListOf<Article>()

    override suspend fun insert(article: Article) {
        articles.add(article)
    }

    override suspend fun insertAll(articles: List<Article>) {
        this.articles.addAll(articles)
    }

    override fun getArticles(): LiveData<List<Article>> {
        val data = MutableLiveData<List<Article>>()
            data.value = articles.sortedByDescending { it.id }
        return data
    }

    override fun getArticle(id: Long): LiveData<Article> {
        val data = MutableLiveData<Article>()
        data.value = articles.firstOrNull { it.id == id }
        return data
    }

    override fun deleteAll() {
        articles.clear()
    }

}