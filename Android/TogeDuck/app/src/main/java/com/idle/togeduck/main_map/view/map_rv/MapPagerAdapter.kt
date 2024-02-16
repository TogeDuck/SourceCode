package com.idle.togeduck.main_map.view.map_rv

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.idle.togeduck.event.view.detail.EventDetailFragment
import com.idle.togeduck.event.view.list.EventListFragment
import com.idle.togeduck.history.view.HistoryDetailFragment
import com.idle.togeduck.history.view.HistoryFragment
import com.idle.togeduck.myquest.view.MyQuestFragment
import com.idle.togeduck.quest.view.QuestFragment

class MapPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 6
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> QuestFragment()
            1 -> EventListFragment()
            2 -> EventDetailFragment()
            3 -> MyQuestFragment()
            4 -> HistoryFragment()
            else -> HistoryDetailFragment()
        }
    }
}