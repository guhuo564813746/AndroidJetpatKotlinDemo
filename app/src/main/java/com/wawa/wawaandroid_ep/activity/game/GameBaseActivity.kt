package com.wawa.wawaandroid_ep.activity.game

import android.os.Build.HOST
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.apollographql.apollo.RoomInfoQuery
import com.apollographql.apollo.RoomListQuery
import com.wawa.baselib.utils.Utils
import com.wawa.baselib.utils.apollonet.BaseDataSource
import com.wawa.baselib.utils.logutils.LogUtils
import com.wawa.baselib.utils.socketio.GameSocketManager
import com.wawa.baselib.utils.socketio.listener.EpGameListener
import com.wawa.baselib.utils.socketio.listener.GameManagerListener
import com.wawa.wawaandroid_ep.BuildConfig
import com.wawa.wawaandroid_ep.WawaApp
import com.wawa.wawaandroid_ep.activity.viewmodule.BaseGameViewModel
import com.wawa.wawaandroid_ep.base.activity.BaseActivity
import com.wawa.wawaandroid_ep.commen.Comen
import com.wawa.wawaandroid_ep.gamevideopager.BaseGameVideoControlor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject

/**
 *作者：create by 张金 on 2021/2/3 14:23
 *邮箱：564813746@qq.com
 */
abstract class GameBaseActivity<V : ViewDataBinding> : BaseActivity<V>(), GameManagerListener {
    private val TAG="GameBaseActivity"
    protected val compositeDisposable = CompositeDisposable()
    protected var gameVideoControlor: Fragment?=null
    protected val baseGameViewModel: BaseGameViewModel by viewModels()
    protected val dataSource: BaseDataSource by lazy {
        (application as WawaApp).getDataSource(WawaApp.ServiceTypes.COROUTINES)
    }

    var ROOM_ID: String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initGame()
        ROOM_ID=intent.getStringExtra("ROOM_ID")
        initRoomData()
    }

    fun initGame(){
        GameSocketManager.getInstance()
            .setGameManagerListener(this)
            .connect(BuildConfig.HOST)
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
            baseGameViewModel.roomInfoData.value=roomInfo.get(0)
        }

    }

    private fun handleErrorRoomInfo(e: Throwable?){

    }



    override fun joinRoomSuccess() {
        LogUtils.d(TAG,"joinRoomSuccess")
    }

    override fun onGameOver(jsondata: JSONObject?) {
        LogUtils.d(TAG,"onGameOver")
    }

    override fun onLiveStreamChanged(jsondata: JSONObject?) {
        LogUtils.d(TAG,"onLiveStreamChanged")
    }

    override fun onIMNotify(jsondata: JSONObject?) {
        LogUtils.d(TAG,"onIMNotify")
    }

    override fun onGameLockCountDowned(jsondata: JSONObject?) {
        LogUtils.d(TAG,"onGameLockCountDowned")
    }

    override fun onConnectError(t: Throwable?) {
        LogUtils.d(TAG,"onConnectError")
    }

    override fun onRoomQueueKickOff() {
        LogUtils.d(TAG,"onRoomQueueKickOff")
    }

    override fun onOwnGameOver(jsondata: JSONObject?) {
        LogUtils.d(TAG,"onOwnGameOver")
    }

    override fun onDisconnect(reason: JSONObject?) {
        LogUtils.d(TAG,"onDisconnect")
    }

    override fun onOwnGameStart(jsondata: JSONObject?) {
        LogUtils.d(TAG,"onOwnGameStart")
    }

    override fun joinRoomError(errcode: Int, errmsg: String?) {
        LogUtils.d(TAG,"joinRoomError")
    }

    override fun onConnect() {
        LogUtils.d(TAG,"onConnect")
        checkLogin()
    }

    fun checkLogin(){
        var data= JSONObject()
        data.put("id",GameSocketManager.generateId().toString())
        data.put("method","check_auth")
        GameSocketManager.getInstance().sendMessage("app",data,object: GameSocketManager.Callback{
            override fun onSuccess(jsonStr: JSONObject?) {
                LogUtils.d(TAG,"checkLoginSuccess")
                joinRoom()
            }

            override fun onError(errorCode: Int, errorMsg: String?) {
                LogUtils.d(TAG,"checkLoginErr"+errorCode)
                if (errorCode==Comen.TOKEN_ERR){
                    //未登录，重新登陆
                    socketLogin()
                }
            }
        })
    }

    fun joinRoom(){
        var data=JSONObject()
        var params=JSONObject()
        data.put("id",GameSocketManager.generateId().toString())
        data.put("method","join_room")
        params.put("room_id",ROOM_ID?.toInt())
        data.put("params",params)
        GameSocketManager.getInstance().sendMessage("app",data, object: GameSocketManager.Callback{
            override fun onSuccess(jsonStr: JSONObject?) {
                LogUtils.d(TAG,"joinRoom_success")
            }

            override fun onError(errorCode: Int, errorMsg: String?) {
                LogUtils.d(TAG,"joinRoom_error")
            }
        })
    }

    /*发送聊天信息*/
    fun sendChatMsg(){

    }

    /*开始排队*/
    fun joinQueue(){
        var data=JSONObject()
        data.put("id",GameSocketManager.generateId().toString())
        data.put("method","join_queue")
        GameSocketManager.getInstance().sendMessage("game",data,object: GameSocketManager.Callback{
            override fun onSuccess(jsonStr: JSONObject?) {
                LogUtils.d(TAG,"join_queue--success")

            }

            override fun onError(errorCode: Int, errorMsg: String?) {
                LogUtils.d(TAG,"join_queue--falure")
            }
        })
    }

    /*退出排队*/
    fun quitQueue(){
        var data=JSONObject()
        data.put("id",GameSocketManager.generateId().toString())
        data.put("method","quit_queue")
        GameSocketManager.getInstance().sendMessage("game",data,object: GameSocketManager.Callback{
            override fun onSuccess(jsonStr: JSONObject?) {
                LogUtils.d(TAG,"quit_queue--success")

            }

            override fun onError(errorCode: Int, errorMsg: String?) {
                LogUtils.d(TAG,"quit_queue--falure")
            }
        })
    }
    /*开始游戏*/
    abstract fun startGame()

    fun endGame(){
        var data=JSONObject()
        data.put("id",GameSocketManager.generateId().toString())
        data.put("method","quit_game")
        GameSocketManager.getInstance().sendMessage("game",data,object: GameSocketManager.Callback{
            override fun onSuccess(jsonStr: JSONObject?) {
                LogUtils.d(TAG,"quit_game--success")

            }

            override fun onError(errorCode: Int, errorMsg: String?) {
                LogUtils.d(TAG,"quit_game--falure")
            }
        })
    }

    fun socketLogin(){
        var  params= JSONObject()
        var data= JSONObject()
        data.put("id",GameSocketManager.generateId().toString())
        data.put("method","login")
        params.put("user_id",Utils.readUid()?.toInt())
        params.put("token",Utils.readToken())
        data.put("params",params)
        GameSocketManager.getInstance().sendMessage("app",data,object: GameSocketManager.Callback{
            override fun onSuccess(jsonStr: JSONObject?) {
                LogUtils.d(TAG,"gameLogin--success")
                var userCoin=jsonStr?.getInt("user_coin")
                var  userPoints=jsonStr?.getInt("user_point")
                baseGameViewModel.coin.set(userCoin.toString())
                baseGameViewModel.points.set(userPoints.toString())
                joinRoom()
            }

            override fun onError(errorCode: Int, errorMsg: String?) {
                LogUtils.d(TAG,"gameLogin--falure")
            }
        })
    }

    override fun onRoomUserAmountChanged(jsondata: JSONObject?) {
        LogUtils.d(TAG,"onRoomUserAmountChanged")
        //处理房间人员信息

    }

    override fun onGameLockEnd(jsondata: JSONObject?) {
        LogUtils.d(TAG,"onGameLockEnd")
    }

    override fun onGameStart(jsondata: JSONObject?) {
        LogUtils.d(TAG,"onGameStart")
    }

    override fun onGameCountdown(jsondata: JSONObject?) {
        LogUtils.d(TAG,"onGameCountdown")
    }

    override fun onRoomQueueStatus(isPublic: Boolean, queueNo: Int, position: Int) {
        LogUtils.d(TAG,"onRoomQueueStatus")
    }

    override fun onGameLockStart(jsondata: JSONObject?) {
        LogUtils.d(TAG,"onGameLockStart")
    }

    override fun onRoomKickOff() {
        LogUtils.d(TAG,"onRoomKickOff")
    }

    override fun onMarqueeMsgNotify(jsondata: JSONObject?) {
        LogUtils.d(TAG,"onMarqueeMsgNotify")
    }

    override fun onGameReconnect(jsondata: JSONObject?) {
        LogUtils.d(TAG,"onGameReconnect")
    }

    override fun onGameReady(timeLeft: Int) {
        LogUtils.d(TAG,"onGameReady")
    }

    override fun websocketClosed() {
        LogUtils.d(TAG,"websocketClosed")
    }

    override fun onGamePlaying(jsonData: JSONObject?) {
        LogUtils.d(TAG,"onGamePlaying")
    }

    override fun onGameQueue(jsonData: JSONObject?) {
        LogUtils.d(TAG,"onGameQueue")
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

}