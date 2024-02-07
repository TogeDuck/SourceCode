package com.idle.togeduck.favorite.view.favorite_rv

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.idle.togeduck.databinding.ItemMyFavoriteBinding
import com.idle.togeduck.favorite.model.Celebrity
import com.idle.togeduck.util.TogeDuckDiffUtil

class MyFavoriteAdapter(
    private var iMyFavorite: IMyFavorite,
    private val context: Context
) : ListAdapter<Celebrity, MyFavoriteViewHolder>(TogeDuckDiffUtil.celebrityDiffUtilCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyFavoriteViewHolder {
        val binding = ItemMyFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyFavoriteViewHolder(binding, iMyFavorite)
    }

    override fun onBindViewHolder(holder: MyFavoriteViewHolder, position: Int) {
       holder.bind(getItem(position), context)
    }

}