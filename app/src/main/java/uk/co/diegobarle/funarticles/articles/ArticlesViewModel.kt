package uk.co.diegobarle.funarticles.articles

import androidx.lifecycle.ViewModel
import uk.co.diegobarle.funarticles.repositories.ArticlesRep
import javax.inject.Inject

class ArticlesViewModel @Inject constructor(private val repository: ArticlesRep) : ViewModel() {
    fun loadArticles() = repository.getArticles()
}