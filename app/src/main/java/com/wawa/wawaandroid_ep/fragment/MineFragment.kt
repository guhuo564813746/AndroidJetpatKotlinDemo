package com.wawa.wawaandroid_ep.fragment

import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollographql.apollo.UserQuery
import com.robotwar.app.BR
import com.robotwar.app.R
import com.robotwar.app.databinding.FragmentMineLayBinding
import com.wawa.baselib.utils.apollonet.BaseDataSource
import com.wawa.baselib.utils.glide.loader.ImageLoader
import com.wawa.baselib.utils.glide.utils.ImageUtil
import com.wawa.baselib.utils.logutils.LogUtils
import com.wawa.wawaandroid_ep.*
import com.wawa.wawaandroid_ep.adapter.MineFragmentListAdapter
import com.wawa.wawaandroid_ep.base.fragment.BaseFragment
import com.wawa.wawaandroid_ep.bean.mine.MineListBean
import com.wawa.wawaandroid_ep.fragment.viewmodule.MineFragmentViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

/**
 *作者：create by 张金 on 2021/1/14 11:33
 *邮箱：564813746@qq.com
 */
class MineFragment : BaseFragment<FragmentMineLayBinding,MineFragmentViewModel>(),View.OnClickListener{
    private val TAG="MineFragment"
    private val compositeDisposable = CompositeDisposable()
    private val dataSource: BaseDataSource by lazy {
        (activity?.application as WawaApp).getDataSource(WawaApp.ServiceTypes.COROUTINES)
    }
    override fun getLayoutId(): Int {
        return R.layout.fragment_mine_lay
    }

    override fun onResume() {
        super.onResume()
        LogUtils.d(TAG,"onResume---")
        (activity as MainActivity).navBottom.visibility=View.VISIBLE
    }


    override fun initFragmentView() {
        LogUtils.d(TAG,"initFragmentView---")
        val backPressCallback=requireActivity().onBackPressedDispatcher.addCallback (this){
            requireActivity().finish()
        }
        backPressCallback.isEnabled
        //initview
        binding.lvMineset.layoutManager=LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
        val mineChargeBean=MineListBean()
        mineChargeBean.title="账单"
        mineChargeBean.itemImSrc=R.mipmap.icon_zhangdan
        val mineSetBean=MineListBean()
        mineSetBean.title="设置"
        mineSetBean.itemImSrc=R.mipmap.setting
        var mineList= listOf<MineListBean>(mineChargeBean,mineSetBean)
        binding.lvMineset.adapter= MineFragmentListAdapter(this,mineList)
        initViewListener()
        //initdata
        initMineData()
    }

    fun initViewListener(){
        binding.imBarSet1.setOnClickListener(this)
        binding.rlHead.setOnClickListener(this)
        binding.tvNickname.setOnClickListener(this)
        binding.tvUserid.setOnClickListener(this)
        binding.tvLevelName.setOnClickListener(this)
        binding.editUserinfo.setOnClickListener(this)
        binding.imUserlevel.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.im_bar_set_1 -> goSettingPage(v)
            R.id.rl_Head,R.id.tv_nickname
                ,R.id.tv_userid,R.id.edit_userinfo -> goProfilePage(v)
            R.id.tv_level_name,R.id.im_userlevel -> goLevelPage(v)
        }
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
            viewModel.userId.set(userData?.userId())
            viewModel.userLevel.set(userData?.name())
            viewModel.userName.set(userData?.nickName())
        }
    }

    fun handleErrorUserInfo(e: Throwable?){

    }

    fun goSettingPage(view: View){
        (activity as MainActivity).navBottom.visibility=View.GONE
        findNavController().navigate(R.id.settingFragment)
    }

    fun goProfilePage(view: View){
        (activity as MainActivity).navBottom.visibility=View.GONE
        findNavController().navigate(R.id.profileFragment)
    }

    fun goLevelPage(view: View){
        (activity as MainActivity).navBottom.visibility=View.GONE
//        findNavController().navigate(R.id.)
    }

    fun goRecordListPage(view: View){
        (activity as MainActivity).navBottom.visibility=View.GONE
        findNavController().navigate(R.id.userRecordFragment)
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): MineFragmentViewModel {
        val mineFragmentViewModel: MineFragmentViewModel by viewModels()
        return mineFragmentViewModel
    }
}