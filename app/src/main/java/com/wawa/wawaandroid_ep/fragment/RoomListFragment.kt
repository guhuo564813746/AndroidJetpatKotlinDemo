package com.wawa.wawaandroid_ep.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apollographql.apollo.BannerListQuery
import com.apollographql.apollo.RoomListQuery
import com.wawa.wawaandroid_ep.BR
import com.wawa.wawaandroid_ep.R
import com.wawa.wawaandroid_ep.WawaApp
import com.wawa.wawaandroid_ep.adapter.RoomListAdapter
import com.wawa.wawaandroid_ep.base.fragment.BaseFragment
import com.wawa.wawaandroid_ep.base.viewmodel.BaseVM
import com.wawa.wawaandroid_ep.databinding.RoomlistFmLayBinding
import com.wawa.wawaandroid_ep.fragment.viewmodule.RoomListFragmentViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 *作者：create by 张金 on 2021/2/1 16:38
 *邮箱：564813746@qq.com
 */
class RoomListFragment : BaseFragment<RoomlistFmLayBinding, RoomListFragmentViewModel>(){
    private val TAG="RoomListFragment"
    private var mPage=1
    init {
        Log.d(TAG,"init")
    }
    private val compositeDisposable = CompositeDisposable()
    var categoryId: Int?=null
    override fun getLayoutId(): Int {
        return R.layout.roomlist_fm_lay
    }

    override fun initFragmentView() {
        Log.d(TAG,"initFragmentView--")
        categoryId=arguments?.getInt("categoryId",0)
        binding.lvRooms.layoutManager=GridLayoutManager(activity,2,LinearLayoutManager.VERTICAL,false)
        setUpRoomListDataSource()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG,"onViewCreated--")
    }

    fun setUpRoomListDataSource(){
        val successRoomListDispose= (activity?.application as WawaApp).dataSource.roomList
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleSuccessRoomList)

        val errorRoomListDispose=(activity?.application as WawaApp).dataSource.error
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleErrorRoomList)
        compositeDisposable.add(successRoomListDispose)
        compositeDisposable.add(errorRoomListDispose)
        categoryId?.let { (activity?.application as WawaApp).dataSource.getRoomList(it,mPage) }
    }

    private fun handleSuccessRoomList(roomList: List<RoomListQuery.List>){
        Log.d(TAG,"handleSuccessRoomList--"+roomList.size)
        if (roomList.size==0){
            return
        }
        binding.lvRooms.adapter= activity?.let { RoomListAdapter(it,roomList) }

    }

    private fun handleErrorRoomList(e:Throwable?){
        Log.d(TAG,"handleErrorRoomList--")
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        Log.d(TAG,"onHiddenChanged--"+hidden)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG,"onDestroyView--")
//        compositeDisposable.dispose()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): RoomListFragmentViewModel {
        val roomListFragmentViewModel: RoomListFragmentViewModel by viewModels()
        return roomListFragmentViewModel
    }

}