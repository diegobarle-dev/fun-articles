package uk.co.diegobarle.funarticles.articledetails

import androidx.lifecycle.ViewModel
import uk.co.diegobarle.funarticles.repositories.ArticlesRep
import javax.inject.Inject

class ArticleDetailsViewModel @Inject constructor(private val repository: ArticlesRep) : ViewModel() {
    fun loadArticle(id: Long) = repository.getArticle(id)
}