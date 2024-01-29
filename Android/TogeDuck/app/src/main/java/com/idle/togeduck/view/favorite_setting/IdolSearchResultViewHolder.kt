package com.idle.togeduck.view.favorite_setting

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.idle.togeduck.R
import com.idle.togeduck.databinding.ItemIdolSearchResultBinding
import com.idle.togeduck.model.FavoriteIdol
import com.idle.togeduck.util.Theme
import com.idle.togeduck.view.favorite_setting.IIdolSearchResult

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

    fun binding(favoriteIdol: FavoriteIdol, context: Context) {
        val drawable = ContextCompat.getDrawable(context, R.drawable.shape_circle) as GradientDrawable

        drawable.setColor(ContextCompat.getColor(context, Theme.theme.main100))
        drawable.setStroke(2, ContextCompat.getColor(context, Theme.theme.main500))
        imgImageView.background = drawable

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