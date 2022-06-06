package com.junyoung.searchmovie.view.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.junyoung.searchmovie.R
import com.junyoung.searchmovie.databinding.ItemLoadStateFooterBinding

class MovieLoadStateViewHolder(
    private val binding: ItemLoadStateFooterBinding, retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.btnRetry.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            binding.tvLoadErrorMsg.text = loadState.error.localizedMessage
        }
        binding.btnRetry.isVisible = loadState is LoadState.Error
        binding.tvLoadErrorMsg.isVisible = loadState is LoadState.Error
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): MovieLoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_load_state_footer, parent, false)
            val binding = ItemLoadStateFooterBinding.bind(view)
            return MovieLoadStateViewHolder(binding, retry)
        }
    }
}