package com.idle.togeduck.view.recyclerview.favorite_setting

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.idle.togeduck.R
import com.idle.togeduck.databinding.ItemIdolSearchResultBinding
import com.idle.togeduck.model.FavoriteIdol

class IdolSearchResultViewHolder(
    binding: ItemIdolSearchResultBinding,
    private var iIdolSearchResult: IIdolSearchResult,
) : RecyclerView.ViewHolder(binding.root),
    View.OnClickListener {
    private val idolSearchResultLinearLayout = binding.llIdolSearchResult
    private val imgImageView = binding.ivImg
    private val nameTextView = binding.tvName

    init {
        idolSearchResultLinearLayout.setOnClickListener(this)
    }

    fun binding(favoriteIdol: FavoriteIdol) {
        Glide
            .with(imgImageView)
            .load(favoriteIdol.imgUrl)
            .thumbnail(
                Glide.with(imgImageView).load(favoriteIdol.imgUrl).override(10, 10)
            )
            .circleCrop()
            .override(200, 200)
            .into(imgImageView)

        nameTextView.text = favoriteIdol.name
    }

    override fun onClick(view: View?) {
        when (view) {
            idolSearchResultLinearLayout -> {
                iIdolSearchResult.idolClicked(bindingAdapterPosition)
            }
        }
    }
}