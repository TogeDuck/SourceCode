package com.idle.togeduck.quest.view.quest_rv

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.idle.togeduck.quest.exchange.view.QuestExchangeFragment
import com.idle.togeduck.quest.recruit.view.QuestRecruitFragment
import com.idle.togeduck.quest.share.view.QuestShareFragment
import com.idle.togeduck.quest.talk.view.QuestTalkFragment

class QuestPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> QuestExchangeFragment()
            1 -> QuestShareFragment()
            2 -> QuestRecruitFragment()
            else -> QuestTalkFragment()
        }
    }

}