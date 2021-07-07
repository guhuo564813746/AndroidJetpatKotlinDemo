package com.wawa.wawaandroid_ep.activity.game

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.RelativeLayout
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.coinhouse777.wawa.widget.popgame.GameSetGroupViewControlor
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.robotwar.app.BR
import com.robotwar.app.R
import com.robotwar.app.databinding.FishgameRoomActivityLayBinding
import com.wawa.baselib.utils.AppUtils
import com.wawa.baselib.utils.baseadapter.imp.ArrayListAdapter
import com.wawa.baselib.utils.dialog.GameReadyDialog
import com.wawa.baselib.utils.logutils.LogUtils
import com.wawa.baselib.utils.socketio.GameSocketManager
import com.wawa.wawaandroid_ep.activity.viewmodule.FishGameViewModel
import com.wawa.wawaandroid_ep.adapter.GameOnlineUserListAdapter
import com.wawa.wawaandroid_ep.adapter.viewmodel.ChatItemPlayerVM
import com.wawa.wawaandroid_ep.adapter.viewmodel.ChatItemViewModel
import com.wawa.wawaandroid_ep.bean.game.GameRoomChatDataBean
import com.wawa.wawaandroid_ep.bean.game.GameRoomChatItemBean
import com.wawa.wawaandroid_ep.bean.game.GameRoomUsers
import com.wawa.wawaandroid_ep.dialog.game.GameFeedBackDialog
import com.wawa.wawaandroid_ep.dialog.game.GameQuit_PortDialog
import com.wawa.wawaandroid_ep.dialog.game.InputFragmentDialog
import com.wawa.wawaandroid_ep.view.ButtonControlPanel
import com.wawa.wawaandroid_ep.view.RobotControlerView
import com.wawa.wawaandroid_ep.view.RockerView
import com.wawa.wawaandroid_ep.view.ViewUtils
import com.wawa.wawaandroid_ep.view.popgame.PopGameItemBean
import org.json.JSONObject
import java.lang.reflect.Type

/**
 *作者：create by 张金 on 2021/6/24 10:23
 *邮箱：564813746@qq.com
 */
class FishGameRoomActivity : GameBaseActivity<FishgameRoomActivityLayBinding, FishGameViewModel>() ,
    GameReadyDialog.GameReadyInterface , GameSetGroupViewControlor.GameViewClickCallback , View.OnTouchListener
    ,GameQuit_PortDialog.GameQuitDialogCallback{
    val TAG="FishGameRoomActivity"
    private var isCameraShow=false
    private var gameReadyDialog: GameReadyDialog?=null
    val chatAdapter= ArrayListAdapter<GameRoomChatDataBean>()
    private  var popupGameSetWindow: PopupWindow?=null
    var gameSetGroupViewControlor: GameSetGroupViewControlor?=null
    var cameraDerection: RockerView.Direction?=null
    var fishingDerection: RockerView.Direction?= null

    override fun onIMNotify(jsondata: JSONObject?) {
        super.onIMNotify(jsondata)
        LogUtils.d(TAG,"onIMNotify--")
        var gson=Gson()
        var gameRoomChatDataBean: GameRoomChatDataBean?= null
        gameRoomChatDataBean=gson.fromJson(jsondata?.toString(), GameRoomChatDataBean::class.java)
        runOnUiThread{
            if (gameRoomChatDataBean != null){
                if (gameRoomChatDataBean.source ==1){
                    //玩家
                    val chatItemPlayerVM = ChatItemPlayerVM()
                    chatItemPlayerVM.model=gameRoomChatDataBean
                    chatAdapter.add(chatItemPlayerVM)
                }else{
                    val  chatItemViewModel = ChatItemViewModel()
                    chatItemViewModel.model=gameRoomChatDataBean
                    chatAdapter.add(chatItemViewModel)
                }
                binding.lvGameNotes.smoothScrollToPosition(chatAdapter.itemCount-1)
//                chatListAdapter?.insertItem(gameRoomChatDataBean.msg_list)
            }
        }
    }

    override fun onRoomUserAmountChanged(jsondata: JSONObject?) {
        super.onRoomUserAmountChanged(jsondata)
        LogUtils.d(TAG,"onRoomUserAmountChanged--")
        runOnUiThread{
            var gson= Gson()
            var gameRoomUsers: GameRoomUsers?=null
            val type: Type = object : TypeToken<GameRoomUsers?>() {}.type
            gameRoomUsers=gson.fromJson(jsondata?.toString(), GameRoomUsers::class.java)
            if (gameRoomUsers != null){
                LogUtils.d(TAG,"onRoomUserAmountChanged--")
                if (gameRoomUsers.user_list.size>4){
                    gameOnlineUserListAdapter?.list=gameRoomUsers.user_list.subList(0,3)
                    gameOnlineUserListAdapter?.notifyDataSetChanged()
                }else{
                    gameOnlineUserListAdapter?.list=gameRoomUsers.user_list
                    gameOnlineUserListAdapter?.notifyDataSetChanged()
                }
                viewModel.roomUserAmountText.set("${gameRoomUsers.user_list.size} Online")
            }
        }
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

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): FishGameViewModel {
        val fishGameViewModel: FishGameViewModel by viewModels()
        return fishGameViewModel
    }

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.fishgame_room_activity_lay
    }

    override fun initView() {
        super.initView()
        initGameMenuView()
        initChatView()
        initOnlineUserView()
        initGameControler()
        for (i in 0..3){
            val gameRoomChatDataBean=GameRoomChatDataBean()
            gameRoomChatDataBean.user_nickname="test"
            gameRoomChatDataBean.source=i
            val bodyBean=GameRoomChatDataBean.MsgListBean()
            val body=GameRoomChatDataBean.MsgListBean.BodyBean()
            body.text="test msg content 开始加快建设深刻揭示了世界经济急急急 急急急就是斤斤计较急急急急急急急急急急急急急急急捐款活动介绍客户上课今后是否会回复"
            bodyBean.body=body
            gameRoomChatDataBean.msg_list= mutableListOf<GameRoomChatDataBean.MsgListBean>()
            gameRoomChatDataBean.msg_list.add(bodyBean)
            if (i==1){
                val chatItemPlayerVM = ChatItemPlayerVM()
                chatItemPlayerVM.model=gameRoomChatDataBean
                chatAdapter.add(chatItemPlayerVM)
            }else{
                val  chatItemViewModel = ChatItemViewModel()
                chatItemViewModel.model=gameRoomChatDataBean
                chatAdapter.add(chatItemViewModel)
            }

        }
        binding.lvGameNotes.postDelayed(Runnable {
            binding.lvGameNotes.smoothScrollToPosition(chatAdapter.itemCount-1)
        },200)
    }


    fun initGameControler(){
        val minDistance=ScreenUtils.getScreenWidth()/3

        viewModel.playerGameViewVisibility.set(View.VISIBLE)
        binding.streamReplaced.setOnTouchListener { v, event ->
            var x=0f
            var y=0f
            when(event.action){
                MotionEvent.ACTION_DOWN->{
                    Log.d(TAG,"streamReplaced--onTouchDown"+"-X-"+event.getX() + "-Y-"+event.getY()+"-LawX-"+event.rawX+"-LawY-"+event.rawY)
                    x=event.getX()
                    y=event.getY()
                }
                MotionEvent.ACTION_MOVE ->{
                    Log.d(TAG,"streamReplaced--onTouchMove"+"-X-"+event.getX() + "-Y-"+event.getY()+"-LawX-"+event.rawX+"-LawY-"+event.rawY)
                    var distance=event.getX()-x
                    var transX=binding.lvGameNotes.translationX
                    if (distance >0){
                        if (distance-ScreenUtils.getScreenWidth() >= 0){
                            distance=ScreenUtils.getScreenWidth().toFloat()
                        }
                    }
                    if (distance < 0){
                        if (Math.abs(distance)-ScreenUtils.getScreenWidth() >= 0){

                        }

                    }

                    if (binding.lvGameNotes.visibility== View.VISIBLE && distance >0){
                        binding.lvGameNotes.translationX=distance
                    }
                    if (binding.lvGameNotes.visibility==View.GONE && distance < 0){
                        binding.lvGameNotes.visibility=View.VISIBLE
                        binding.lvGameNotes.translationX=distance
                    }

                }
                MotionEvent.ACTION_UP,MotionEvent.ACTION_CANCEL ->{
                    Log.d(TAG,"streamReplaced--onTouchUp"+"-X-"+event.getX() + "-Y-"+event.getY()+"-LawX-"+event.rawX+"-LawY-"+event.rawY)
                    var distance=event.getX()-x
                    if (binding.lvGameNotes.visibility== View.VISIBLE ){
                        if (distance > 0) {
                            if (distance >= minDistance){
                                ObjectAnimator.ofFloat(binding.lvGameNotes,"translationX",
                                    ScreenUtils.getScreenWidth()-distance).apply{
                                    duration=500
                                    addListener(object : AnimatorListenerAdapter(){
                                        override fun onAnimationEnd(animation: Animator?) {
                                            super.onAnimationEnd(animation)
                                            binding.lvGameNotes.visibility=View.GONE
                                        }
                                    })
                                    start()
                                }
                            }else{
                                ObjectAnimator.ofFloat(binding.lvGameNotes,"translationX",
                                    -distance).apply{
                                    duration=500
                                    addListener(object : AnimatorListenerAdapter(){
                                        override fun onAnimationEnd(animation: Animator?) {
                                            super.onAnimationEnd(animation)
                                        }
                                    })
                                    start()
                                }
                            }
                        }
                    }else{
                        if (distance < 0){
                            if (Math.abs(distance) >= minDistance){
                                ObjectAnimator.ofFloat(binding.lvGameNotes,"translationX",
                                    -(ScreenUtils.getScreenWidth()-Math.abs(distance))).apply{
                                    duration=500
                                    addListener(object : AnimatorListenerAdapter(){
                                        override fun onAnimationEnd(animation: Animator?) {
                                            super.onAnimationEnd(animation)
                                        }
                                    })
                                    start()
                                }
                            }else{
                                ObjectAnimator.ofFloat(binding.lvGameNotes,"translationX",
                                    Math.abs(distance)).apply{
                                    duration=500
                                    addListener(object : AnimatorListenerAdapter(){
                                        override fun onAnimationEnd(animation: Animator?) {
                                            super.onAnimationEnd(animation)
                                            binding.lvGameNotes.visibility=View.GONE
                                        }
                                    })
                                    start()
                                }
                            }
                        }
                    }
                }
            }
            true
        }
        binding.tvGameFangxian.setOnTouchListener(this)
        binding.tvGameShouxian.setOnTouchListener(this)
        binding.btnCameraZoomOut.setOnTouchListener(this)
        binding.cameraZoomIn.setOnTouchListener(this)
        binding.canmeraControler.setListener(object: ButtonControlPanel.OnTouchListener {


            override fun onTouch(
                view: View?,
                direction: RockerView.Direction?,
                event: MotionEvent?
            ): Boolean {
                cameraDerection=direction
                when(event?.action){
                    MotionEvent.ACTION_DOWN ->{
                        when(cameraDerection?.name){
                            "DIRECTION_LEFT" ->{
                                controlCamera("left",1)
                            }
                            "DIRECTION_RIGHT" ->{
                                controlCamera("right",1)
                            }
                            "DIRECTION_UP" ->{
                                controlCamera("up",1)
                            }
                            "DIRECTION_DOWN" ->{
                                controlCamera("down",1)
                            }
                        }
                    }
                    MotionEvent.ACTION_UP,MotionEvent.ACTION_CANCEL ->{
                        when(cameraDerection?.name){
                            "DIRECTION_LEFT" ->{
                                controlCamera("left_r",1)
                            }
                            "DIRECTION_RIGHT" ->{
                                controlCamera("right_r",1)
                            }
                            "DIRECTION_UP" ->{
                                controlCamera("up_r",1)
                            }
                            "DIRECTION_DOWN" ->{
                                controlCamera("down_r",1)
                            }
                        }
                    }
                }
                return true
            }
        })
        binding.fishingControler.setListener(object : ButtonControlPanel.OnTouchListener {

            override fun onTouch(view: View?, direction: RockerView.Direction?, event: MotionEvent?): Boolean {
                fishingDerection=direction
                when(event?.action){
                    MotionEvent.ACTION_DOWN ->{
                        when(fishingDerection?.name){
                            "DIRECTION_LEFT" ->{
                                controlFishing("left")
                            }
                            "DIRECTION_RIGHT" ->{
                                controlFishing("right")
                            }
                            "DIRECTION_UP" ->{
                                controlFishing("up")
                            }
                            "DIRECTION_DOWN" ->{
                                controlFishing("down")
                            }
                        }
                    }
                    MotionEvent.ACTION_UP,MotionEvent.ACTION_CANCEL->{
                        when(fishingDerection?.name){
                            "DIRECTION_LEFT" ->{
                                controlFishing("left_r")
                            }
                            "DIRECTION_RIGHT" ->{
                                controlFishing("right_r")
                            }
                            "DIRECTION_UP" ->{
                                controlFishing("up_r")
                            }
                            "DIRECTION_DOWN" ->{
                                controlFishing("down_r")
                            }
                        }
                    }
                }
                return true
            }
        })
    }

    fun showCameraControler(view: View){
        //显示摄像头设置
        var fromX=0f
        var toX=0f

        if (isCameraShow){
            fromX=AppUtils.dp2px(this,120f).toFloat()
            toX=0f

        }else{
            fromX=0f
            toX=AppUtils.dp2px(this,120f).toFloat()
        }
        val objectAnimation= ObjectAnimator.ofFloat(binding.rlCameraContainer,"translationX",fromX,toX)
        Log.d(TAG,"showCameraControler--"+fromX+"--"+toX)
        objectAnimation.addListener(object: Animator.AnimatorListener{
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                val layoutParams=binding.rlCameraControlor.layoutParams
                if(isCameraShow){
//                    (layoutParams as RelativeLayout.LayoutParams).leftMargin=-AppUtils.dp2px(baseContext,110f)
                    isCameraShow=false
                }else{
//                    (layoutParams as RelativeLayout.LayoutParams).leftMargin=0
                    isCameraShow=true
                }
            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationStart(animation: Animator?) {

            }
        })
        objectAnimation.start()
    }

    fun casting(view: View){
        //抛杆
        controlFishing("casting")
    }

    fun finishHook(view: View){
        //提竿
        controlFishing("finish_hook")
    }

    fun addBait(view: View){
        //上饵
        controlFishing("add_bait")
    }
    fun unload(view: View){
        //摘鱼
        controlFishing("unload")
    }
    fun showPopSet(){
        popupGameSetWindow?.let {
            if (it.isShowing){
                it.dismiss()
            }else{
                it.showAsDropDown(binding.llGameMenu,0,AppUtils.dp2px(this,15f), Gravity.END)
            }
        }
    }
    private fun initGameMenuView(){
        val popGameList: MutableList<PopGameItemBean> = mutableListOf()
        for (i in 0..8){
            val popGameItemBean=PopGameItemBean()
            when(i){
                0 ->{
                    popGameItemBean.enableTab=true
                    popGameItemBean.imgRes=R.drawable.continute_gametime_bg
                }
                1 ->{
                    popGameItemBean.enableTab=true
                    popGameItemBean.imgRes=R.drawable.fishgame_menu_topup_bg
                }
                2->{
                    popGameItemBean.enableTab=true
                    popGameItemBean.imgRes=R.drawable.gamemenu_nav_bg
                }
                3->{
                    popGameItemBean.enableTab=true
                    popGameItemBean.imgRes=R.drawable.dsrank_bg
                }
                4->{
                    popGameItemBean.enableTab=true
                    popGameItemBean.imgRes=R.drawable.gamefix_bg
                }
                5->{
                    popGameItemBean.enableTab=true
                    popGameItemBean.imgRes=R.drawable.gameseting_bg
                }
                6->{
                    popGameItemBean.enableTab=true
                    popGameItemBean.imgRes=R.drawable.gamereset_bg
                }
                7->{
                    popGameItemBean.enableTab=true
                    popGameItemBean.imgRes=R.drawable.gamecs_bg
                }
                8->{
                    popGameItemBean.enableTab=true
                    popGameItemBean.imgRes=R.drawable.gamechat_bg
                }
            }
            popGameList.add(popGameItemBean)
        }
        if (gameSetGroupViewControlor== null){
            gameSetGroupViewControlor=GameSetGroupViewControlor(this,popGameList,this)
        }else{
            gameSetGroupViewControlor?.setPopGameSetListData(popGameList)
        }
        binding.llGameMenu.setOnClickListener {
            showPopSet()
        }
        val popWidth=AppUtils.dp2px(this,40f)
        val popHeight=AppUtils.dp2px(this,380f)
        gameSetGroupViewControlor?.let {
            popupGameSetWindow=ViewUtils(this).initPopupWindow(it.getPopDialog(), popWidth,popHeight,-1)
            popupGameSetWindow?.setOutsideTouchable(false)
            popupGameSetWindow?.setOnDismissListener {

            }
        }
    }

    private fun initChatView(){
        val chatLayoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        binding.lvGameNotes.bindAdapter(chatAdapter,chatLayoutManager)
    }

    private fun initOnlineUserView(){
        binding.userList.layoutManager=
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
        if (gameOnlineUserListAdapter == null){
            gameOnlineUserListAdapter= GameOnlineUserListAdapter(this,ArrayList())
        }
        binding.userList.adapter=gameOnlineUserListAdapter
    }

    override fun continuteGame() {
        startGame()
    }

    override fun cancelGame() {
        quitQueue()
        gameReadyDialog=null
    }

    override fun gameSetClick(pos: Int) {
        when(pos){
            0->{

            }
            1->{}
            2->{}
            3->{}
            4->{
                //维修
                val feedbackDialog=GameFeedBackDialog()
                ROOM_ID?.let {
                    feedbackDialog.roomId=it.toInt()
                }
                feedbackDialog.machineType=""
                feedbackDialog.showDialog(supportFragmentManager,GameFeedBackDialog.TAG)
            }
            5->{}
            6->{}
            7->{}
            8->{
                val inputDialog= InputFragmentDialog()
                inputDialog.showDialog(supportFragmentManager,InputFragmentDialog.TAG)
            }
        }
    }



    override fun gameStartBtnBg(): Int {
//        return R.drawable.startgame_btn_bg
        return R.mipmap.im_fish_startgame
    }

    override fun gameCancelBtnBg(): Int {
//        return R.drawable.cancel_yuyue_btbg
        return R.mipmap.im_cancel_fishgame
    }

    override fun gameQueueBtnBg(): Int {
//        return R.drawable.gamequeue_btn_bg
        return R.mipmap.im_prequeue_fishgame
    }

    override fun initStartGame() {

    }

    fun controlFishing(command: String){
        var data = JSONObject()
        var params = JSONObject()
        data.put("id", GameSocketManager.generateId().toString())
        data.put("method", "control")
        params.put("operation", command)
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

    fun controlCamera(command: String,cameraPos: Int){
        var data = JSONObject()
        var params = JSONObject()
        data.put("id", GameSocketManager.generateId().toString())
        data.put("method", "camera_control")
        params.put("serial_no",cameraPos)
        params.put("operation", command)
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

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN ->{
                v?.isSelected=true
                when(v?.id){
                    R.id.tv_game_fangxian ->{
                        controlFishing("increase_line")
                    }
                    R.id.tv_game_shouxian ->{
                        controlFishing("decrease_line")
                    }
                    R.id.btn_camera_zoom_out ->{
                        controlCamera("zoom_out",1)
                    }
                    R.id.camera_zoom_in ->{
                        controlCamera("zoom_in",1)
                    }

                }
            }
            MotionEvent.ACTION_UP,MotionEvent.ACTION_CANCEL ->{
                v?.isSelected=false
                when(v?.id){
                    R.id.tv_game_fangxian ->{
                        controlFishing("stop_line")
                    }
                    R.id.tv_game_shouxian ->{
                        controlFishing("stop_line")
                    }
                    R.id.btn_camera_zoom_out ->{
                        controlCamera("zoom_out_r",1)
                    }
                    R.id.camera_zoom_in ->{
                        controlCamera("zoom_in_r",1)
                    }
                }
            }
        }
        return true
    }

    override fun back(view: View) {
        super.back(view)
        onBackPressed()
    }

    override fun showQuitDislog() {
        val quitDialog= GameQuit_PortDialog()
        quitDialog.isQuit=true
        quitDialog.listener=this
        quitDialog.showDialog(supportFragmentManager,GameQuit_PortDialog.TAG)
    }

    override fun onQuitGame() {
        endGame()
    }

}