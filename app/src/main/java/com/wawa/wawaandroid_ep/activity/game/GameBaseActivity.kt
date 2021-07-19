package com.wawa.wawaandroid_ep.activity.game

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.ObservableInt
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import com.apollographql.apollo.RoomInfoQuery
import com.blankj.utilcode.util.ToastUtils
import com.robotwar.app.BuildConfig
import com.robotwar.app.R
import com.wawa.baselib.utils.SharePreferenceUtils
import com.wawa.baselib.utils.apollonet.BaseDataSource
import com.wawa.baselib.utils.glide.loader.ImageLoader
import com.wawa.baselib.utils.logutils.LogUtils
import com.wawa.baselib.utils.socketio.GameSocketManager
import com.wawa.baselib.utils.socketio.listener.GameManagerListener
import com.wawa.wawaandroid_ep.MainViewModule
import com.wawa.wawaandroid_ep.WawaApp
import com.wawa.wawaandroid_ep.activity.viewmodule.BaseGameViewModel
import com.wawa.wawaandroid_ep.adapter.GameOnlineUserListAdapter
import com.wawa.wawaandroid_ep.base.activity.BaseActivity
import com.wawa.wawaandroid_ep.commen.Comen
import com.wawa.wawaandroid_ep.gamevideopager.BaseGameVideoControlor
import com.wawa.wawaandroid_ep.gamevideopager.DaniuGameVideoControlor
import com.wawa.wawaandroid_ep.gamevideopager.LiveGameFragment
import com.wawa.wawaandroid_ep.sound.SoundManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

/**
 *作者：create by 张金 on 2021/2/3 14:23
 *邮箱：564813746@qq.com
 */
abstract class GameBaseActivity<V : ViewDataBinding,VM : BaseGameViewModel> : BaseActivity<V,VM>(), GameManagerListener {
    companion object{
        val CONSUME_TYPE_COIN=1
        val CONSUNE_TYPE_POINT=2
        val MSG_TYPE_TEXT=1
        val MSG_TYPE_AUDIO=2
        val MSG_TYPE_VIDEO=3
        val MSG_TYPE_FACE=4
        val GAME_TYPE_EP="dji_ep"
        val GAME_TYPE_FISH="fishing"
    }
    private val ERRORCODE_NO_BALANCE=0
    private val TAG="GameBaseActivity"
    protected val compositeDisposable = CompositeDisposable()
    protected var gameVideoControlor: Fragment?=null
    protected var gameOnlineUserListAdapter: GameOnlineUserListAdapter?=null
    protected var booleanShowChat=true
    val GAME_STATUS_EMPTY=1
    val GAME_STATUS_QUEUE=2
    val GAME_STATUS_PLAYING=3
    val GAME_STATUS_ENDING=4
    val GAME_STATUS_PREQUEUE=5
    var mGameStatus = ObservableInt(GAME_STATUS_EMPTY)
    var gameCurrency: Int=CONSUME_TYPE_COIN
    var gameResultCurrency: Int=CONSUME_TYPE_COIN
    var coin2hardRatio: Float=0f
    var diamond2hardRatio: Float=0f
    var score2hardRatio: Float=0f
    var queueTotal: Int=0
    var queuePosition: Int=0
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
        MainViewModule.mutableLiveuserData?.observe(this, Observer {
            viewModel.coin.set(it?.userAccount()?.coin().toString())
        })
        MainViewModule.mutableLiveuserData.value=MainViewModule.userData
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

    override fun initView() {
        viewModel.soundManager= SoundManager(this)
        (application as WawaApp).sendMsg.observe(this, Observer {
            sendChatMsg(it,MSG_TYPE_TEXT)
        })
        viewModel.topUpTipsDialogShow.observe(this, Observer {
            if (it){
                runOnUiThread{
                    showTopUpTipsDialog()
                }
            }
        })
        viewModel.roomInfoData?.observe(this, Observer {
            initGameVideo(it)
            var gameConsumeType=it.fragments()?.roomFragment()?.roomGameOption()?.fragments()?.roomGameOptionFragment()?.gameCurrency()
            var coinsExchange=it.fragments()?.roomFragment()?.roomGameOption()?.fragments()?.roomGameOptionFragment()?.coin2hardRatio()
            var diamonsExchange=it.fragments()?.roomFragment()?.roomGameOption()?.fragments()?.roomGameOptionFragment()?.diamond2hardRatio()
            var scoresExchange=it.fragments()?.roomFragment()?.roomGameOption()?.fragments()?.roomGameOptionFragment()?.score2hardRatio()
            gameConsumeType?.let { gameCurrency=it }
            when(gameConsumeType){
                CONSUME_TYPE_COIN ->{
                    coinsExchange?.let {
                        coin2hardRatio=it.toFloat()
                        viewModel.fee.set(java.lang.String.format("%s: %s",getString(R.string.this_time),coin2hardRatio.toInt().toString()))
                    }
                }
                CONSUNE_TYPE_POINT ->{
                    scoresExchange?.let {
                        score2hardRatio=it.toFloat()
                        viewModel.fee.set(java.lang.String.format("%s: %s",getString(R.string.this_time),score2hardRatio.toInt().toString()))
                    }
                }
            }

        })
    }

    private fun handleSuccessRoomInfo(roomInfo: List<RoomInfoQuery.List>){
        Log.d(TAG,"handleSuccessRoomInfo--"+roomInfo?.toString())
        if (!roomInfo.isNullOrEmpty()){
            viewModel.roomInfoData.value=roomInfo.get(0)
        }

    }

    private fun handleErrorRoomInfo(e: Throwable?){

    }



    override fun joinRoomSuccess() {
        LogUtils.d(TAG,"joinRoomSuccess")
    }

    override fun onGameResult(jsonData: JSONObject?) {
        LogUtils.d(TAG,"onGameResult--${jsonData?.toString()}")
    }

    override fun onGameOver(jsondata: JSONObject?) {
        LogUtils.d(TAG,"onGameOver")
        runOnUiThread{
            viewModel.playerGameViewVisibility.set(View.GONE)
            var player=jsondata?.getJSONObject("player")
            var userId=player?.getInt("user_id")
            userId?.let {
                if (it.toString().equals(MainViewModule.userData?.userId())){
                    setGameOverStatus()
                }
            }
        }
    }

    override fun onLiveStreamChanged(jsondata: JSONObject?) {
        LogUtils.d(TAG,"onLiveStreamChanged")
        runOnUiThread{

        }
    }

    override fun onIMNotify(jsondata: JSONObject?) {
        LogUtils.d(TAG,"onIMNotify"+jsondata?.toString())
        runOnUiThread{

        }
    }

    override fun onGameLockCountDowned(jsondata: JSONObject?) {
        LogUtils.d(TAG,"onGameLockCountDowned")
        runOnUiThread{

        }
    }

    override fun onConnectError(t: Throwable?) {
        LogUtils.d(TAG,"onConnectError")
        runOnUiThread{

        }
    }

    override fun onRoomQueueKickOff() {
        LogUtils.d(TAG,"onRoomQueueKickOff")
        runOnUiThread{
            setGameOverStatus()
        }
    }

    override fun onOwnGameOver(jsondata: JSONObject?) {
        LogUtils.d(TAG,"onOwnGameOver")
        runOnUiThread{

        }
    }

    override fun onDisconnect(reason: JSONObject?) {
        LogUtils.d(TAG,"onDisconnect")
        runOnUiThread{

        }
    }

    override fun onOwnGameStart(jsondata: JSONObject?) {
        LogUtils.d(TAG,"onOwnGameStart")
        runOnUiThread{

        }
    }

    override fun joinRoomError(errcode: Int, errmsg: String?) {
        LogUtils.d(TAG,"joinRoomError")
        runOnUiThread{

        }
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
                runOnUiThread{
//                    ToastUtils.showShort(errorMsg)
                }
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
                runOnUiThread {
                    ToastUtils.showShort(errorMsg)
                }

            }
        })
    }


    fun btnStartGame(view: View) {
//        startGame()
        when(mGameStatus.get()){
            GAME_STATUS_EMPTY,GAME_STATUS_PREQUEUE->{
                joinQueue()
            }
            GAME_STATUS_QUEUE->{
                quitQueue()
            }
        }

    }

    /*开始排队*/
    fun joinQueue(){
        var data=JSONObject()
        data.put("id",GameSocketManager.generateId().toString())
        data.put("method","join_queue")
        GameSocketManager.getInstance().sendMessage("game",data,object: GameSocketManager.Callback{
            override fun onSuccess(jsonStr: JSONObject?) {
                LogUtils.d(TAG,"join_queue--success")
                setGameQueueStatus()
            }

            override fun onError(errorCode: Int, errorMsg: String?) {
                LogUtils.d(TAG,"join_queue--falure")
                runOnUiThread {
                    ToastUtils.showShort(errorMsg)
                    setGameOverStatus()
                    if (errorCode ==ERRORCODE_NO_BALANCE){
                        //余额不足，显示充值提示弹窗
                        showTopUpTipsDialog()
                    }
                }
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
                setGameOverStatus()
            }

            override fun onError(errorCode: Int, errorMsg: String?) {
                LogUtils.d(TAG,"quit_queue--falure")
                runOnUiThread {
                    ToastUtils.showShort(errorMsg)
                }
            }
        })
    }
    /*开始游戏*/
    fun startGame() {
        runOnUiThread{
            var data = JSONObject()
            data.put("id", GameSocketManager.generateId().toString())
            data.put("method", "start_game")
            GameSocketManager.getInstance()
                .sendMessage("game", data, object : GameSocketManager.Callback {
                    override fun onSuccess(jsonStr: JSONObject?) {
                        LogUtils.d(TAG, "startgame--success")
                        //发个指令测试
                        initStartGame()
                    }

                    override fun onError(errorCode: Int, errorMsg: String?) {
                        LogUtils.d(TAG, "startgame--falure" + errorMsg)
                        runOnUiThread {
                            ToastUtils.showShort(errorMsg)
                            setGameOverStatus()
                        }
                    }
                })
        }
    }

    abstract fun initStartGame()

    fun endGame(){
        when(mGameStatus.get()){
            GAME_STATUS_PLAYING ->{
                var data = JSONObject()
                data.put("id", GameSocketManager.generateId().toString())
                data.put("method", "quit_game")
                GameSocketManager.getInstance()
                    .sendMessage("game", data, object : GameSocketManager.Callback {
                        override fun onSuccess(jsonStr: JSONObject?) {
                            LogUtils.d(TAG, "quitgame--success")
                            //发个指令测试

                        }

                        override fun onError(errorCode: Int, errorMsg: String?) {
                            LogUtils.d(TAG, "quitgame--falure" + errorMsg)
                            runOnUiThread {
                                ToastUtils.showShort(errorMsg)
                            }
                        }
                    })
            }
            else ->{
                finish()
            }
        }
    }

    fun socketLogin(){
        var  params= JSONObject()
        var data= JSONObject()
        data.put("id",GameSocketManager.generateId().toString())
        data.put("method","login")
        params.put("user_id",SharePreferenceUtils.readUid()?.toInt())
        params.put("token",SharePreferenceUtils.readToken())
        data.put("params",params)
        GameSocketManager.getInstance().sendMessage("app",data,object: GameSocketManager.Callback{
            override fun onSuccess(jsonStr: JSONObject?) {
                LogUtils.d(TAG,"gameLogin--success $jsonStr")
                var userCoin=jsonStr?.getInt("user_coin")
                var  userPoints=jsonStr?.getInt("user_point")
                viewModel.coin.set(userCoin.toString())
                viewModel.points.set(userPoints.toString())
                joinRoom()
            }

            override fun onError(errorCode: Int, errorMsg: String?) {
                LogUtils.d(TAG,"gameLogin--falure")
                runOnUiThread {
                    ToastUtils.showShort(errorMsg)
                }
            }
        })
    }

    override fun onRoomUserAmountChanged(jsondata: JSONObject?) {
        LogUtils.d(TAG,"onRoomUserAmountChanged"+jsondata.toString())
        //处理房间人员信息
        runOnUiThread{

            val playerStr=jsondata?.getString("player")
            var player: JSONObject?=null
            playerStr?.let {
                try {
                    player= JSONObject(it)
                    player?.let {
                        val userId=it.getInt("user_id")
                        val nickname=it.getString("nickname")
                        val avatar=it.getString("avatar")
                        viewModel.playerName.set(nickname)
                    }
                }catch (e: Exception){

                }
            }
        }
    }

    override fun onGameLockEnd(jsondata: JSONObject?) {
        LogUtils.d(TAG,"onGameLockEnd")
        runOnUiThread{

        }
    }

    override fun onGameStart(jsondata: JSONObject?) {
        LogUtils.d(TAG,"onGameStart"+jsondata.toString())
        runOnUiThread{
            viewModel.playerGameViewVisibility.set(View.VISIBLE)
            var player=jsondata?.getJSONObject("player")
            var userId=player?.getInt("user_id")
            userId?.let {
                if (it.toString().equals(MainViewModule.userData?.userId())){
                    setGameStartStatus()
                    initStartGame()
                }
            }
        }
    }

    override fun onGameCountdown(jsondata: JSONObject?) {
        LogUtils.d(TAG,"onGameCountdown")
        runOnUiThread{

        }
    }

    override fun onRoomQueueStatus(isPublic: Boolean, queueNo: Int, position: Int) {
        LogUtils.d(TAG,"onRoomQueueStatus")
        runOnUiThread{

        }
    }

    override fun onGameLockStart(jsondata: JSONObject?) {
        LogUtils.d(TAG,"onGameLockStart")
        runOnUiThread{

        }
    }

    override fun onRoomKickOff() {
        LogUtils.d(TAG,"onRoomKickOff")
        runOnUiThread{

        }
    }

    override fun onMarqueeMsgNotify(jsondata: JSONObject?) {
        LogUtils.d(TAG,"onMarqueeMsgNotify")
        runOnUiThread{

        }
    }

    override fun onGameReconnect(jsondata: JSONObject?) {
        LogUtils.d(TAG,"onGameReconnect")
        runOnUiThread{
//            setGameStartStatus()
//            initStartGame()
        }
    }

    override fun onGameReady(timeLeft: Int) {
        LogUtils.d(TAG,"onGameReady")
        runOnUiThread{
            when(mGameStatus.get()){
                GAME_STATUS_EMPTY,GAME_STATUS_PREQUEUE ->{

                }
                GAME_STATUS_QUEUE->{
                    showGameReadyDialog(timeLeft)
                }
            }
        }
    }

    abstract fun showGameReadyDialog(timeLeft: Int)

    override fun websocketClosed() {
        LogUtils.d(TAG,"websocketClosed")
        runOnUiThread{

        }
    }

    override fun onGamePlaying(jsonData: JSONObject?) {
        LogUtils.d(TAG,"onGamePlaying")
        runOnUiThread{
            setGameStartStatus()
            initStartGame()
        }
    }

    override fun onGameQueue(jsonData: JSONObject?) {
        LogUtils.d(TAG,"onGameQueue")
        runOnUiThread{
            var type=jsonData?.getInt("type")
            jsonData?.getInt("queue_total")?.let {
                queueTotal=it
            }
            jsonData?.getInt("queue_position")?.let {
                queuePosition=it
            }
            when(type){
                0->{
                    //全局
                    queueTotal?.let { if (it > 0){
                        if (mGameStatus.get()==GAME_STATUS_EMPTY){
                            viewModel.startGameBtnRes.set(gameQueueBtnBg())
                        }
                        viewModel.queueCount.set(getString(R.string.cur_queue)+it)
                    }else{
                        viewModel.queueCount.set("")
                        if (mGameStatus.get()==GAME_STATUS_EMPTY){
                            viewModel.startGameBtnRes.set(gameStartBtnBg())
                        }
                    }
                    }

                }
                1->{
                    //排队中的玩家
//                viewModel.startGameBtnRes.set(R.drawable.btn_cancel_game)
                    queuePosition?.let {
                        if (it >0){
                            if (mGameStatus.get() != GAME_STATUS_EMPTY){
                                viewModel.queueCount.set(getString(R.string.brefore_queue)+it)
                            }
                        }else{
                            viewModel.queueCount.set("")
                        }
                    }

                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    fun setGameQueueStatus(){
        mGameStatus.set(GAME_STATUS_QUEUE)
        viewModel.startGameBtnRes.set(gameCancelBtnBg())
    }

    fun setGameResultStatus(){
        mGameStatus.set(GAME_STATUS_ENDING)
    }

    fun setGameStartStatus(){
        viewModel.soundManager?.playSound(SoundManager.GAME_START)
        mGameStatus.set(GAME_STATUS_PLAYING)
        viewModel.gamePanelVisibility.set(View.VISIBLE)
        viewModel.guestPanelVisibility.set(View.GONE)
        viewModel.playerGameViewVisibility.set(View.VISIBLE)
    }

    fun setGameOverStatus(){
        mGameStatus.set(GAME_STATUS_EMPTY)
        viewModel.queueCount.set("")
        viewModel.startGameBtnRes.set(gameStartBtnBg())
        viewModel.gamePanelVisibility.set(View.GONE)
        viewModel.guestPanelVisibility.set(View.VISIBLE)
        viewModel.playerGameViewVisibility.set(View.GONE)
    }

    fun initGameVideo(data: RoomInfoQuery.List) {
        if (gameVideoControlor == null) {
            if (SharePreferenceUtils.getSwitch(SharePreferenceUtils.VIDEO_PLAYER)){
                gameVideoControlor= LiveGameFragment()
            }else{
                gameVideoControlor = DaniuGameVideoControlor()
            }
            var bundle = Bundle()
            data?.fragments()?.roomFragment()?.liveStream()?.let {
                if (it.size >0){
                    bundle.putString(
                        BaseGameVideoControlor.MASTER_VIDEO_URL,
                        it.get(0)?.fragments()?.liveStreamforGameFragment()?.liveRtmpUrl()
                    )

                }
            }
            bundle.putString(BaseGameVideoControlor.SLAVE_VIDEO_URL, "")
            gameVideoControlor?.arguments = bundle
//        mLiveGameController.setLiveStreamUrl(mLiveBean.liveStreamV2List.get(streamDefaultQuility).liveRtmpUrl, null);
            var ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            gameVideoControlor?.let{
                ft.replace(R.id.stream_replaced, it)
                    .commitAllowingStateLoss()
            }
        }
    }

    abstract fun gameStartBtnBg(): Int
    abstract fun gameCancelBtnBg(): Int
    abstract fun gameQueueBtnBg(): Int
    abstract fun showQuitDislog()
    abstract fun showTopUpDialog()
    abstract fun showTopUpTipsDialog()
    override fun back(view: View) {

    }

    fun sendChatMsg(msg: String,msgType: Int){
        var data = JSONObject()
        data.put("id", GameSocketManager.generateId().toString())
        data.put("method", "send_message")
        //init Params
        val bodyJson=JSONObject()
        bodyJson.put("text",msg)
        val msgListBean=JSONObject()
        msgListBean.put("type",msgType)
        msgListBean.put("body",bodyJson)
        val msgList=JSONArray()
        msgList.put(0,msgListBean)
        val paramsJson=JSONObject()
        paramsJson.put("msg_list",msgList)
        data.put("params",paramsJson)

        GameSocketManager.getInstance()
            .sendMessage("app", data, object : GameSocketManager.Callback {
                override fun onSuccess(jsonStr: JSONObject?) {
                    LogUtils.d(TAG, "sendChatMsg--success")

                }

                override fun onError(errorCode: Int, errorMsg: String?) {
                    LogUtils.d(TAG, "sendChatMsg--falure" + errorMsg)
                    runOnUiThread {
                        ToastUtils.showShort(errorMsg)
                    }
                }
            })
    }

    override fun onBackPressed() {
//        super.onBackPressed()
        if (mGameStatus.get()==GAME_STATUS_PLAYING){
            showQuitDislog()
        }else{
            finish()
        }
    }
}