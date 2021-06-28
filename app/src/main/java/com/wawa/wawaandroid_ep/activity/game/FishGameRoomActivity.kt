package com.wawa.wawaandroid_ep.activity.game

import android.os.Bundle
import android.view.Gravity
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
import com.wawa.wawaandroid_ep.bean.game.GameRoomChatDataBean
import com.wawa.wawaandroid_ep.bean.game.GameRoomChatItemBean
import com.wawa.wawaandroid_ep.bean.game.GameRoomUsers
import com.wawa.wawaandroid_ep.view.ViewUtils
import com.wawa.wawaandroid_ep.view.popgame.PopGameItemBean
import org.json.JSONObject
import java.lang.reflect.Type

/**
 *作者：create by 张金 on 2021/6/24 10:23
 *邮箱：564813746@qq.com
 */
class FishGameRoomActivity : GameBaseActivity<FishgameRoomActivityLayBinding, FishGameViewModel>() ,
    GameReadyDialog.GameReadyInterface , GameSetGroupViewControlor.GameViewClickCallback {
    val TAG="FishGameRoomActivity"
    val chatAdapter= ArrayListAdapter<GameRoomChatItemBean>()
    private  var popupGameSetWindow: PopupWindow?=null
    var gameSetGroupViewControlor: GameSetGroupViewControlor?=null

    override fun startGame() {
        var data = JSONObject()
        data.put("id", GameSocketManager.generateId().toString())
        data.put("method", "start_game")
        GameSocketManager.getInstance()
            .sendMessage("game", data, object : GameSocketManager.Callback {
                override fun onSuccess(jsonStr: JSONObject?) {
                    LogUtils.d(TAG, "startgame--success")

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

    override fun onIMNotify(jsondata: JSONObject?) {
        super.onIMNotify(jsondata)
        LogUtils.d(TAG,"onIMNotify--")
        var gson=Gson()
        var gameRoomChatDataBean: GameRoomChatDataBean?= null
        gameRoomChatDataBean=gson.fromJson(jsondata?.toString(), GameRoomChatDataBean::class.java)
        runOnUiThread{
            if (gameRoomChatDataBean != null){

//                chatListAdapter?.insertItem(gameRoomChatDataBean.msg_list)
            }
        }
    }

    override fun onRoomUserAmountChanged(jsondata: JSONObject?) {
        super.onRoomUserAmountChanged(jsondata)
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

    }

    override fun cancelGame() {

    }

    override fun gameSetClick(pos: Int) {
        when(pos){

        }
    }


}