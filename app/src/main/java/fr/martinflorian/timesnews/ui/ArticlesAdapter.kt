package fr.martinflorian.timesnews.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import fr.martinflorian.timesnews.R
import fr.martinflorian.timesnews.databinding.ItemArticleBinding
import fr.martinflorian.timesnews.model.Article

class ArticlesAdapter(private val onArticleClick: (Article) -> Unit) :
    ListAdapter<Article, ArticlesAdapter.ViewHolder>(DiffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
        holder.itemView.setOnClickListener { onArticleClick(currentItem) }
    }


    class ViewHolder(private var binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(article: Article) {
            val imageUri = article.imageUrl?.toUri()?.buildUpon()?.scheme("https")?.build()
            binding.apply {
                itemArticleCategory.text = article.section
                itemArticleTitle.text = article.title
                itemArticleSnippet.text = article.snippet
                itemArticleAuthors.text = article.authors
                itemArticleImage.load(imageUri) {
                    placeholder(R.drawable.loading_animation)
                    error(R.drawable.image_not_found)
                }
            }
        }
    }

    companion object DiffCallBack : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(
            oldItem: Article,
            newItem: Article
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: Article,
            newItem: Article
        ): Boolean {
            return oldItem.title == newItem.title && oldItem.webUrl == newItem.webUrl
        }
    }
}
