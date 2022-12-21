package fr.martinflorian.timesnews.ui.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import fr.martinflorian.timesnews.R
import fr.martinflorian.timesnews.databinding.ItemArticleCategoryBinding
import fr.martinflorian.timesnews.model.ArticleCategory

class CategoriesAdapter(
    private val categoriesList: List<ArticleCategory>,
    private val onItemClick: (ArticleCategory) -> Unit
) :
    RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

    private var selectedIndex = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemArticleCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = categoriesList[position]
        holder.bind(currentItem)
        holder.itemView.apply {
            setOnClickListener {
                onItemClick(currentItem)
                selectedIndex = holder.adapterPosition
                notifyDataSetChanged()
            }
        }
        if (holder.adapterPosition == selectedIndex) {
            holder.itemView.background = ContextCompat.getDrawable(
                holder.itemView.context,
                R.drawable.clicked_category_background
            )
            holder.binding.itemArticleCategoryName.setTextColor(
                ContextCompat.getColor(holder.itemView.context, R.color.white)
            )
        } else {
            holder.itemView.background = ContextCompat.getDrawable(
                holder.itemView.context,
                R.drawable.default_category_background
            )
            holder.binding.itemArticleCategoryName.setTextColor(
                ContextCompat.getColor(holder.itemView.context, R.color.black)
            )
        }
    }

    override fun getItemCount(): Int {
        return categoriesList.size
    }


    class ViewHolder(val binding: ItemArticleCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(articleCategory: ArticleCategory) {
            binding.itemArticleCategoryName.text = articleCategory.name
        }
    }
}