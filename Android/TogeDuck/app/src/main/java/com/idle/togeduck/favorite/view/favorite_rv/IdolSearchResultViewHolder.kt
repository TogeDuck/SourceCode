package com.idle.togeduck.favorite.view.favorite_rv

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.idle.togeduck.R
import com.idle.togeduck.databinding.ItemIdolSearchResultBinding
import com.idle.togeduck.util.Theme

class IdolSearchResultViewHolder(
    binding: ItemIdolSearchResultBinding,
    private val iIdolSearchResult: IIdolSearchResult,
) : RecyclerView.ViewHolder(binding.root),
    View.OnClickListener {
    private val idolSearchResultLinearLayout = binding.llIdolSearchResult
    private val imgImageView = binding.ivImg
    private val nameTextView = binding.tvName

    init {
        idolSearchResultLinearLayout.setOnClickListener(this)
    }

    fun binding(favoriteIdol: FavoriteIdol, context: Context) {
        val drawable = ContextCompat.getDrawable(context, R.drawable.shape_circle) as GradientDrawable

        drawable.setColor(ContextCompat.getColor(context, Theme.theme.main100))
        drawable.setStroke(2, ContextCompat.getColor(context, Theme.theme.main500))
        imgImageView.background = drawable

        Glide
            .with(imgImageView)
            .load(favoriteIdol.imgUrl)
            .thumbnail(
                Glide.with(imgImageView).load(favoriteIdol.imgUrl).override(50, 50)
            )
            .circleCrop()
            .override(500, 500)
            .into(imgImageView)

        nameTextView.text = favoriteIdol.name
        nameTextView.setTextColor(ContextCompat.getColor(context, Theme.theme.main500))
    }

    override fun onClick(view: View?) {
        when (view) {
            idolSearchResultLinearLayout -> {
                iIdolSearchResult.idolClicked(bindingAdapterPosition)
            }
        }
    }
}