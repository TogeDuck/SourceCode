package com.idle.togeduck.quest.exchange.view.my_exchange_rv

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.idle.togeduck.R
import com.idle.togeduck.databinding.ItemQuestExchangeMyBinding
import com.idle.togeduck.quest.exchange.ExchangeViewModel
import com.idle.togeduck.quest.exchange.model.Exchange
import com.idle.togeduck.util.DpPxUtil

class MyExchangeViewHolder (
    binding: ItemQuestExchangeMyBinding,
    private var iMyExchangeDetail: IMyExchangeDetail
) : RecyclerView.ViewHolder(binding.root), View.OnClickListener{
    private val questExchangeMyLayout = binding.questExchangeMyItem
    private val image = binding.questExchangeMyImage
    private val overlay = binding.questExchangeMyOverlay
    private val checked = binding.questExchangeMyIcon

    private var myExchange: Exchange? = null

    init {
        questExchangeMyLayout.setOnClickListener(this)
    }

    fun binding(myExchange: Exchange, context: Context, isSelected: Boolean) {
        this.myExchange = myExchange
        setImage(myExchange, context)
        setTheme(myExchange, context, isSelected)
    }

    override fun onClick(v: View?) {
        when(v){
            questExchangeMyLayout-> {
                myExchange?.let { iMyExchangeDetail.myExchangeItemClicked(it) }
            }
        }
    }

    private fun setImage(myExchange: Exchange, context: Context){
        Glide
            .with(image)
            .load(myExchange.image)
            .thumbnail(
                Glide.with(image).load(myExchange.image).override(200,200)
            )
            .transform(CenterCrop(), RoundedCorners(DpPxUtil.dpToPx(15, context)))
            .override(500, 500)
            .into(image)
    }

    private fun setTheme(myExchange: Exchange, context: Context, isSelected: Boolean){
        val roundSmall = ContextCompat.getDrawable(context, R.drawable.shape_all_round_10) as GradientDrawable
        roundSmall.setColor(ContextCompat.getColor(context, R.color.white_transparent))
        overlay.background = roundSmall
        if(!isSelected){
            overlay.visibility = View.GONE
            checked.visibility = View.GONE
        }
        else{
            overlay.visibility = View.VISIBLE
            checked.visibility = View.VISIBLE
        }
    }

}