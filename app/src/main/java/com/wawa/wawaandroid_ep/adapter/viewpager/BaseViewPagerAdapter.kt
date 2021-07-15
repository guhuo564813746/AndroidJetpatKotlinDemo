package com.wawa.wawaandroid_ep.adapter.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter

/**
 *作者：create by 张金 on 2021/7/15 15:54
 *邮箱：564813746@qq.com
 */
class BaseViewPagerAdapter(private val fm: FragmentManager,private val fragments: List<Fragment>,private val titles: List<String>) : FragmentPagerAdapter(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return fragments.get(position)
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles.get(position)
    }
}