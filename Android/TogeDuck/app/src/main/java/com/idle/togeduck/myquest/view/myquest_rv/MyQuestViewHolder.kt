package com.idle.togeduck.myquest.view.myquest_rv

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.idle.togeduck.R
import com.idle.togeduck.common.RandomCupcake
import com.idle.togeduck.common.Theme
import com.idle.togeduck.databinding.ItemMyquestBinding
import com.idle.togeduck.myquest.model.MyQuest
import com.idle.togeduck.util.getColor

class MyQuestViewHolder(
    binding: ItemMyquestBinding,
    private var myquestDetail: IMyQuestDetail
) : RecyclerView.ViewHolder(binding.root), View.OnClickListener{

    private val myQuestLayout = binding.myquestMainLayout
    private val cupcake = binding.myquestMainIcon
    private val title = binding.myquestTitle
    private val questKind = binding.myquestKind

    private var myQuest: MyQuest? = null

    init {
        myQuestLayout.setOnClickListener(this)
    }

    fun binding(myQuest: MyQuest, context: Context) {
        this.myQuest = myQuest
        setTheme(myQuest,context)
    }

    fun setTheme(myQuest: MyQuest, context: Context){
        // Layout Theme
        val squareCircleDrawable = ContextCompat.getDrawable(context, R.drawable.shape_square_circle) as GradientDrawable
        squareCircleDrawable.setColor(getColor(context, Theme.theme.sub200))
        squareCircleDrawable.setStroke(0, 0)
        myQuestLayout.background = squareCircleDrawable

        val whiteCircleDrawable = ContextCompat.getDrawable(context, R.drawable.shape_circle) as GradientDrawable
        whiteCircleDrawable.setColor(getColor(context, R.color.white))
        whiteCircleDrawable.setStroke(0, 0)
        cupcake.background = whiteCircleDrawable
        cupcake.setImageDrawable(ContextCompat.getDrawable(context, RandomCupcake.getImage()))

        title.setText(myQuest.title)

        val questCircleDrawable = ContextCompat.getDrawable(context, R.drawable.shape_circle) as GradientDrawable
        questCircleDrawable.setStroke(0, 0)
        when{
            myQuest.title.equals("모집") -> {
                questCircleDrawable.setColor(getColor(context, R.color.green))
            }
            myQuest.title.equals("교환") -> {
                questCircleDrawable.setColor(getColor(context, R.color.yellow))
            }
            else -> {
                questCircleDrawable.setColor(getColor(context, Theme.theme.main500))
            }
        }
        questKind.setText(myQuest.type)
        questKind.background = questCircleDrawable
    }

    override fun onClick(v: View?) {
        when(v){
            myQuestLayout -> {
                myQuest?.let { myquestDetail.myQuestClicked(it) }
            }
        }
    }
}