package com.wawa.wawaandroid_ep.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.google.android.material.tabs.TabLayout
import com.wawa.wawaandroid_ep.R
import com.wawa.wawaandroid_ep.base.fragment.BaseFragment
import com.wawa.wawaandroid_ep.databinding.FragmentChargeLayBinding
import com.wawa.wawaandroid_ep.fragment.viewmodule.ChargeFragmentViewModel

/**
 *作者：create by 张金 on 2021/1/14 11:29
 *邮箱：564813746@qq.com
 */
class ChargeFragment : BaseFragment<FragmentChargeLayBinding>(){
    private lateinit var chargeTabLay: TabLayout
    var titles = mutableListOf<String>()
    var fragments = mutableListOf<Fragment>()

    private val chaegeViewModel: ChargeFragmentViewModel by viewModels()
    override fun getLayoutId(): Int {
        return R.layout.fragment_charge_lay
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chaegeViewModel.loadChargeList()
    }

    override fun initFragmentView() {
        initChargeTab()
    }

    fun initChargeTab(){
        titles.add("Coin")
        titles.add("Diamond")
        fragments.add(ChargeListFragment())
        fragments.add(ChargeListFragment())
        binding.viewPager.adapter=ChargeFragmentPagerAdapter(childFragmentManager)
        chargeTabLay=binding.tabLay.findViewById(R.id.main_slide_tab) as TabLayout
        chargeTabLay.setupWithViewPager(binding.viewPager)
    }

    private inner class ChargeFragmentPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager){
        override fun getItem(position: Int): Fragment {
            return fragments?.get(position)
        }

        override fun getCount(): Int {
            return fragments?.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titles?.get(position)
        }
    }

}