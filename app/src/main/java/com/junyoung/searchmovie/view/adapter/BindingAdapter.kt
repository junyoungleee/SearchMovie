package com.junyoung.searchmovie.view.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.junyoung.searchmovie.R


@BindingAdapter("loadImage")
fun loadImage(imageView: ImageView, url: String?) {
    Glide.with(imageView)
        .load(url)
        .fallback(R.drawable.ic_launcher_background)
        .into(imageView)
}


