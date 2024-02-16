package com.idle.togeduck.favorite.view.favorite_rv

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.idle.togeduck.databinding.ItemIdolSearchResultBinding
import com.idle.togeduck.favorite.model.Celebrity
import com.idle.togeduck.util.TogeDuckDiffUtil

class IdolSearchResultAdapter(
    private var iIdolSearchResult: IIdolSearchResult,
    private val context: Context,
    private val spanCount: Int
) : ListAdapter<Celebrity, IdolSearchResultViewHolder>(TogeDuckDiffUtil.celebrityDiffUtilCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IdolSearchResultViewHolder {
        val binding = ItemIdolSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IdolSearchResultViewHolder(binding, iIdolSearchResult)
    }

    override fun onBindViewHolder(holder: IdolSearchResultViewHolder, position: Int) {
        holder.binding(getItem(position), context, spanCount)
    }

}
