package com.wawa.wawaandroid_ep.activity.game

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.PopupWindow
import androidx.activity.viewModels
import androidx.databinding.Observable
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ScreenUtils
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
import com.wawa.baselib.utils.socketio.listener.FishGameListener
import com.wawa.wawaandroid_ep.activity.viewmodule.FishGameViewModel
import com.wawa.wawaandroid_ep.adapter.GameOnlineUserListAdapter
import com.wawa.wawaandroid_ep.adapter.viewmodel.ChatItemPlayerVM
import com.wawa.wawaandroid_ep.adapter.viewmodel.ChatItemViewModel
import com.wawa.wawaandroid_ep.bean.game.FishGameBuyTime
import com.wawa.wawaandroid_ep.bean.game.GameRoomChatDataBean
import com.wawa.wawaandroid_ep.bean.game.GameRoomUsers
import com.wawa.wawaandroid_ep.bean.game.SocketBaseBean
import com.wawa.wawaandroid_ep.dialog.game.*
import com.wawa.wawaandroid_ep.sound.SoundManager
import com.wawa.wawaandroid_ep.view.ButtonControlPanel
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
    ,GameQuit_PortDialog.GameQuitDialogCallback, FishGameListener {
    val TAG="FishGameRoomActivity"
    private var isCameraShow=false
    private var gameFishPrizeDialog: GameFishPrizeDialog?=null
    private var gameReadyDialog: GameReadyDialog?=null
    private var gifDialog: GifDialog?= null
    val chatAdapter= ArrayListAdapter<GameRoomChatDataBean>()
    private  var popupGameSetWindow: PopupWindow?=null
    var gameSetGroupViewControlor: GameSetGroupViewControlor?=null
    var cameraDerection: RockerView.Direction?=null
    var fishingDerection: RockerView.Direction?= null
    val screenWidth=ScreenUtils.getScreenWidth()
    var x=0f
    var distance=0f
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

    override fun onGamePlaying(jsonData: JSONObject?) {
        super.onGamePlaying(jsonData)
        runOnUiThread {
            jsonData?.let {
                val userCoin=jsonData?.getInt("user_coin")
                val userPoint=jsonData?.getInt("user_point")
                val gameRecordId=jsonData?.getInt("game_record_id")
                val totalWeight=jsonData?.getInt("total_weight")
                val totalNumber=jsonData?.getInt("total_number")
                viewModel.fishOnPrizeTips.set("渔获 ")
            }

        }
    }

    override fun onHook(msg: JSONObject?) {
        //上钩了
        viewModel.soundManager?.playSound(SoundManager.FISH_ONHOOK)
        Log.d(TAG,"onHook--"+ (viewModel.soundManager== null))
        if (gifDialog == null){
            gifDialog=GifDialog()
            gifDialog!!.dialogWidth=0
            gifDialog!!.dialogHeight=0
        }
        if (!gifDialog!!.isAdded){
            gifDialog!!.showDialog(supportFragmentManager,"GifDialog")
        }
    }

    override fun onFishPrize(msg: JSONObject?) {
        runOnUiThread {
            viewModel.soundManager?.playSound(SoundManager.FISH_PRIZE)
            val weight=msg?.getInt("weight")
            val totalWeight=msg?.getInt("total_weight")
            val totalNumber=msg?.getInt("total_number")
            //显示渔获弹窗
            if (gameFishPrizeDialog == null){
                gameFishPrizeDialog= GameFishPrizeDialog()
            }
            if (!gameFishPrizeDialog!!.isAdded){
                gameFishPrizeDialog!!.showDialog(supportFragmentManager,"GameFishPrizeDialog")
            }
            if (gameFishPrizeDialog!!.isAdded){
                gameFishPrizeDialog!!.viewModel.gameFishPrizeTips.set("恭喜中鱼，重${weight}斤")
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
        initViewObservable()
        initGameMenuView()
        initChatView()
        initOnlineUserView()
        initGameControler()
        /*for (i in 0..3){
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
        */

    }

    fun initViewObservable(){
        mGameStatus.addOnPropertyChangedCallback(object: Observable.OnPropertyChangedCallback(){
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                initGameMenuData()
            }
        })
        FishGameViewModel.stopSound.observe(this, Observer {
            Log.d(TAG,"fishOnHook__observe"+it)
            if (it){
                viewModel.soundManager?.stopSound()
            }
        })
    }

    fun initGameControler(){
        val minDistance=screenWidth/3
        binding.streamReplaced.setOnTouchListener { v, event ->
            when(event.action){
                MotionEvent.ACTION_DOWN->{
                    Log.d(TAG,"streamReplaced--onTouchDown"+"-X-"+event.getX() + "-Y-"+event.getY()+"-LawX-"+event.rawX+"-LawY-"+event.rawY+
                    "screenWidth"+screenWidth)
                    x=event.getX()
                }
                MotionEvent.ACTION_MOVE ->{
                    distance=event.getX()-x
                    var transX=binding.lvGameNotes.translationX
                    Log.d(TAG,"streamReplaced--onTouchMove"+"-X-"+event.getX() + "-Y-"+event.getY()+"-LawX-"+event.rawX+"-LawY-"+event.rawY
                            +"transX"+transX+"distance"+distance)

                    if (distance >0){
                        if (transX < screenWidth){
                            if (transX+distance >= screenWidth){
                                binding.lvGameNotes.translationX=screenWidth.toFloat()
                            }else{
                                binding.lvGameNotes.translationX=transX+distance
                            }

                        }
                    }
                    if (distance < 0){
                        if (transX > 0){
                            if(distance+transX<=0){
                                binding.lvGameNotes.translationX=0f
                            }else{
                                binding.lvGameNotes.translationX=distance+transX
                            }
                        }
                    }
                    x=event.getX()
                }
                MotionEvent.ACTION_UP,MotionEvent.ACTION_CANCEL ->{
                    Log.d(TAG,"streamReplaced--onTouchUp"+"-X-"+event.getX() + "-Y-"+event.getY()+"-LawX-"+event.rawX+"-LawY-"+event.rawY)
//                    var distance=event.getX()-x
                    var transX=binding.lvGameNotes.translationX
                    if (distance > 0) {
                        if (transX >= minDistance){
                            ObjectAnimator.ofFloat(binding.lvGameNotes,"translationX",
                                screenWidth.toFloat()).apply{
                                duration=300
                                addListener(object : AnimatorListenerAdapter(){
                                    override fun onAnimationEnd(animation: Animator?) {
                                        super.onAnimationEnd(animation)
                                    }
                                })
                                start()
                            }
                        }else{
                            ObjectAnimator.ofFloat(binding.lvGameNotes,"translationX",
                                0f).apply{
                                duration=300
                                addListener(object : AnimatorListenerAdapter(){
                                    override fun onAnimationEnd(animation: Animator?) {
                                        super.onAnimationEnd(animation)
                                    }
                                })
                                start()
                            }
                        }
                    }
                    if (distance < 0){
                        if (transX <= 2*minDistance){
                            ObjectAnimator.ofFloat(binding.lvGameNotes,"translationX",
                                0f).apply{
                                duration=300
                                addListener(object : AnimatorListenerAdapter(){
                                    override fun onAnimationEnd(animation: Animator?) {
                                        super.onAnimationEnd(animation)
                                    }
                                })
                                start()
                            }
                        }else{
                            ObjectAnimator.ofFloat(binding.lvGameNotes,"translationX",
                                screenWidth.toFloat()).apply{
                                duration=300
                                addListener(object : AnimatorListenerAdapter(){
                                    override fun onAnimationEnd(animation: Animator?) {
                                        super.onAnimationEnd(animation)
                                    }
                                })
                                start()
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

    private fun initGameMenuData(){
        val popGameList: MutableList<PopGameItemBean> = mutableListOf()
        for (i in 0..7){
            val popGameItemBean=PopGameItemBean()
            when(i){
                0->{
                    popGameItemBean.enableTab=mGameStatus.get()== GAME_STATUS_PLAYING
                    if (mGameStatus.get() ==GAME_STATUS_PLAYING){
                        popGameItemBean.imgRes=R.drawable.gamereset_bg
                    }else{
                        popGameItemBean.imgRes=R.mipmap.im_resetfishgame_unable
                    }
                }
                1 ->{
                    popGameItemBean.enableTab=mGameStatus.get()==GAME_STATUS_PLAYING
                    if (mGameStatus.get()==GAME_STATUS_PLAYING){
                        popGameItemBean.imgRes=R.drawable.continute_gametime_bg
                    }else{
                        popGameItemBean.imgRes=R.mipmap.im_continutetime_unable
                    }
                }
                2 ->{
                    popGameItemBean.enableTab=true
                    popGameItemBean.imgRes=R.drawable.fishgame_menu_topup_bg
                }
                3->{
                    popGameItemBean.enableTab=true
                    popGameItemBean.imgRes=R.drawable.gamemenu_nav_bg
                }
                /*4->{
                    popGameItemBean.enableTab=true
                    popGameItemBean.imgRes=R.drawable.dsrank_bg
                }*/
                5->{
                    popGameItemBean.enableTab=true
                    popGameItemBean.imgRes=R.drawable.gamefix_bg
                }
                4->{
                    popGameItemBean.enableTab=true
                    popGameItemBean.imgRes=R.drawable.gameseting_bg
                }

                6->{
                    popGameItemBean.enableTab=true
                    popGameItemBean.imgRes=R.drawable.gamecs_bg
                }
                7->{
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
    }

    private fun initGameMenuView(){
        initGameMenuData()
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

    fun fishBuyTime(num: Int){
        val fishGameBuyTime=FishGameBuyTime()
        fishGameBuyTime.number=num
        val socketBean=SocketBaseBean<FishGameBuyTime>()
        socketBean.id=GameSocketManager.generateId().toString()
        socketBean.method="renew"
        socketBean.params=fishGameBuyTime
        val jsonStr=Gson().toJson(socketBean)
        val data=JSONObject(jsonStr)
        GameSocketManager.getInstance()
            .sendMessage("game", data, object : GameSocketManager.Callback {
                override fun onSuccess(jsonStr: JSONObject?) {
                    LogUtils.d(TAG, "fishBuyTime_success")
                }

                override fun onError(errorCode: Int, errorMsg: String?) {
                    LogUtils.d(TAG, "fishBuyTime_error")
                }
            })

    }

    override fun gameSetClick(pos: Int) {
        when(pos){
            0->{
                //钓鱼机复位操作
                val gameFishResetDialog= GameFishResetDialog()
                gameFishResetDialog.showDialog(supportFragmentManager,"GameFishResetDialog")
            }
            1->{
                //续时
                val gameFishBuyTimeDialog=GameFishBuyTimeDialog()
                gameFishBuyTimeDialog.showDialog(supportFragmentManager,"GameFishBuyTimeDialog")
            }
            2->{
                //充值
                showTopUpDialog()
            }
            3->{
                //指南

            }

            5->{
                //维修
                val feedbackDialog=GameFeedBackDialog()
                ROOM_ID?.let {
                    feedbackDialog.roomId=it.toInt()
                }
                feedbackDialog.machineType=""
                feedbackDialog.showDialog(supportFragmentManager,GameFeedBackDialog.TAG)
            }
            4->{
                //设置
                val gameSetDialog= GameSettingDialog()
                if (!gameSetDialog.isAdded){
                    gameSetDialog.showDialog(supportFragmentManager,"GameSettingDialog")
                }
            }

            6->{
                //客服
            }
            7->{
                //聊天
                val inputDialog= InputFragmentDialog()
                inputDialog.showDialog(supportFragmentManager,InputFragmentDialog.TAG)
            }
        }
    }



    override fun gameStartBtnBg(): Int {
//        return R.drawable.startgame_btn_bg
        return R.drawable.fishgame_startgame_bg
    }

    override fun gameCancelBtnBg(): Int {
//        return R.drawable.cancel_yuyue_btbg
        return R.drawable.fishgame_cancelgame_bg
    }

    override fun gameQueueBtnBg(): Int {
//        return R.drawable.gamequeue_btn_bg
        return R.drawable.fishgame_queue_bg
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

    override fun showTopUpDialog() {
        val payDialog= PayPortDialog_V2()
        payDialog.showDialog(supportFragmentManager,PayPortDialog.TAG)
    }

    override fun showTopUpTipsDialog() {
        val noCoinsTipsDialog= NoCoinsTipsDialog()
        if (!noCoinsTipsDialog.isAdded){
            noCoinsTipsDialog.showDialog(supportFragmentManager,"NoCoinsTipsDialog")
        }
    }

}