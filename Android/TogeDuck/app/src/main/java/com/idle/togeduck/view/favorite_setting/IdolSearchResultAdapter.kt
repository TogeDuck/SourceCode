package com.idle.togeduck.view.favorite_setting

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.idle.togeduck.databinding.ItemIdolSearchResultBinding
import com.idle.togeduck.model.FavoriteIdol
import com.idle.togeduck.util.TogeDuckDiffUtil
import com.idle.togeduck.view.favorite_setting.IIdolSearchResult
import com.idle.togeduck.view.favorite_setting.IdolSearchResultViewHolder

class IdolSearchResultAdapter(
    private var iIdolSearchResult: IIdolSearchResult,
    private val context: Context
) : ListAdapter<FavoriteIdol, IdolSearchResultViewHolder>(TogeDuckDiffUtil.favoriteIdolDiffUtilItemCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IdolSearchResultViewHolder {
        val binding = ItemIdolSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IdolSearchResultViewHolder(binding, iIdolSearchResult)
    }

    override fun onBindViewHolder(holder: IdolSearchResultViewHolder, position: Int) {
        holder.binding(getItem(position), context)
    }

}