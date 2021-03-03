package com.wawa.wawaandroid_ep.fragment

import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayout
import com.wawa.wawaandroid_ep.BR
import com.wawa.wawaandroid_ep.R
import com.wawa.wawaandroid_ep.base.fragment.BaseFragment
import com.wawa.wawaandroid_ep.databinding.UserRecordLayBinding
import com.wawa.wawaandroid_ep.fragment.viewmodule.RecordListFragmentViewModel

/**
 *作者：create by 张金 on 2021/3/2 16:59
 *邮箱：564813746@qq.com
 */
class RecordListFragment : BaseFragment<UserRecordLayBinding,RecordListFragmentViewModel>(){
    private var mTitles: List<String> = listOf()
    private val mFragments: List<Fragment> = listOf()

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): RecordListFragmentViewModel {
        val recordListFragmentViewModel: RecordListFragmentViewModel by viewModels()
        return recordListFragmentViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.user_record_lay
    }

    override fun initFragmentView() {
        initData()
        val title=binding.viewTitle.findViewById<TextView>(R.id.title)
        title.setText(getString(R.string.tv_myorder))
        val slideTab=binding.viewSliding.findViewById<TabLayout>(R.id.main_slide_tab)
        binding.vpUserRecord.adapter=RecordPageAdapter(childFragmentManager)
        slideTab.tabMode=TabLayout.MODE_FIXED
        slideTab.setupWithViewPager(binding.vpUserRecord)
    }

    fun initData(){
        mTitles= listOf(getString(R.string.tx_game_record),
            java.lang.String.format(
                resources.getString(R.string.tx_coinsRecord),
                "游戏币"
            ),getString(R.string.tx_diamonRecord),getString(R.string.tx_rechargeRecord))
        for (i in 0..3){

        }
    }

    private inner class RecordPageAdapter(manager:FragmentManager) : FragmentPagerAdapter(manager){
        override fun getItem(position: Int): Fragment {
            return mFragments.get(position)
        }

        override fun getCount(): Int {
            return mFragments.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mTitles.get(position)
        }
    }

}