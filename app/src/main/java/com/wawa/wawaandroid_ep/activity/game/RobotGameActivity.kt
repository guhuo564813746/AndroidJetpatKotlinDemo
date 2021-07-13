package com.wawa.wawaandroid_ep.activity.game

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.robotwar.app.BR
import com.robotwar.app.R
import com.robotwar.app.databinding.RobotGameActivityLayBinding
import com.wawa.baselib.utils.dialog.ConfirmDialogFatory
import com.wawa.baselib.utils.dialog.GameOperationDialog
import com.wawa.baselib.utils.dialog.GameReadyDialog
import com.wawa.baselib.utils.logutils.LogUtils
import com.wawa.baselib.utils.socketio.GameSocketManager
import com.wawa.baselib.utils.socketio.listener.EpGameListener

import com.wawa.wawaandroid_ep.MainViewModule
import com.wawa.wawaandroid_ep.activity.viewmodule.RobotGameViewModel
import com.wawa.wawaandroid_ep.adapter.GameOnlineUserListAdapter
import com.wawa.wawaandroid_ep.adapter.LiveChatListAdapter
import com.wawa.wawaandroid_ep.bean.game.GameRoomChatDataBean
import com.wawa.wawaandroid_ep.bean.game.GameRoomUsers
import com.wawa.wawaandroid_ep.dialog.game.GameQuit_PortDialog
import com.wawa.wawaandroid_ep.dialog.game.GameSetDialog
import com.wawa.wawaandroid_ep.view.DrawableMenuLayout
import com.wawa.wawaandroid_ep.view.RobotControlerView
import com.wawa.wawaandroid_ep.view.RockerView
import com.wawa.wawaandroid_ep.view.recycleview.NoAlphaItemAnimator
import org.json.JSONObject
import java.lang.reflect.Type
import java.math.RoundingMode
import java.text.DecimalFormat


/**
 *作者：create by 张金 on 2021/2/3 14:27
 *邮箱：564813746@qq.com
 */
class RobotGameActivity : GameBaseActivity<RobotGameActivityLayBinding,RobotGameViewModel>(), EpGameListener
    ,GameReadyDialog.GameReadyInterface, GameQuit_PortDialog.GameQuitDialogCallback {
    private val TAG = "RobotGameActivity"
    private var gameReadyDialog: GameReadyDialog?=null
    var chatListAdapter: LiveChatListAdapter?= null
    private var halfHeight=0
    private var halfWidth=0
    private var chassisSpeed="0.0"
    private var gimbalSpeed="0"
    private var rockerWidth=0.0

    private var perDistance=SizeUtils.dp2px(75f)/5;
    private var format= DecimalFormat("0.#")
    private var gimbalWidthPerDistance=0.0
    private var gimbalHeightPerDistance=0.0

    private var curRockerDistance=0
    private var curBtControlorDisance=0
    //Ep指令
    private var goTopleftEp: String = "chassis speed x $chassisSpeed y $chassisSpeed z 1;"
    private var goToprightEp = "chassis speed x -$chassisSpeed y $chassisSpeed z 1;"
    private var speedTopEp = "chassis speed x $chassisSpeed y 0 z 1;"
    private var speedDownEp = "chassis speed x -$chassisSpeed y 0 z 1;"
    private var speedLeftEp = "chassis speed x 0 y -$chassisSpeed z 1;"
    private var speedRightEp = "chassis speed x 0 y $chassisSpeed z 1;"
    private var goDownLeftEp = "chassis speed x -$chassisSpeed y -$chassisSpeed z 1;"
    private var goDownRightEp = "chassis speed x -$chassisSpeed y $chassisSpeed z 1;"

    //云台速度控制
    private var gimbalUp = "gimbal speed p $gimbalSpeed y 0;"
    private var gimbalDown = "gimbal speed p -$gimbalSpeed y 0;"
    private var gimbalLeft = "gimbal speed p 0 y -$gimbalSpeed;"
    private var gimbalRight = "gimbal speed p 0 y $gimbalSpeed;"
    private val gimbalStop = "gimbal speed p 0 y 0;"

    private val stopSpeed = "chassis speed x 0 y 0 z 0;"
    private val fire_num = "blaster bead 2;"
    private val fire = "blaster fire;"

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.robot_game_activity_lay
    }

    override fun initView() {
        super.initView()
        format.roundingMode= RoundingMode.FLOOR
        rockerWidth=SizeUtils.dp2px(75f).toDouble()
        halfHeight=ScreenUtils.getScreenHeight()/2
        halfWidth=ScreenUtils.getScreenWidth()/4
        gimbalWidthPerDistance=halfWidth.toDouble()/4
        gimbalHeightPerDistance=halfHeight.toDouble()/4
        binding.epButtonControl.layoutParams.width=ScreenUtils.getScreenWidth()/2
        binding.streamReplaced.setOnClickListener {
            switchShowUserData()
        }
        initGameMenuView()
        initGameControlView()
        initChatView()
        initOnlineUserView()
//        binding.streamReplaced

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    fun switchShowUserData(){
        if (viewModel.userDataGroupVisibility.get()==View.GONE){
            viewModel.userDataGroupVisibility.set(View.VISIBLE)
        }else{
            viewModel.userDataGroupVisibility.set(View.GONE)
        }
    }

    fun initChatView(){
        binding.chatList.setHasFixedSize(true)
        binding.chatList.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        binding.chatList.itemAnimator
        chatListAdapter= LiveChatListAdapter(this)
        binding.chatList.itemAnimator=NoAlphaItemAnimator()
        binding.chatList.adapter=chatListAdapter
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

    fun initSpeed(){
        //Ep指令
        goTopleftEp= "chassis speed x $chassisSpeed y -$chassisSpeed z 1;"
        goToprightEp = "chassis speed x $chassisSpeed y $chassisSpeed z 1;"
        speedTopEp = "chassis speed x $chassisSpeed y 0 z 1;"
        speedDownEp = "chassis speed x -$chassisSpeed y 0 z 1;"
        speedLeftEp = "chassis speed x 0 y -$chassisSpeed z 1;"
        speedRightEp = "chassis speed x 0 y $chassisSpeed z 1;"
        goDownLeftEp = "chassis speed x -$chassisSpeed y -$chassisSpeed z 1;"
        goDownRightEp = "chassis speed x -$chassisSpeed y $chassisSpeed z 1;"

        //云台速度控制
        gimbalUp = "gimbal speed p $gimbalSpeed y 0;"
        gimbalDown = "gimbal speed p -$gimbalSpeed y 0;"
        gimbalLeft = "gimbal speed p 0 y -$gimbalSpeed;"
        gimbalRight = "gimbal speed p 0 y $gimbalSpeed;"
        LogUtils.d(TAG,"initSpeed--$goTopleftEp -- $gimbalUp")
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
                curRockerDistance=0
                operateRobot(stopSpeed)
            }

            override fun direction(direction: RockerView.Direction?,distance: Int) {

                LogUtils.d(TAG,"epRockerControl--distance--$distance -- perDistance-- $perDistance")
                if (Math.abs(distance-curRockerDistance) >= perDistance ){
                    if (distance <= rockerWidth){
                        chassisSpeed=format.format(distance/rockerWidth)
                    }else{
                        chassisSpeed="1.0"
                    }
                    initSpeed()
                    dealWithControlAction(direction)
                    curRockerDistance=distance
                }

            }

            override fun onStart() {
                LogUtils.d(TAG,"rockerControl--start")
            }
        })
        binding.epButtonControl.mOnShakeListener=object: RobotControlerView.OnShakeListener {
            override fun onStart() {
                LogUtils.d(TAG,"epButtonControl--start")
            }

            override fun direction(direction: RobotControlerView.Direction?,distance: Int) {
                LogUtils.d(TAG,"epButtonControl--direction--distance--$distance")

                direction?.let {
                    dealWithBtControlAction(it,distance)
                }
            }

            override fun onFinish() {
                LogUtils.d(TAG,"epButtonControl_onFinish--")
                curBtControlorDisance=0
                operateRobot(gimbalStop)
            }
            /* override fun onTouch(
                 view: View?,
                 direction: RockerView.Direction?,
                 event: MotionEvent?
             ): Boolean {
                 when (event?.action) {
                     MotionEvent.ACTION_DOWN -> {
                         dealWithBtControlAction(direction)
                     }
                     MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                         operateRobot(gimbalStop)
                     }
                 }
                 return true
             }*/
        }
    }

    fun dealWithBtControlAction(direction: RobotControlerView.Direction?,distance: Int){
        when(direction?.name){
            "DIRECTION_LEFT"-> {
                LogUtils.d(TAG,"dealWithBtControlAction--DIRECTION_LEFT")
                if (Math.abs(distance-curBtControlorDisance) >= gimbalWidthPerDistance){
                    if (distance <= halfWidth){
                        gimbalSpeed=(360*distance/halfWidth).toString()
                    }else{
                        gimbalSpeed="360"
                    }
                    LogUtils.d(TAG,"gimbalSpeed-- $gimbalSpeed")
                    initSpeed()
                    operateRobot(gimbalLeft)
                    curBtControlorDisance=distance
                }

            }
            "DIRECTION_RIGHT" -> {
                LogUtils.d(TAG,"dealWithBtControlAction--DIRECTION_RIGHT")

                if (Math.abs(distance-curBtControlorDisance) >= gimbalWidthPerDistance){
                    if (distance <= halfWidth){
                        gimbalSpeed=(360*distance/halfWidth).toString()
                    }else{
                        gimbalSpeed="360"
                    }
                    initSpeed()
                    operateRobot(gimbalRight)
                    curBtControlorDisance=distance
                }
            }
            "DIRECTION_UP" ->{
                LogUtils.d(TAG,"dealWithBtControlAction--DIRECTION_UP")
                if (Math.abs(distance -curBtControlorDisance) >= gimbalHeightPerDistance){
                    if (distance <= halfHeight){
                        gimbalSpeed=(360*distance/halfHeight).toString()
                    }else{
                        gimbalSpeed="360"
                    }
                    initSpeed()
                    operateRobot(gimbalUp)
                    curBtControlorDisance=distance
                }
//                gimbalSpeed=360*(distance/halfHeight.toDouble()).toInt()
//                operateRobot(gimbalUp)
            }
            "DIRECTION_DOWN" ->{
                LogUtils.d(TAG,"dealWithBtControlAction--DIRECTION_DOWN")
                if (Math.abs(distance -curBtControlorDisance) >= gimbalHeightPerDistance){
                    if (distance <= halfHeight){
                        gimbalSpeed=(360*distance/halfHeight).toString()
                    }else{
                        gimbalSpeed="360"
                    }
                    initSpeed()
                    operateRobot(gimbalDown)
                    curBtControlorDisance=distance
                }
            }
        }
    }

    fun dealWithControlAction(direction: RockerView.Direction?){
        when(direction?.name){
            "DIRECTION_LEFT"-> {
                LogUtils.d(TAG,"DIRECTION_LEFT--")
                operateRobot(speedLeftEp)
            }
            "DIRECTION_RIGHT" -> {
                LogUtils.d(TAG,"DIRECTION_RIGHT")
                operateRobot(speedRightEp)
            }
            "DIRECTION_UP" -> {
                LogUtils.d(TAG,"DIRECTION_UP")
                operateRobot(speedTopEp)
            }
            "DIRECTION_DOWN" -> {
                LogUtils.d(TAG,"DIRECTION_DOWN")
                operateRobot(speedDownEp)
            }
            "DIRECTION_UP_LEFT" -> {
                LogUtils.d(TAG,"DIRECTION_UP_LEFT")
                operateRobot(goTopleftEp)
            }
            "DIRECTION_UP_RIGHT" -> {
                LogUtils.d(TAG,"DIRECTION_UP_RIGHT")
                operateRobot(goToprightEp)
            }
            "DIRECTION_DOWN_LEFT" -> {
                LogUtils.d(TAG,"DIRECTION_DOWN_LEFT")
                operateRobot(goDownLeftEp)
            }
            "DIRECTION_DOWN_RIGHT" -> {
                LogUtils.d(TAG,"DIRECTION_DOWN_RIGHT")
                operateRobot(goDownRightEp)
            }
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
                2 -> onBackPressed()
            }
        }
    }



    fun openSet() {
        var gameSetDialog= GameSetDialog()
        gameSetDialog.showDialog(supportFragmentManager,"GameSetDialog")
    }

    fun openGameDesc() {
        var operationDialog=GameOperationDialog()
        var bundle=Bundle()
        bundle.putString(GameOperationDialog.DIALOG_TITLE,getString(R.string.tx_owngamerules_tips))
        bundle.putString(GameOperationDialog.DIALOG_URL,MainViewModule.configData?.page()?.fragments()?.pageOptionFragment()?.robotGameRuleUrl())
        operationDialog.arguments=bundle
        operationDialog.showDialog(supportFragmentManager,"GameOperationDialog")

    }

    fun quitGameRoom() {
        var quitDialog=ConfirmDialogFatory(getString(R.string.confirm),getString(R.string.cancel),getString(R.string.tip),getString(R.string.GAME_QUIT_CONFIRM))
        quitDialog.dialogSelectInterface=object : ConfirmDialogFatory.DialogSelectInterface{
            override fun onPositiveClick() {
                quitDialog.dismissAllowingStateLoss()
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

            override fun onNegativeClick() {
                quitDialog.dismissAllowingStateLoss()
            }

        }
        quitDialog.showConfirmDialog(supportFragmentManager)
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

        }
    }

    override fun onIMNotify(jsondata: JSONObject?) {
        super.onIMNotify(jsondata)
        LogUtils.d(TAG,"onIMNotify--")
        var gson=Gson()
        var gameRoomChatDataBean:GameRoomChatDataBean?= null
        gameRoomChatDataBean=gson.fromJson(jsondata?.toString(),GameRoomChatDataBean::class.java)
        runOnUiThread{
            if (gameRoomChatDataBean != null){
                chatListAdapter?.insertItem(gameRoomChatDataBean.msg_list)
            }
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


    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): RobotGameViewModel {
        val robotGameActivityViewModel: RobotGameViewModel by viewModels()
        return robotGameActivityViewModel
    }

    override fun showGameReadyDialog(timeLeft: Int) {
        //显示游戏准备弹窗
        if (gameReadyDialog == null){
            gameReadyDialog= GameReadyDialog()
        }
        gameReadyDialog?.gameReadyInterface=this
        gameReadyDialog?.showDialog(supportFragmentManager,"GameReadyDialog")
        if(gameReadyDialog!!.isAdded){
            gameReadyDialog?.setTimes(timeLeft)
        }
    }

    override fun continuteGame() {
        startGame()
        gameReadyDialog=null
    }

    override fun cancelGame() {
        quitQueue()
        gameReadyDialog=null
    }

    override fun gameStartBtnBg(): Int {
        return R.drawable.btn_start_game
    }

    override fun gameCancelBtnBg(): Int {
        return R.drawable.btn_cancel_game
    }

    override fun gameQueueBtnBg(): Int {
        return R.drawable.pre_game_bg
    }

    override fun initStartGame() {
        val ep = "robot mode gimbal_lead;"
        operateRobot(ep)
    }

    override fun back(view: View) {
        onBackPressed()
    }

    override fun showQuitDislog() {
        val quitDialog= GameQuit_PortDialog()
        quitDialog.isQuit=true
        quitDialog.listener=this
        quitDialog.showDialog(supportFragmentManager, GameQuit_PortDialog.TAG)
    }

    override fun onQuitGame() {
        endGame()
    }

    override fun showTopUpDialog() {

    }


}