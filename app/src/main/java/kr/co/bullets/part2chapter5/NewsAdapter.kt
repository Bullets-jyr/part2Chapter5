package kr.co.bullets.part2chapter5

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.co.bullets.part2chapter5.databinding.ItemNewsBinding

class NewsAdapter : ListAdapter<NewsModel, NewsAdapter.NewsViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class NewsViewHolder(private val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: NewsModel) {
            binding.titleTextView.text = item.title
            Glide.with(binding.thumbnailImageView)
                .load(item.imageUrl)
                .into(binding.thumbnailImageView)
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<NewsModel>() {
            override fun areItemsTheSame(oldItem: NewsModel, newItem: NewsModel): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: NewsModel, newItem: NewsModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}