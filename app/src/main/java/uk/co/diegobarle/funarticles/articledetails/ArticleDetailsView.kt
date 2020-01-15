package uk.co.diegobarle.funarticles.articledetails

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.view_article_details.view.*
import uk.co.diegobarle.funarticles.R
import uk.co.diegobarle.funarticles.model.Article

class ArticleDetailsView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): ConstraintLayout(context, attrs, defStyleAttr){

    lateinit var article: Article

    init {
        inflate(context, R.layout.view_article_details, this)
    }

    fun bind(article: Article){
        this.article = article
        tvBody.text = article.body?:context.getString(R.string.loading_details_message)
        tvDate.text = article.date
        tvSubtitle.text = article.subtitle
    }

    fun isInitialized() = ::article.isInitialized
}