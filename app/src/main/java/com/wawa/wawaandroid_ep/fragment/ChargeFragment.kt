package com.wawa.wawaandroid_ep.fragment

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayout
import com.robotwar.app.BR
import com.robotwar.app.R
import com.robotwar.app.databinding.FragmentChargeLayBinding
import com.wawa.baselib.utils.logutils.LogUtils
import com.wawa.baselib.utils.pay.PayManager
import com.wawa.wawaandroid_ep.*
import com.wawa.wawaandroid_ep.base.fragment.BaseFragment
import com.wawa.wawaandroid_ep.fragment.viewmodule.ChargeFragmentViewModel

/**
 *作者：create by 张金 on 2021/1/14 11:29
 *邮箱：564813746@qq.com
 */
class ChargeFragment : BaseFragment<FragmentChargeLayBinding,ChargeFragmentViewModel>(){
    private val TAG = "ChargeFragment"
    companion object{
        val GOODS_TYPE_COIN = 0
        val GOODS_TYPE_DIAMOND = 1
        val BUNDLE_PARAMS_GOODS_TYPE = "BUNDLE_PARAMS_GOODS_TYPE"
    }
    private lateinit var chargeTabLay: TabLayout
    var titles = mutableListOf<String>()
    var fragments = mutableListOf<Fragment>()
    lateinit var payManager: PayManager
    override fun getLayoutId(): Int {
        return R.layout.fragment_charge_lay
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadChargeList()
    }

    override fun onResume() {
        super.onResume()
        LogUtils.d(TAG,"onResume---")
        (activity as MainActivity).navBottom.visibility=View.VISIBLE
    }

    override fun initFragmentView() {
        val backPressCallback=requireActivity().onBackPressedDispatcher.addCallback (this){
            requireActivity().finish()
        }
        backPressCallback.isEnabled

        MainViewModule.mutableLiveuserData?.observe(this, Observer {
            viewModel.coin.set(it?.userAccount()?.fragments()?.userAcountFragment()?.coin().toString())
            viewModel.diamond.set("0")
        })
        activity?.let { payManager=PayManager(it,WawaApp.apolloClient) }
        lifecycle.addObserver(payManager)
        initChargeTab()
    }

    fun initChargeTab(){
        titles.add("Coin")
//        titles.add("Diamond")
        var mBundle = Bundle()
        mBundle.putInt(BUNDLE_PARAMS_GOODS_TYPE, GOODS_TYPE_COIN)
        var chargeCoinListFragment=ChargeListFragment()
        var chargeDiamondListFragment=ChargeListFragment()
        chargeCoinListFragment.arguments=mBundle
        mBundle.putInt(BUNDLE_PARAMS_GOODS_TYPE, GOODS_TYPE_COIN)
        chargeDiamondListFragment.arguments=mBundle
        fragments.add(chargeCoinListFragment)
//        fragments.add(chargeDiamondListFragment)
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

    override fun onDestroy() {
        super.onDestroy()
        LogUtils.d(TAG,"onDestroy--")
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): ChargeFragmentViewModel {
        val chaegeViewModel: ChargeFragmentViewModel by viewModels()
        return chaegeViewModel
    }

}