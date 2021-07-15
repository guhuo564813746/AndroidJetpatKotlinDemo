package com.wawa.wawaandroid_ep.fragmentv2

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.apollographql.apollo.BannerListQuery
import com.robotwar.app.BR
import com.robotwar.app.R
import com.robotwar.app.databinding.ChargeFmV2LayBinding
import com.to.aboomy.pager2banner.IndicatorView
import com.wawa.baselib.utils.logutils.LogUtils
import com.wawa.wawaandroid_ep.pay.PayManager
import com.wawa.wawaandroid_ep.MainActivity
import com.wawa.wawaandroid_ep.MainViewModule
import com.wawa.wawaandroid_ep.WawaApp
import com.wawa.wawaandroid_ep.adapter.ImageAdapter
import com.wawa.wawaandroid_ep.base.fragment.BaseFragment
import com.wawa.wawaandroid_ep.fragment.ChargeFragment
import com.wawa.wawaandroid_ep.fragment.ChargeListFragment
import com.wawa.wawaandroid_ep.fragmentv2.viewmodel.ChargeFmV2ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 *作者：create by 张金 on 2021/6/21 10:04
 *邮箱：564813746@qq.com
 */
class ChargeFragmentV2 : BaseFragment<ChargeFmV2LayBinding, ChargeFmV2ViewModel>() {
    val TAG="ChargeFragmentV2"
    var titles = mutableListOf<String>()
    var fragments = mutableListOf<Fragment>()
    lateinit var payManager: PayManager

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): ChargeFmV2ViewModel {
        val chargeFmV2ViewModel: ChargeFmV2ViewModel by viewModels()
        return chargeFmV2ViewModel
    }

    override fun onResume() {
        super.onResume()
        LogUtils.d(TAG,"onResume---")
        (activity as MainActivity).navBottom.visibility= View.VISIBLE
    }

    override fun getLayoutId(): Int {
        return R.layout.charge_fm_v2_lay
    }

    override fun initFragmentView() {
        val backPressCallback=requireActivity().onBackPressedDispatcher.addCallback (this){
            requireActivity().finish()
        }
        backPressCallback.isEnabled

        MainViewModule.mutableLiveuserData?.observe(this, Observer {
            viewModel.coin.set(it?.userAccount()?.coin().toString())
            viewModel.diamond.set("0")
        })
        activity?.let { payManager=
            PayManager(it, WawaApp.apolloClient)
        }
        lifecycle.addObserver(payManager)
        initChargeTab()
        initChargeBanner()
    }

    fun initChargeBanner(){
        val bannerDataSuccess=apolloDataSource.bannerList
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::dealBannerDataSuccess)
        val bannerDataError=apolloDataSource.error
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::dealBannerDataError)
        fragmentDisposible.add(bannerDataSuccess)
        fragmentDisposible.add(bannerDataError)
        apolloDataSource.getBannerList(2)
    }

    private fun dealBannerDataSuccess(bannerList: List<BannerListQuery.BannerList>){
        Log.d(TAG,"dealBannerDataSuccess--"+bannerList.size+bannerList.toString())
        val indicator = IndicatorView(context)
            .setIndicatorColor(Color.DKGRAY)
            .setIndicatorSelectorColor(Color.WHITE)
        val imageAdapter= ImageAdapter(activity,bannerList)
        binding.chargeBanner.setIndicator(indicator).adapter=imageAdapter
    }

    private fun dealBannerDataError(error: Throwable?){

    }

    fun initChargeTab(){
        titles.add("Coin")
//        titles.add("Diamond")
        var mBundle = Bundle()
        mBundle.putInt(ChargeFragment.BUNDLE_PARAMS_GOODS_TYPE, ChargeFragment.GOODS_TYPE_COIN)
        var chargeCoinListFragment= ChargeListFragment()
        var chargeDiamondListFragment= ChargeListFragment()
        chargeCoinListFragment.arguments=mBundle
        mBundle.putInt(ChargeFragment.BUNDLE_PARAMS_GOODS_TYPE, ChargeFragment.GOODS_TYPE_COIN)
        chargeDiamondListFragment.arguments=mBundle
        fragments.add(chargeCoinListFragment)
//        fragments.add(chargeDiamondListFragment)
        binding.viewPager.adapter=ChargeFragmentPagerAdapter(childFragmentManager)
        binding.chargeSlideTab.setupWithViewPager(binding.viewPager)
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