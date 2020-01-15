package uk.co.diegobarle.funarticles.articles

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_articles.*
import uk.co.diegobarle.funarticles.R
import uk.co.diegobarle.funarticles.articledetails.ArticleDetailsActivity
import uk.co.diegobarle.funarticles.di.injector
import uk.co.diegobarle.funarticles.model.Article
import uk.co.diegobarle.funarticles.network.DataResult
import uk.co.diegobarle.funarticles.util.ARTICLE_ID_BUNDLE
import uk.co.diegobarle.funarticles.util.ARTICLE_TITLE_BUNDLE
import java.util.*

class ArticlesActivity : AppCompatActivity(), ArticlesAdapter.Callback {
    private val viewModel by lazy {
        ViewModelProvider(this, injector.articlesViewModelFactory()).get(ArticlesViewModel::class.java)
    }

    private val articlesAdapter = ArticlesAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_articles)

        setupViews()
        loadArticles()
    }

    private fun setupViews(){
        reloadButton.setOnClickListener { loadArticles() }
        listArticles.adapter = articlesAdapter

        //Decoration
        val decoration = androidx.recyclerview.widget.DividerItemDecoration(this, androidx.recyclerview.widget.DividerItemDecoration.VERTICAL)
        decoration.setDrawable(Objects.requireNonNull<Drawable>(ContextCompat.getDrawable(this, R.drawable.list_decorator_drawable)))
        listArticles.addItemDecoration(decoration)
    }

    private fun subscribeArticles(result: DataResult<List<Article>>){
        when (result.status) {
            DataResult.Status.SUCCESS -> {
                progressBar.isVisible = false
                reloadButton.isVisible = false
                result.data?.let { articlesAdapter.submitList(it) }
            }
            DataResult.Status.LOADING -> {
                if(articlesAdapter.itemCount == 0) progressBar.isVisible = true
                reloadButton.isVisible = false
            }
            DataResult.Status.ERROR -> {
                progressBar.isVisible = false
                if(articlesAdapter.itemCount == 0) reloadButton.isVisible = true
                Snackbar.make(root, getString(R.string.error_message), Snackbar.LENGTH_LONG).show()
            }
        }
    }

    override fun onArticleSelected(article: Article) {
        val intent = Intent(this, ArticleDetailsActivity::class.java).apply {
            putExtra(ARTICLE_ID_BUNDLE, article.id)
            putExtra(ARTICLE_TITLE_BUNDLE, article.title)
        }
        startActivity(intent)
    }

    private fun loadArticles(){
        viewModel.loadArticles().observe(this, Observer{ subscribeArticles(it) })
    }

}
