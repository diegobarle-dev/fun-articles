package uk.co.diegobarle.funarticles.articles

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uk.co.diegobarle.funarticles.R
import uk.co.diegobarle.funarticles.model.Article

class ArticlesAdapter(private val callback: Callback): ListAdapter<Article, ArticlesAdapter.ViewHolder>(DiffCallback()){

    interface Callback{
        fun onArticleSelected(article: Article)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.view_article_list_item, parent, false)
        return ViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), callback)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        private val tvSubtitle = itemView.findViewById<TextView>(R.id.tvSubtitle)
        private val tvDate = itemView.findViewById<TextView>(R.id.tvDate)

        fun bind(item: Article, callback: Callback) {
            tvTitle.text = item.title
            tvSubtitle.text = item.subtitle
            tvDate.text = item.date
            itemView.setOnClickListener { callback.onArticleSelected(item) }
        }
    }
}

private class DiffCallback : DiffUtil.ItemCallback<Article>() {

    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem == newItem
    }
}