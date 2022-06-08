package com.junyoung.searchmovie.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.junyoung.searchmovie.R
import com.junyoung.searchmovie.databinding.ItemMovieBinding
import com.junyoung.searchmovie.databinding.ItemSearchWordBinding

class RecentSearchAdapter(private val clickListener: WordClickListener)
    : ListAdapter<String, RecentSearchAdapter.RecentSearchViewHolder>(WordDiffUtil) {

    private lateinit var binding: ItemSearchWordBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentSearchViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_search_word, parent, false)
        return RecentSearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecentSearchViewHolder, position: Int) {
        val word = getItem(position)
        holder.bind(word, clickListener)
    }

    class WordClickListener(val clickListener: (word: String) -> Unit) {
        fun onWordClick(word: String) = clickListener(word)
    }

    class RecentSearchViewHolder(val binding: ItemSearchWordBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(word: String, clickListener: WordClickListener) {
            binding.word = word
            binding.click = clickListener
            binding.executePendingBindings()
        }
    }

    companion object WordDiffUtil: DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }

}