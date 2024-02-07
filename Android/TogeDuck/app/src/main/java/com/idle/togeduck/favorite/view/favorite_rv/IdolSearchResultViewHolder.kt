package com.idle.togeduck.favorite.view.favorite_rv

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.idle.togeduck.R
import com.idle.togeduck.common.ScreenSize
import com.idle.togeduck.common.Theme
import com.idle.togeduck.databinding.ItemIdolSearchResultBinding
import com.idle.togeduck.favorite.model.Celebrity
import com.idle.togeduck.util.DpPxUtil
import com.idle.togeduck.util.getColor

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

    fun binding(celebrity: Celebrity, context: Context, spanCount: Int) {
        setWidth(context, spanCount)

        val drawable = ContextCompat.getDrawable(context, R.drawable.shape_circle) as GradientDrawable

        drawable.setColor(getColor(context, Theme.theme.main100))
        drawable.setStroke(2, getColor(context, Theme.theme.main500))
        imgImageView.background = drawable

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
        nameTextView.setTextColor(getColor(context, Theme.theme.main500))
    }

    private fun setWidth(context: Context, spanCount: Int) {
        val newSize = DpPxUtil.dpToPx(ScreenSize.widthDp - 100 - (spanCount - 1) * 20, context) / spanCount
        val layoutParams = imgImageView.layoutParams as LinearLayout.LayoutParams
        layoutParams.width = newSize
        layoutParams.height = newSize
        imgImageView.layoutParams = layoutParams
    }

    override fun onClick(view: View?) {
        when (view) {
            idolSearchResultLinearLayout -> {
                iIdolSearchResult.idolClicked(bindingAdapterPosition)
            }
        }
    }
}