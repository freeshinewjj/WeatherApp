package com.jay.weatherapp.common.util

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

fun ImageView.loadFromUrl(url: String) {
    Glide.with(this.context)
        .load(url.ifEmpty { null })
        .centerCrop()
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}