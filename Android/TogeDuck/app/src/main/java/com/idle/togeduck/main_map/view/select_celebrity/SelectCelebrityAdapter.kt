package com.idle.togeduck.main_map.view.select_celebrity

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.idle.togeduck.databinding.ItemSelectCelebrityBinding
import com.idle.togeduck.favorite.model.Celebrity
import com.idle.togeduck.util.TogeDuckDiffUtil

class SelectCelebrityAdapter(
    private var iSelectCelebrity: ISelectCelebrity,
    private val context: Context
) : ListAdapter<Celebrity, SelectCelebrityViewHolder>(TogeDuckDiffUtil.celebrityDiffUtilCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectCelebrityViewHolder {
        val binding = ItemSelectCelebrityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SelectCelebrityViewHolder(binding, iSelectCelebrity)
    }

    override fun onBindViewHolder(holder: SelectCelebrityViewHolder, position: Int) {
        holder.bind(getItem(position), context)
    }
}