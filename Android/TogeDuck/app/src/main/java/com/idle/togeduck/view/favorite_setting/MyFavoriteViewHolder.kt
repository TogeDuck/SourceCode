package com.idle.togeduck.view.recyclerview.favorite_setting

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.idle.togeduck.databinding.ItemMyFavoriteBinding
import com.idle.togeduck.model.FavoriteIdol

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

    fun bind(favoriteIdol: FavoriteIdol) {
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
            idolMyFavoriteLinearLayout -> {
                iMyFavorite.myFavoriteRemoveBtnClicked(bindingAdapterPosition)
            }
        }
    }
}