package com.wawa.wawaandroid_ep.activity.game

import android.os.Bundle
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.PopupWindow
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
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
    GameReadyDialog.GameReadyInterface , GameSetGroupViewControlor.GameViewClickCallback , View.OnTouchListener{
    val TAG="FishGameRoomActivity"
    private var gameReadyDialog: GameReadyDialog?=null
    val chatAdapter= ArrayListAdapter<GameRoomChatDataBean>()
    private  var popupGameSetWindow: PopupWindow?=null
    var gameSetGroupViewControlor: GameSetGroupViewControlor?=null
    var cameraDerection: RobotControlerView.Direction?=null
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
    }


    fun initGameControler(){
        binding.streamReplaced.setOnTouchListener { v, event ->
            when(event.action){

            }
        }
        binding.tvGameFangxian.setOnTouchListener(this)
        binding.tvGameShouxian.setOnTouchListener(this)
        binding.btnCameraZoomOut.setOnTouchListener(this)
        binding.cameraZoomIn.setOnTouchListener(this)
        binding.canmeraControler.mOnShakeListener=object: RobotControlerView.OnShakeListener {
            override fun onStart() {

            }

            override fun direction(direction: RobotControlerView.Direction?, distance: Int) {
                cameraDerection=direction
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

            override fun onFinish() {
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
        binding.fishingControler.setListener(object : RockerView.OnShakeListener{
            override fun onFinish() {
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

            override fun direction(direction: RockerView.Direction?, distance: Int) {
                fishingDerection=direction
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

            override fun onStart() {

            }
        })
    }

    fun showCameraControler(view: View){
        //显示摄像头设置
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
        for (i in 0..2){
            val popGameItemBean=PopGameItemBean()
            popGameItemBean.enableTab=true
            popGameItemBean.imgRes=R.mipmap.game_icon_setting
            when(i){
                0 ->{

                }
                1 ->{

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

    }

    override fun gameSetClick(pos: Int) {
        when(pos){

        }
    }



    override fun gameStartBtnBg(): Int {
        return R.drawable.startgame_btn_bg
    }

    override fun gameCancelBtnBg(): Int {
        return R.drawable.cancel_yuyue_btbg
    }

    override fun gameQueueBtnBg(): Int {
        return R.drawable.gamequeue_btn_bg
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


}