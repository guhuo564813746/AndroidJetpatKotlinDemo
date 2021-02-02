package com.wawa.wawaandroid_ep.fragment

import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apollographql.apollo.BannerListQuery
import com.apollographql.apollo.RoomListQuery
import com.wawa.wawaandroid_ep.R
import com.wawa.wawaandroid_ep.WawaApp
import com.wawa.wawaandroid_ep.adapter.RoomListAdapter
import com.wawa.wawaandroid_ep.base.fragment.BaseFragment
import com.wawa.wawaandroid_ep.databinding.RoomlistFmLayBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 *作者：create by 张金 on 2021/2/1 16:38
 *邮箱：564813746@qq.com
 */
class RoomListFragment : BaseFragment<RoomlistFmLayBinding>(){
    private val TAG="RoomListFragment"
    private val compositeDisposable = CompositeDisposable()
    var categoryId: Int?=null
    override fun getLayoutId(): Int {
        return R.layout.roomlist_fm_lay
    }

    override fun initFragmentView() {
        setUpRoomListDataSource()
        categoryId=arguments?.getInt("categoryId",0)
        binding.lvRooms.layoutManager=GridLayoutManager(activity,2,LinearLayoutManager.VERTICAL,false)

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
        categoryId?.let { (activity?.application as WawaApp).dataSource.getRoomList(it) }
    }

    private fun handleSuccessRoomList(roomList: List<RoomListQuery.RoomList>){
        Log.d(TAG,"handleSuccessRoomList--")
        binding.lvRooms.adapter= activity?.let { RoomListAdapter(it,roomList) }

    }

    private fun handleErrorRoomList(e:Throwable?){
        Log.d(TAG,"handleErrorRoomList--")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.dispose()
    }

}