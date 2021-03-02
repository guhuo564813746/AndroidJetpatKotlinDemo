package com.wawa.wawaandroid_ep.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.viewpager.widget.PagerAdapter
import com.apollographql.apollo.BannerListQuery
import com.apollographql.apollo.RoomCategoryListQuery
import com.google.android.material.tabs.TabLayout
import com.to.aboomy.pager2banner.IndicatorView
import com.to.aboomy.pager2banner.ScaleInTransformer
import com.wawa.baselib.utils.apollonet.BaseDataSource
import com.wawa.baselib.utils.glide.GlideManager
import com.wawa.baselib.utils.glide.loader.ImageLoader
import com.wawa.baselib.utils.glide.utils.ImageUtil
import com.wawa.wawaandroid_ep.BR
import com.wawa.wawaandroid_ep.MainViewModule
import com.wawa.wawaandroid_ep.R
import com.wawa.wawaandroid_ep.WawaApp
import com.wawa.wawaandroid_ep.adapter.ImageAdapter
import com.wawa.wawaandroid_ep.base.fragment.BaseFragment
import com.wawa.wawaandroid_ep.databinding.FragmentMainLayBinding
import com.wawa.wawaandroid_ep.fragment.viewmodule.MainFragmentViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import net.lucode.hackware.magicindicator.buildins.UIUtil


/**
 *作者：create by 张金 on 2021/1/13 18:08
 *邮箱：564813746@qq.com
 */
class MainFragment : BaseFragment<FragmentMainLayBinding,MainFragmentViewModel>() {
    private lateinit var mainTabLay: TabLayout
    var titles = mutableListOf<String>()
    var fragments = mutableListOf<Fragment>()
    private val compositeDisposable = CompositeDisposable()
    val dataSource: BaseDataSource by lazy {
        (activity?.application as WawaApp).getDataSource(WawaApp.ServiceTypes.COROUTINES)
    }
    companion object{
        val TAG="MainFragment"
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_main_lay
    }

    override fun initFragmentView() {
        MainViewModule.userData?.observe(this, Observer {
            binding.tvMainUsername.text=it.fragments()?.userFragment()?.nickName()
            ImageLoader.with(activity)
                .url(it.fragments()?.userFragment()?.avatarThumb())
//                .placeHolder(R.mipmap.ic_launcher)
                .rectRoundCorner(ImageUtil.dip2px(30f), RoundedCornersTransformation.CornerType.ALL)
                .into(binding.imMainHead2);

//            glideManager?.displayImg()
            Log.d(TAG,it.fragments()?.userFragment()?.avatarThumb().toString())
            viewModel.coins.set(it.fragments()?.userFragment()?.userAccount()?.fragments()?.userAcountFragment()?.coin().toString()+"")
//            mainFragmentViewModel.diamons.set(it.userAccount()?.fragments()?.userAcountFragment()?.)


        })
        setUpBannerList()
        setUpRoomCategoryListDataSource()
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
            var roomListFragment=RoomListFragment()
            var bundle=Bundle();
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
        mainTabLay=binding.tabLay2.findViewById(R.id.main_slide_tab) as TabLayout
        mainTabLay.setupWithViewPager(binding.mainViewpager)
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
        val imageAdapter=ImageAdapter(activity,bannerList)
        binding.mainBanner.setIndicator(indicator).adapter=imageAdapter


    }


    private fun handleErrorRoomCategoryList(error: Throwable?){
        Log.d(TAG,"handleErrorRoomCategoryList--")

    }

    private fun handleErrorBannerList(error: Throwable?){
        Log.d(TAG,"handleErrorBannerList--")
    }



    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): MainFragmentViewModel {
        val mainFragmentViewModel: MainFragmentViewModel by viewModels()
        return mainFragmentViewModel
    }

}