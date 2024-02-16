package com.idle.togeduck.quest.recruit.view.recruit_rv

import com.idle.togeduck.quest.recruit.model.Recruit

interface IQuestRecruit {
    fun joinBtnClicked(questRecruit: Recruit)
    fun removeItem(questRecruit: Recruit)
}