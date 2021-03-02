package com.wawa.wawaandroid_ep.fragment

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollographql.apollo.UserQuery
import com.wawa.baselib.utils.apollonet.BaseDataSource
import com.wawa.wawaandroid_ep.BR
import com.wawa.wawaandroid_ep.R
import com.wawa.wawaandroid_ep.WawaApp
import com.wawa.wawaandroid_ep.adapter.MineFragmentListAdapter
import com.wawa.wawaandroid_ep.base.fragment.BaseFragment
import com.wawa.wawaandroid_ep.bean.mine.MineListBean
import com.wawa.wawaandroid_ep.databinding.FragmentMineLayBinding
import com.wawa.wawaandroid_ep.fragment.viewmodule.MineFragmentViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 *作者：create by 张金 on 2021/1/14 11:33
 *邮箱：564813746@qq.com
 */
class MineFragment : BaseFragment<FragmentMineLayBinding,MineFragmentViewModel>(){
    private val compositeDisposable = CompositeDisposable()
    private val dataSource: BaseDataSource by lazy {
        (activity?.application as WawaApp).getDataSource(WawaApp.ServiceTypes.COROUTINES)
    }
    override fun getLayoutId(): Int {
        return R.layout.fragment_mine_lay
    }

    override fun initFragmentView() {
        //initview
        binding.lvMineset.layoutManager=LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
        val mineChargeBean=MineListBean()
        mineChargeBean.title="账单"
        mineChargeBean.itemImSrc=R.mipmap.game_icon_setting
        val mineSetBean=MineListBean()
        mineSetBean.title="设置"
        mineSetBean.itemImSrc=R.mipmap.game_icon_setting
        var mineList= listOf<MineListBean>(mineChargeBean,mineSetBean)
        binding.lvMineset.adapter= activity?.let { MineFragmentListAdapter(it,mineList) }

        //initdata
        initMineData()
    }

    fun initMineData(){
        val mineUserDataSuccessDp=dataSource.userData
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleSuccessUserInfo)
        val mineUserDataErrorDp=dataSource.error
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleErrorUserInfo)
        compositeDisposable.add(mineUserDataSuccessDp)
        compositeDisposable.add(mineUserDataErrorDp)
        dataSource.getUserData()
    }

    fun handleSuccessUserInfo(userData: UserQuery.User){
        if (userData != null){
            viewModel.coins.set(userData.fragments().userFragment()?.userAccount()?.fragments()?.userAcountFragment()?.coin().toString())
            viewModel.scores.set(userData.fragments().userFragment()?.userAccount()?.fragments()?.userAcountFragment()?.point().toString())
            viewModel.diamons.set(userData.fragments().userFragment()?.userAccount()?.fragments()?.userAcountFragment()?.coin().toString())
            viewModel.userId.set(userData.fragments().userFragment().userId())
            viewModel.userLevel.set(userData.fragments().userFragment().name())
            viewModel.userName.set(userData.fragments().userFragment().nickName())
        }
    }

    fun handleErrorUserInfo(e: Throwable?){

    }

    fun goSettingPage(){

    }

    fun goProfilePage(){

    }

    fun goLevelPage(){

    }

    fun goRecordListPage(){

    }
    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): MineFragmentViewModel {
        val mineFragmentViewModel: MineFragmentViewModel by viewModels()
        return mineFragmentViewModel
    }
}