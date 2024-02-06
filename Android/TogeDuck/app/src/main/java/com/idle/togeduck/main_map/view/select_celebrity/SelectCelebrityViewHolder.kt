package com.idle.togeduck.main_map.view.select_celebrity

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.idle.togeduck.R
import com.idle.togeduck.common.Theme
import com.idle.togeduck.databinding.ItemSelectCelebrityBinding
import com.idle.togeduck.favorite.model.Celebrity
import com.idle.togeduck.util.getColor

class SelectCelebrityViewHolder(
    binding: ItemSelectCelebrityBinding,
    private var iSelectCelebrity: ISelectCelebrity
) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
    private val selectCelebrityLayout = binding.llSelectCelebrity
    private val ivImg = binding.ivImg
    private val tvTeamName = binding.tvTeamName
    private val tvName = binding.tvName

    init {
        selectCelebrityLayout.setOnClickListener(this)
    }

    fun bind(celebrity: Celebrity, context: Context) {
        setTheme(context)

        Glide
            .with(ivImg)
            .load(celebrity.image)
            .thumbnail(
                Glide.with(ivImg).load(celebrity.image).override(50, 50)
            )
            .circleCrop()
            .override(500, 500)
            .into(ivImg)

//        tvTeamName.text = celebrity.teamName
//        tvTeamName.setTextColor(getColor(context, Theme.theme.main500))

        tvName.text = celebrity.nickname
    }

    private fun setTheme(context: Context) {
        val squareCircleDrawable = ContextCompat.getDrawable(context, R.drawable.shape_square_circle) as GradientDrawable
        squareCircleDrawable.setColor(getColor(context, Theme.theme.sub200))
        squareCircleDrawable.setStroke(0, Theme.theme.main500)

        selectCelebrityLayout.background = squareCircleDrawable

        tvName.setTextColor(getColor(context, Theme.theme.main500))
    }

    override fun onClick(view: View?) {
        when (view) {
            selectCelebrityLayout -> {
                iSelectCelebrity.celebrityClicked(bindingAdapterPosition)
            }
        }
    }
}