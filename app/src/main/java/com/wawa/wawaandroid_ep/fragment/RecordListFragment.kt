package com.wawa.wawaandroid_ep.fragment

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.robotwar.app.BR
import com.robotwar.app.R
import com.robotwar.app.databinding.UserRecordLayBinding
import com.wawa.wawaandroid_ep.base.fragment.BaseFragment
import com.wawa.wawaandroid_ep.fragment.viewmodule.RecordListFragmentViewModel

/**
 *作者：create by 张金 on 2021/3/2 16:59
 *邮箱：564813746@qq.com
 */
class RecordListFragment : BaseFragment<UserRecordLayBinding,RecordListFragmentViewModel>(){
    private var mTitles: List<String> = listOf()
    private var mFragments: List<Fragment> = listOf()

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
        var title=binding.viewTitle.findViewById<TextView>(R.id.title)
        var imBack=binding.viewTitle.findViewById<ImageView>(R.id.im_back)
        imBack.setOnClickListener {
            findNavController().popBackStack()
        }
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
            )/*,getString(R.string.tx_diamonRecord)*/,getString(R.string.tx_rechargeRecord))
        for (i in 0..2){
            when(i){
                0 ->{
                    var gameRecordFragment=RecordListItemFragment()
                    var bundle=Bundle()
                    bundle.putString(RecordListItemFragment.TAG,RecordListItemFragment.GAME_RECORD)
                    gameRecordFragment.arguments=bundle
                    mFragments+=gameRecordFragment
                }
                1->{
                    var coinRecordFragment=RecordListItemFragment()
                    var bundle=Bundle()
                    bundle.putString(RecordListItemFragment.TAG,RecordListItemFragment.COINS_RECORD)
                    coinRecordFragment.arguments=bundle
                    mFragments+=coinRecordFragment
                }
                2 ->{
                    var chargeRecordFragment=RecordListItemFragment()
                    var bundle=Bundle()
                    bundle.putString(RecordListItemFragment.TAG,RecordListItemFragment.CHARGE_RECORD)
                    chargeRecordFragment.arguments=bundle
                    mFragments+=chargeRecordFragment
                }
            }
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