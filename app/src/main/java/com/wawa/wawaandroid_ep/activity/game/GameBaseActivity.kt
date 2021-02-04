package com.wawa.wawaandroid_ep.activity.game

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.ViewDataBinding
import com.apollographql.apollo.RoomInfoQuery
import com.apollographql.apollo.RoomListQuery
import com.wawa.baselib.utils.apollonet.BaseDataSource
import com.wawa.wawaandroid_ep.WawaApp
import com.wawa.wawaandroid_ep.activity.viewmodule.BaseGameViewModel
import com.wawa.wawaandroid_ep.base.activity.BaseActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 *作者：create by 张金 on 2021/2/3 14:23
 *邮箱：564813746@qq.com
 */
abstract class GameBaseActivity<V : ViewDataBinding> : BaseActivity<V>(){
    private val TAG="GameBaseActivity"
    protected var roomInfoData: RoomInfoQuery.RoomList?=null
    protected val compositeDisposable = CompositeDisposable()
    private val baseGameViewModel: BaseGameViewModel by viewModels()
    protected val dataSource: BaseDataSource by lazy {
        (application as WawaApp).getDataSource(WawaApp.ServiceTypes.COROUTINES)
    }

    var ROOM_ID: String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ROOM_ID=intent.getStringExtra("ROOM_ID")
        initRoomData()
    }

    fun initRoomData(){
        if (ROOM_ID.isNullOrEmpty()){
            return
        }
        val successRoomInfoDispose= dataSource.roomInfo
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleSuccessRoomInfo)

        val errorRoomInfoDispose=dataSource.error
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleErrorRoomInfo)
        compositeDisposable.add(successRoomInfoDispose)
        compositeDisposable.add(errorRoomInfoDispose)
        dataSource.getRoomInfoData(ROOM_ID!!.toInt())
    }

    private fun handleSuccessRoomInfo(roomInfo: List<RoomInfoQuery.RoomList>){
        Log.d(TAG,"handleSuccessRoomInfo--"+roomInfo.size)
        if (!roomInfo.isNullOrEmpty()){
            roomInfoData=roomInfo.get(0)
        }

    }

    private fun handleErrorRoomInfo(e: Throwable?){

    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

}