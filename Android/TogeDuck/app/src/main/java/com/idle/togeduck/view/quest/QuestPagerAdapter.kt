package com.idle.togeduck.view.quest

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.idle.togeduck.R
import com.idle.togeduck.view.EventListFragment
import com.idle.togeduck.view.MyRecordFragment
import com.idle.togeduck.view.QuestRecruitFragment
import com.idle.togeduck.view.QuestShareFragment

class QuestPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> QuestShareFragment()
            1 -> QuestShareFragment()
            2 -> QuestRecruitFragment()
            3 -> QuestRecruitFragment()
            else -> QuestRecruitFragment()
        }
    }

}