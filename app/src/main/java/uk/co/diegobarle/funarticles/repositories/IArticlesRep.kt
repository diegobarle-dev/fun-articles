package uk.co.diegobarle.funarticles.repositories

import androidx.lifecycle.LiveData
import uk.co.diegobarle.funarticles.model.Article
import uk.co.diegobarle.funarticles.network.DataResult

interface IArticlesRep {
    fun getArticles(): LiveData<DataResult<List<Article>>>
    fun getArticle(id: Long): LiveData<DataResult<Article>>
}