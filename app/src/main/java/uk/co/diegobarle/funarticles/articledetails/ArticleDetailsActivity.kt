package uk.co.diegobarle.funarticles.articledetails

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import uk.co.diegobarle.funarticles.R

import kotlinx.android.synthetic.main.activity_article_details.*
import kotlinx.android.synthetic.main.content_article_details.*
import uk.co.diegobarle.funarticles.di.injector
import uk.co.diegobarle.funarticles.model.Article
import uk.co.diegobarle.funarticles.network.DataResult
import uk.co.diegobarle.funarticles.util.ARTICLE_ID_BUNDLE
import uk.co.diegobarle.funarticles.util.ARTICLE_TITLE_BUNDLE

class ArticleDetailsActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this, injector.articleDetailsViewModelFactory()).get(ArticleDetailsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_details)
        setSupportActionBar(toolbar)

        val id = intent.getLongExtra(ARTICLE_ID_BUNDLE, -1)
        val title = intent.getStringExtra(ARTICLE_TITLE_BUNDLE)
        if(id < 0 || title.isNullOrEmpty()){
            finish()
            return
        }

        setTitle(title)

        viewModel.loadArticle(id).observe(this, Observer{ subscribeArticle(it) })
    }

    private fun subscribeArticle(result: DataResult<Article>){
        when (result.status) {
            DataResult.Status.SUCCESS -> {
                progressBar.isVisible = false
                result.data?.let { viewArticle.bind(it) }
            }
            DataResult.Status.LOADING -> if(viewArticle.isInitialized()) progressBar.isVisible = true
            DataResult.Status.ERROR -> {
                progressBar.isVisible = false
                Snackbar.make(root, result.message!!, Snackbar.LENGTH_LONG).show()
            }
        }
    }

}
