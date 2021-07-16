package com.wawa.wawaandroid_ep.fragmentv2

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.activity.addCallback
import androidx.databinding.ObservableArrayList
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollographql.apollo.BannerListQuery
import com.apollographql.apollo.ConfigDataQuery
import com.apollographql.apollo.RoomCategoryListQuery
import com.apollographql.apollo.fragment.PageOptionFragment
import com.blankj.utilcode.util.BarUtils
import com.google.android.material.tabs.TabLayout
import com.robotwar.app.BR
import com.robotwar.app.R
import com.robotwar.app.databinding.MainFmV2LayBinding
import com.to.aboomy.pager2banner.IndicatorView
import com.to.aboomy.pager2banner.ScaleInTransformer
import com.wawa.baselib.utils.apollonet.BaseDataSource
import com.wawa.baselib.utils.baseadapter.imp.ArrayListAdapter
import com.wawa.baselib.utils.baseadapter.imp.ArrayListViewModel
import com.wawa.baselib.utils.glide.loader.ImageLoader
import com.wawa.baselib.utils.glide.utils.ImageUtil
import com.wawa.baselib.utils.logutils.LogUtils
import com.wawa.wawaandroid_ep.MainActivity
import com.wawa.wawaandroid_ep.MainViewModule
import com.wawa.wawaandroid_ep.WawaApp
import com.wawa.wawaandroid_ep.adapter.ImageAdapter
import com.wawa.wawaandroid_ep.adapter.viewmodel.NavItemViewModel
import com.wawa.wawaandroid_ep.base.fragment.BaseFragment
import com.wawa.wawaandroid_ep.fragment.MainFragment
import com.wawa.wawaandroid_ep.fragment.RoomListFragment
import com.wawa.wawaandroid_ep.fragmentv2.viewmodel.MainFmV2ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import net.lucode.hackware.magicindicator.buildins.UIUtil

/**
 *作者：create by 张金 on 2021/6/21 10:38
 *邮箱：564813746@qq.com
 */
class MainFragmentV2 : BaseFragment<MainFmV2LayBinding,MainFmV2ViewModel>(){
    val TAG="MainFragmentV2"
    val navListAdapter=ArrayListAdapter<PageOptionFragment.HomeMenu>()
    var titles = mutableListOf<String>()
    var fragments = mutableListOf<Fragment>()
    var curTab=0
    private val compositeDisposable = CompositeDisposable()
    val dataSource: BaseDataSource by lazy {
        (activity?.application as WawaApp).getDataSource(WawaApp.ServiceTypes.COROUTINES)
    }

    override fun onResume() {
        super.onResume()
        LogUtils.d(TAG,"onResume---")
        (activity as MainActivity).navBottom.visibility= View.VISIBLE
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): MainFmV2ViewModel {
        val mainFmV2ViewModel: MainFmV2ViewModel by viewModels()
        return mainFmV2ViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.main_fm_v2_lay
    }

    override fun initFragmentView() {
        MainViewModule.configMutableLiveData.observe(this,Observer{
            MainViewModule.configData=it
            initNavListView()
        })
        (WawaApp.app)!!.setUpDataSource()
        binding.mainSlideTab.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.setCustomView(null)
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                var textView= TextView(activity)
                textView.setTextSize( TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 16f, getResources().getDisplayMetrics()))
                textView.setTextColor(resources.getColor(R.color.black))
                textView.setTypeface(WawaApp.mMainTypeface)
                textView.text=tab?.text
                tab?.setCustomView(textView)
                tab?.let { curTab=it.position }
            }
        })
        val backPressCallback=requireActivity().onBackPressedDispatcher.addCallback (this){
            requireActivity().finish()
        }
        backPressCallback.isEnabled
        MainViewModule.mutableLiveuserData?.observe(this, Observer {
            binding.tvMainUsername.text=it?.nickName()
            ImageLoader.with(activity)
                .url(it?.avatarThumb())
//                .placeHolder(R.mipmap.ic_launcher)
                .rectRoundCorner(ImageUtil.dip2px(30f), RoundedCornersTransformation.CornerType.ALL)
                .into(binding.imMainHead2);

//            glideManager?.displayImg()
            Log.d(TAG,it?.avatarThumb().toString())
            viewModel.coins.set(it?.userAccount()?.coin().toString()+"")
//            mainFragmentViewModel.diamons.set(it.userAccount()?.fragments()?.userAcountFragment()?.)


        })
//        initNavListView()
        setUpBannerList()
        setUpRoomCategoryListDataSource()
        (activity as MainActivity).showUserAgreementDialog()
    }

    fun initNavListView(){
        val navLayoutManager=LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        binding.lvMainTopNavigation.bindAdapter(navListAdapter,navLayoutManager)
        MainViewModule.configData?.page()?.fragments()?.pageOptionFragment()?.homeMenu()?.let {
            if (it.size >0){
                val navlist=ObservableArrayList<ArrayListViewModel<PageOptionFragment.HomeMenu>>()
                for (i in it){
                    val homeModel=NavItemViewModel()
                    homeModel.model=i
                    navlist.add(homeModel)
                }
                navListAdapter.addAllData(navlist)
            }
        }
    }

    private fun setUpBannerList(){
        val successBannerListDisposable=dataSource.bannerList
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleSuccessBannerList)

        val errorBannerListDisposable=dataSource.error
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleErrorBannerList)
        compositeDisposable.add(successBannerListDisposable)
        compositeDisposable.add(errorBannerListDisposable)
        dataSource.getBannerList(1)
    }

    private fun setUpRoomCategoryListDataSource(){
        val successCategoryListDisposable = dataSource.roomCategoryList
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleSuccessRoomCategoryList)
        val errorRoomCategoryListDisposable=dataSource.error
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleErrorRoomCategoryList)
        compositeDisposable.add(successCategoryListDisposable)
        compositeDisposable.add(errorRoomCategoryListDisposable)
        dataSource.getRoomCategoryList()
    }

    private fun handleSuccessRoomCategoryList(categoryList: List<RoomCategoryListQuery.RoomCategoryList>){
        Log.d(TAG,categoryList.size.toString()+"")
        for (category in categoryList){
            category.name()?.let { titles.add(it) }
            var roomListFragment= RoomListFragment()
            var bundle= Bundle();
            category.roomCategoryId()?.toInt()?.let { bundle.putInt("categoryId", it) }
            roomListFragment.arguments=bundle
            fragments.add(roomListFragment)
        }
        binding.mainViewpager.adapter=
            object : FragmentPagerAdapter(childFragmentManager) {
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
        binding.mainSlideTab.setupWithViewPager(binding.mainViewpager)
        binding.imRefresh.setOnClickListener {
            (fragments.get(curTab) as RoomListFragment).reFreshPage()
        }
    }

    private fun handleSuccessBannerList(bannerList: List<BannerListQuery.BannerList>){
        Log.d(TAG,bannerList.size.toString()+"")
        //使用内置Indicator
        binding.mainBanner//设置左右页面露出来的宽度及item与item之间的宽度
            .setPageMargin(UIUtil.dip2px(context, 20.0), UIUtil.dip2px(context, 10.0))
//内置ScaleInTransformer，设置切换缩放动画
        binding.mainBanner.addPageTransformer( ScaleInTransformer())
        //使用内置Indicator
        val indicator = IndicatorView(context)
            .setIndicatorColor(Color.DKGRAY)
            .setIndicatorSelectorColor(Color.WHITE)
        val imageAdapter= ImageAdapter(activity,bannerList)
        binding.mainBanner.setIndicator(indicator).adapter=imageAdapter

    }


    private fun handleErrorRoomCategoryList(error: Throwable?){
        Log.d(TAG,"handleErrorRoomCategoryList--")

    }

    private fun handleErrorBannerList(error: Throwable?){
        Log.d(TAG,"handleErrorBannerList--")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable?.clear()
    }

}