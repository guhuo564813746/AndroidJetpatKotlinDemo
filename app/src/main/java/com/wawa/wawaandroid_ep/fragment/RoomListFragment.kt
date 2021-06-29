package com.wawa.wawaandroid_ep.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apollographql.apollo.BannerListQuery
import com.apollographql.apollo.RoomListQuery
import com.robotwar.app.BR
import com.robotwar.app.R
import com.robotwar.app.databinding.RoomlistFmLayBinding
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.wawa.baselib.utils.apollonet.BaseDataSource
import com.wawa.baselib.utils.dialog.LoadingDialogManager
import com.wawa.baselib.utils.logutils.LogUtils
import com.wawa.wawaandroid_ep.WawaApp
import com.wawa.wawaandroid_ep.adapter.RoomListAdapter
import com.wawa.wawaandroid_ep.base.fragment.BaseFragment
import com.wawa.wawaandroid_ep.base.viewmodel.BaseVM
import com.wawa.wawaandroid_ep.fragment.viewmodule.RoomListFragmentViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 *作者：create by 张金 on 2021/2/1 16:38
 *邮箱：564813746@qq.com
 */
class RoomListFragment : BaseFragment<RoomlistFmLayBinding, RoomListFragmentViewModel>(),
    OnRefreshListener, OnLoadMoreListener {
    private val TAG="RoomListFragment"
    private var mPage=1
    private val dataSource: BaseDataSource by lazy {
        (activity?.application as WawaApp).getDataSource(WawaApp.ServiceTypes.COROUTINES)
    }
    init {
        Log.d(TAG,"init")
    }
    private lateinit var roomListAdapter: RoomListAdapter
    private val compositeDisposable = CompositeDisposable()
    var categoryId: Int?=null
    override fun getLayoutId(): Int {
        return R.layout.roomlist_fm_lay
    }

    override fun initFragmentView() {
        Log.d(TAG,"initFragmentView--")
        binding.refreshLayout.setOnRefreshListener(this)
        binding.refreshLayout.setOnLoadMoreListener(this)
        categoryId=arguments?.getInt("categoryId",0)
        binding.lvRooms.layoutManager=GridLayoutManager(activity,2,LinearLayoutManager.VERTICAL,false)
        activity?.let { roomListAdapter=RoomListAdapter(it) }
        binding.lvRooms.adapter= roomListAdapter
        setUpRoomListDataSource()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        LogUtils.d(TAG,"setUserVisibleHint--$isVisibleToUser")

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG,"onViewCreated--")
    }

    fun setUpRoomListDataSource(){
        LogUtils.d(TAG,"setUpRoomListDataSource--")
        val successRoomListDispose= dataSource.roomList
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleSuccessRoomList)

        val errorRoomListDispose=dataSource.error
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleErrorRoomList)
        compositeDisposable.add(successRoomListDispose)
        compositeDisposable.add(errorRoomListDispose)
        categoryId?.let { dataSource.getRoomList(it,mPage) }
    }

    private fun handleSuccessRoomList(roomList: List<RoomListQuery.List>){
        Log.d(TAG,"handleSuccessRoomList--"+roomList.toString())
        LoadingDialogManager.dismissLoading()
        if (mPage==1){
            if (binding.refreshLayout.isRefreshing){
                binding.refreshLayout.finishRefresh()
            }
            roomList?.let {
                binding.noData.visibility=View.GONE
                roomListAdapter?.roomLists=it
                roomListAdapter?.notifyDataSetChanged()
            }
            if (roomList== null || roomList?.size==0){
                binding.noData.visibility=View.VISIBLE
                roomListAdapter?.notifyDataSetChanged()
            }
        }else{
            if (binding.refreshLayout.isLoading){
                binding.refreshLayout.finishLoadMore()
            }
            roomList?.let{
                roomListAdapter?.roomLists = roomListAdapter?.roomLists?.plus(it)
                roomListAdapter?.notifyItemRangeInserted(roomListAdapter?.roomLists?.size!! - it.size,it.size)
//                gameRecordsAdapter?.notifyDataSetChanged()
            }
            if (roomList==null || roomList?.size==0){
                Toast.makeText(activity,"no more data", Toast.LENGTH_SHORT).show()
                binding.refreshLayout.finishLoadMoreWithNoMoreData()
            }
        }

    }

    private fun handleErrorRoomList(e:Throwable?){
        Log.d(TAG,"handleErrorRoomList--")
        LoadingDialogManager.dismissLoading()
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
        LogUtils.d(TAG,"onDestroy--")
        compositeDisposable.dispose()
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): RoomListFragmentViewModel {
        val roomListFragmentViewModel: RoomListFragmentViewModel by viewModels()
        return roomListFragmentViewModel
    }

    fun reFreshPage(){
        LogUtils.d(TAG,"reFreshPage--")

        activity?.let {
            LoadingDialogManager.loadBigDialog(it,getString(R.string.loading))?.show()
        }
        mPage=1
        setUpRoomListDataSource()
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        LogUtils.d(TAG,"onRefresh--")
        mPage=1
        setUpRoomListDataSource()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        LogUtils.d(TAG,"onLoadMore--")
        mPage+=1
        setUpRoomListDataSource()
    }

}