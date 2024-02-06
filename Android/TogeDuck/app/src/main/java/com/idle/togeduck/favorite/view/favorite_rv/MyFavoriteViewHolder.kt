package com.idle.togeduck.favorite.view.favorite_rv

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.idle.togeduck.R
import com.idle.togeduck.databinding.ItemMyFavoriteBinding
import com.idle.togeduck.common.Theme
import com.idle.togeduck.favorite.model.Celebrity
import com.idle.togeduck.util.getColor
import com.idle.togeduck.util.toAlpha

class MyFavoriteViewHolder(
    binding: ItemMyFavoriteBinding,
    private var iMyFavorite: IMyFavorite,
) : RecyclerView.ViewHolder(binding.root),
    View.OnClickListener {
    private val idolMyFavoriteLinearLayout = binding.llMyFavorite
    private val removeBtn = binding.ivRemoveBtn
    private val imgImageView = binding.ivImg
    private val nameTextView = binding.tvName

    init {
        idolMyFavoriteLinearLayout.setOnClickListener(this)
    }

    fun bind(celebrity: Celebrity, context: Context) {
        setTheme(context)

        Glide
            .with(imgImageView)
            .load(celebrity.image)
            .thumbnail(
                Glide.with(imgImageView).load(celebrity.image).override(50, 50)
            )
            .circleCrop()
            .override(500, 500)
            .into(imgImageView)

        nameTextView.text = celebrity.nickname
        nameTextView.setTextColor(ContextCompat.getColor(context, Theme.theme.main500))

        removeBtn.setColorFilter(ContextCompat.getColor(context, Theme.theme.main500))
    }

    private fun setTheme(context: Context) {
        val circleDrawable = ContextCompat.getDrawable(context, R.drawable.shape_circle) as GradientDrawable
        circleDrawable.setColor(ContextCompat.getColor(context, Theme.theme.main100))
        circleDrawable.setStroke(2, ContextCompat.getColor(context, Theme.theme.main500))
        imgImageView.background = circleDrawable

        val allCornerDrawable = ContextCompat.getDrawable(context, R.drawable.shape_all_round_5) as GradientDrawable
        allCornerDrawable.setColor(ContextCompat.getColor(context, Theme.theme.main100))
        allCornerDrawable.setStroke(0, getColor(context, Theme.theme.main500))
        allCornerDrawable.alpha = 0.4.toAlpha()
        idolMyFavoriteLinearLayout.background = allCornerDrawable
    }

    override fun onClick(view: View?) {
        when (view) {
            idolMyFavoriteLinearLayout -> {
                iMyFavorite.myFavoriteRemoveBtnClicked(bindingAdapterPosition)
            }
        }
    }
}