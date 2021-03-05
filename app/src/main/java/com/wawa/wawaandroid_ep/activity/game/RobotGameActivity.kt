package com.wawa.wawaandroid_ep.activity.game

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollographql.apollo.RoomInfoQuery
import com.blankj.utilcode.util.SizeUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wawa.baselib.utils.logutils.LogUtils
import com.wawa.baselib.utils.socketio.GameSocketManager
import com.wawa.baselib.utils.socketio.listener.EpGameListener
import com.wawa.wawaandroid_ep.BR
import com.wawa.wawaandroid_ep.R
import com.wawa.wawaandroid_ep.activity.viewmodule.RobotGameViewModel
import com.wawa.wawaandroid_ep.adapter.GameOnlineUserListAdapter
import com.wawa.wawaandroid_ep.bean.game.GameRoomUsers
import com.wawa.wawaandroid_ep.databinding.RobotGameActivityLayBinding
import com.wawa.wawaandroid_ep.gamevideopager.DaniuGameVideoControlor
import com.wawa.wawaandroid_ep.view.ButtonControlPanel
import com.wawa.wawaandroid_ep.view.DrawableMenuLayout
import com.wawa.wawaandroid_ep.view.RockerView
import org.json.JSONObject
import java.lang.reflect.Type


/**
 *作者：create by 张金 on 2021/2/3 14:27
 *邮箱：564813746@qq.com
 */
class RobotGameActivity : GameBaseActivity<RobotGameActivityLayBinding,RobotGameViewModel>(), EpGameListener {
    private val TAG = "RobotGameActivity"

    //Ep指令
    private var goTopleftEp: String = "chassis speed x 0.3 y 0.3 z 1;"
    private val goToprightEp = "chassis speed x -0.3 y 0.3 z 1;"
    private val stopEp = "chassis wheel w2 0 w1 0 w3 0 w4 0;"
    private val speedTopEp = "chassis speed x 0.3 y 0 z 1;"
    private val speedDownEp = "chassis speed x -0.3 y 0 z 1;"
    private val speedLeftEp = "chassis speed x 0 y -0.3 z 1;"
    private val speedRightEp = "chassis speed x 0 y 0.3 z 1;"
    private val goDownLeftEp = "chassis speed x -0.3 y -0.3 z 1;"
    private val goDownRightEp = "chassis speed x -0.3 y 0.3 z 1;"

    //云台速度控制
    private val gimbalUp = "gimbal speed p 60 y 0;"
    private val gimbalDown = "gimbal speed p -60 y 0;"
    private val gimbalLeft = "gimbal speed p 0 y -60;"
    private val gimbalRight = "gimbal speed p 0 y 60;"
    private val gimbalStop = "gimbal speed p 0 y 0;"

    private val stopSpeed = "chassis speed x 0 y 0 z 0;"
    private val fire_num = "blaster bead 2;"
    private val fire = "blaster fire;"

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.robot_game_activity_lay
    }

    override fun initView() {
        binding.streamReplaced.setOnClickListener {
            switchShowUserData()
        }
        initGameMenuView()
        initGameControlView()
        initChatView()
        initOnlineUserView()
//        binding.streamReplaced
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
                        viewModel.fee.set(java.lang.String.format("%s: %s",getString(R.string.this_time),coin2hardRatio.toString()))
                    }
                }
                CONSUNE_TYPE_POINT ->{
                    scoresExchange?.let {
                        score2hardRatio=it.toFloat()
                        viewModel.fee.set(java.lang.String.format("%s: %s",getString(R.string.this_time),score2hardRatio.toString()))
                    }
                }
            }

        })

    }

    fun switchShowUserData(){
        if (viewModel.userDataGroupVisibility.get()==View.GONE){
            viewModel.userDataGroupVisibility.set(View.VISIBLE)
        }else{
            viewModel.userDataGroupVisibility.set(View.GONE)
        }
    }

    fun initChatView(){
        binding.rlChatTag.setOnClickListener {
            if (booleanShowChat){
                hideChatView()
            }else{
                showChatView()
            }
        }
    }

    fun showChatView(){
        ObjectAnimator.ofFloat(binding.rlChat,"translationX",SizeUtils.dp2px(0f).toFloat()).apply{
            duration=500
            addListener(object : AnimatorListenerAdapter(){
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    booleanShowChat=true
                }
            })
            start()
        }
    }

    fun hideChatView(){
        ObjectAnimator.ofFloat(binding.rlChat,"translationX",SizeUtils.dp2px(-200f).toFloat()).apply{
            duration=500
            addListener(object : AnimatorListenerAdapter(){
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    booleanShowChat=false
                }
            })
            start()
        }
    }

    fun initOnlineUserView(){
        binding.userList.layoutManager=LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        if (gameOnlineUserListAdapter == null){
            gameOnlineUserListAdapter= GameOnlineUserListAdapter(this,ArrayList())
        }
        binding.userList.adapter=gameOnlineUserListAdapter
    }


    fun initGameControlView(){
        binding.btnFire.setOnClickListener {
            operateRobot(fire)
        }
        binding.epRockerControl.setListener(object :RockerView.OnShakeListener{
            override fun onFinish() {
                LogUtils.d(TAG,"rockerControl--finish")
                operateRobot(stopSpeed)
            }

            override fun direction(direction: RockerView.Direction?) {
                dealWithControlAction(direction)
            }

            override fun onStart() {
                LogUtils.d(TAG,"rockerControl--start")
            }
        })
        binding.epButtonControl.setListener(object: ButtonControlPanel.OnTouchListener{
            override fun onTouch(
                view: View?,
                direction: RockerView.Direction?,
                event: MotionEvent?
            ): Boolean {
                when(event?.action){
                    MotionEvent.ACTION_DOWN ->{
                        dealWithControlAction(direction)
                    }
                    MotionEvent.ACTION_UP,MotionEvent.ACTION_CANCEL->{
                        operateRobot(gimbalStop)
                    }
                }
                return true
            }
        })
    }

    fun dealWithControlAction(direction: RockerView.Direction?){
        when(direction?.name){
            "DIRECTION_LEFT"-> operateRobot(speedLeftEp)
            "DIRECTION_RIGHT" -> operateRobot(speedRightEp)
            "DIRECTION_UP" -> operateRobot(speedTopEp)
            "DIRECTION_DOWN" -> operateRobot(speedDownEp)
            "DIRECTION_UP_LEFT" -> operateRobot(goTopleftEp)
            "DIRECTION_UP_RIGHT" -> operateRobot(goToprightEp)
            "DIRECTION_DOWN_LEFT" -> operateRobot(goDownLeftEp)
            "DIRECTION_DOWN_RIGHT" -> operateRobot(goDownRightEp)
        }
    }
    fun initGameMenuView() {
        var drawItemBeans = ArrayList<DrawableMenuLayout.DrawItemBean>()
        for (i in 1..3) {
            var drawItemBean = DrawableMenuLayout.DrawItemBean()
            when (i) {
                1 -> drawItemBean.itemImgSrc = R.mipmap.game_icon_setting
                2 -> drawItemBean.itemImgSrc = R.mipmap.game_icon_desc
                3 -> drawItemBean.itemImgSrc = R.mipmap.game_icon_quitroom
            }
            drawItemBeans.add(drawItemBean)
        }
        binding.menuPanel.drawItemBeans = drawItemBeans
        binding.menuPanel.setOnItemClickListener {
            when (it) {
                0 -> openSet()
                1 -> openGameDesc()
                2 -> quitGameRoom()
            }
        }
    }

    fun btnStartGame(view: View) {
//        startGame()
        joinQueue()
    }

    fun openSet() {

    }

    fun openGameDesc() {

    }

    fun quitGameRoom() {

    }

    fun initGameVideo(data: RoomInfoQuery.List) {
        if (gameVideoControlor == null) {
            gameVideoControlor = DaniuGameVideoControlor()
            var bundle = Bundle()
            bundle.putString(
                DaniuGameVideoControlor.MASTER_VIDEO_URL,
                data?.fragments()?.roomFragment()?.liveStream()?.get(0)?.fragments()?.liveStreamforGameFragment()?.liveRtmpUrl()
            )
            bundle.putString(DaniuGameVideoControlor.SLAVE_VIDEO_URL, "")
            (gameVideoControlor as DaniuGameVideoControlor)?.arguments = bundle
//        mLiveGameController.setLiveStreamUrl(mLiveBean.liveStreamV2List.get(streamDefaultQuility).liveRtmpUrl, null);
            var ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            ft.replace(R.id.stream_replaced, gameVideoControlor as DaniuGameVideoControlor)
                .commitAllowingStateLoss()
        }
    }

    fun operateRobot(command: String) {
        var data = JSONObject()
        var params = JSONObject()
        data.put("id", GameSocketManager.generateId().toString())
        data.put("method", "robot_command")
        params.put("command", command)
        data.put("params", params)
        GameSocketManager.getInstance()
            .sendMessage("game", data, object : GameSocketManager.Callback {
                override fun onSuccess(jsonStr: JSONObject?) {
                    LogUtils.d(TAG, "operateRobot_success")
                }

                override fun onError(errorCode: Int, errorMsg: String?) {
                    LogUtils.d(TAG, "operateRobot_error")
                }
            })
    }

    override fun onEpEvent(msg: JSONObject?) {
        LogUtils.d(TAG, "onEpEvent")
        runOnUiThread{

        }
    }

    override fun onEpGameOver(msg: JSONObject?) {
        LogUtils.d(TAG, "onEpGameOver")
        runOnUiThread{

        }
    }

    override fun onGameReady(timeLeft: Int) {
        super.onGameReady(timeLeft)
        LogUtils.d(TAG, "onGameReady")
        runOnUiThread{
            if (timeLeft == 9) {
                startGame()
            }
        }
    }

    override fun onIMNotify(jsondata: JSONObject?) {
        super.onIMNotify(jsondata)
        runOnUiThread{

        }
    }

    override fun onRoomUserAmountChanged(jsondata: JSONObject?) {
        super.onRoomUserAmountChanged(jsondata)
        runOnUiThread{
            var gson=Gson()
            var gameRoomUsers: GameRoomUsers?=null
            val type: Type = object : TypeToken<GameRoomUsers?>() {}.type
            gameRoomUsers=gson.fromJson(jsondata?.toString(),GameRoomUsers::class.java)
            if (gameRoomUsers != null){
                LogUtils.d(TAG,"onRoomUserAmountChanged--")
                if (gameRoomUsers.user_list.size>4){
                    gameOnlineUserListAdapter?.list=gameRoomUsers.user_list.subList(0,3)
                    gameOnlineUserListAdapter?.notifyDataSetChanged()
                }else{
                    gameOnlineUserListAdapter?.list=gameRoomUsers.user_list
                    gameOnlineUserListAdapter?.notifyDataSetChanged()
                }
                binding.userTotal.setText("${gameRoomUsers.user_list.size} Online")
            }
        }
    }

    override fun startGame() {
        runOnUiThread{
            var data = JSONObject()
            data.put("id", GameSocketManager.generateId().toString())
            data.put("method", "start_game")
            GameSocketManager.getInstance()
                .sendMessage("game", data, object : GameSocketManager.Callback {
                    override fun onSuccess(jsonStr: JSONObject?) {
                        LogUtils.d(TAG, "startgame--success")
                        //发个指令测试
                        val ep = "robot mode gimbal_lead;"
                        operateRobot(ep)
                    }

                    override fun onError(errorCode: Int, errorMsg: String?) {
                        LogUtils.d(TAG, "startgame--falure" + errorMsg)
                    }
                })
        }
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): RobotGameViewModel {
        val robotGameActivityViewModel: RobotGameViewModel by viewModels()
        return robotGameActivityViewModel
    }


}