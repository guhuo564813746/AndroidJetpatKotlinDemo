package com.wawa.wawaandroid_ep.fragmentv2

import android.util.Log
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollographql.apollo.UserQuery
import com.apollographql.apollo.fragment.PageOptionFragment
import com.robotwar.app.BR
import com.robotwar.app.R
import com.robotwar.app.databinding.MineFmV2LayBinding
import com.wawa.baselib.utils.baseadapter.imp.ArrayListAdapter
import com.wawa.baselib.utils.glide.loader.ImageLoader
import com.wawa.baselib.utils.glide.utils.ImageUtil
import com.wawa.baselib.utils.logutils.LogUtils
import com.wawa.wawaandroid_ep.MainActivity
import com.wawa.wawaandroid_ep.MainViewModule
import com.wawa.wawaandroid_ep.adapter.viewmodel.AppMenuListViewModel
import com.wawa.wawaandroid_ep.base.fragment.BaseFragment
import com.wawa.wawaandroid_ep.fragmentv2.viewmodel.MineFmV2ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

/**
 *作者：create by 张金 on 2021/6/21 10:02
 *邮箱：564813746@qq.com
 */
class MineFragmentV2 : BaseFragment<MineFmV2LayBinding,MineFmV2ViewModel>(){
    val TAG="MineFragmentV2"
    var appMenuList1: List<PageOptionFragment.AppMenu> = mutableListOf()
    var appMenuList2: List<PageOptionFragment.AppMenu> = mutableListOf()
    var appMenuList3: List<PageOptionFragment.AppMenu> = mutableListOf()
    val appMenuAdapter= ArrayListAdapter<List<PageOptionFragment.AppMenu>>()
    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun onResume() {
        super.onResume()
        LogUtils.d(TAG,"onResume---")
        (activity as MainActivity).navBottom.visibility= View.VISIBLE
    }

    override fun initViewModel(): MineFmV2ViewModel {
        val mineFmV2ViewModel: MineFmV2ViewModel by viewModels()
        return mineFmV2ViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.mine_fm_v2_lay
    }

    override fun initFragmentView() {
        LogUtils.d(TAG,"initFragmentView---")
        val backPressCallback=requireActivity().onBackPressedDispatcher.addCallback (this){
            requireActivity().finish()
        }
        backPressCallback.isEnabled
        initMineData()
        initAppMenu()
    }
    //初始化菜单列表
    fun initAppMenu(){
        val manager=LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
        binding.lvMineset.bindAdapter(appMenuAdapter,manager)
        MainViewModule.configData?.page()?.fragments()?.pageOptionFragment()?.appMenu()?.let {
            if (it.size <=3){
                appMenuList1=it
            }else if (it.size > 3 && it.size <=5){
                appMenuList1=it.subList(0,2)
                appMenuList2=it.subList(3,it.size-1)
            }else if(it.size > 5){
                appMenuList1=it.subList(0,2)
                appMenuList2=it.subList(3,4)
                appMenuList3=it.subList(5,it.size-1)
            }
            if (appMenuList1.size > 0){
                val appMenuListViewModel= AppMenuListViewModel()
                appMenuListViewModel.model=appMenuList1
                appMenuAdapter.add(0,appMenuListViewModel)
            }
            if (appMenuList2.size > 0){
                val appMenuListViewModel= AppMenuListViewModel()
                appMenuListViewModel.model=appMenuList2
                appMenuAdapter.add(1,appMenuListViewModel)
            }
            if (appMenuList3.size > 0){
                val appMenuListViewModel= AppMenuListViewModel()
                appMenuListViewModel.model=appMenuList3
                appMenuAdapter.add(2,appMenuListViewModel)
            }

        }

    }
    //初始化用户数据
    fun initMineData(){
        val mineUserDataSuccessDp=apolloDataSource.userData
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleSuccessUserInfo)
        val mineUserDataErrorDp=apolloDataSource.error
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleErrorUserInfo)
        fragmentDisposible.add(mineUserDataSuccessDp)
        fragmentDisposible.add(mineUserDataErrorDp)
        apolloDataSource.getUserData()
    }

    fun handleErrorUserInfo(e: Throwable?){

    }

    fun handleSuccessUserInfo(userData: UserQuery.User){
        Log.d(TAG,"handleSuccessUserInfo--")
        if (userData != null){
            MainViewModule.mutableLiveuserData.value=userData
            MainViewModule.userData=userData
            ImageLoader.with(activity)
                .url(userData?.avatarThumb())
//                .placeHolder(R.mipmap.ic_launcher)
                .rectRoundCorner(ImageUtil.dip2px(30f), RoundedCornersTransformation.CornerType.ALL)
                .into(binding.imHead)
            viewModel.coins.set(userData?.userAccount()?.coin().toString())
            viewModel.scores.set(userData?.userAccount()?.point().toString())
            viewModel.diamons.set("0")
            viewModel.userId.set("ID: ${userData?.userId()}")
            viewModel.userLevel.set(userData?.name())
            viewModel.userName.set(userData?.nickName())
        }
    }
}