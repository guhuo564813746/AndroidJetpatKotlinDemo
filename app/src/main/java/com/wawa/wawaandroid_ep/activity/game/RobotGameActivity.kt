package com.wawa.wawaandroid_ep.activity.game

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import com.apollographql.apollo.RoomInfoQuery
import com.wawa.baselib.utils.logutils.LogUtils
import com.wawa.baselib.utils.socketio.GameSocketManager
import com.wawa.baselib.utils.socketio.listener.EpGameListener
import com.wawa.wawaandroid_ep.R
import com.wawa.wawaandroid_ep.activity.viewmodule.RobotGameViewModel
import com.wawa.wawaandroid_ep.databinding.RobotGameActivityLayBinding
import com.wawa.wawaandroid_ep.gamevideopager.DaniuGameVideoControlor
import com.wawa.wawaandroid_ep.view.DrawableMenuLayout
import org.json.JSONObject

/**
 *作者：create by 张金 on 2021/2/3 14:27
 *邮箱：564813746@qq.com
 */
class RobotGameActivity : GameBaseActivity<RobotGameActivityLayBinding>(), EpGameListener {
    private val TAG = "RobotGameActivity"

    //Ep指令
    private var goTopleftEp: String? = "chassis speed x 0.3 y 0.3 z 1;"
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

    val robotGameActivityViewModel: RobotGameViewModel by viewModels()


    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.robot_game_activity_lay
    }

    override fun initView() {
        initGameMenuView()
//        binding.streamReplaced
        baseGameViewModel.roomInfoData?.observe(this, Observer {
            initGameVideo(it)
        })
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

    fun initGameVideo(data: RoomInfoQuery.RoomList) {
        if (gameVideoControlor == null) {
            gameVideoControlor = DaniuGameVideoControlor()
            var bundle = Bundle()
            bundle.putString(
                DaniuGameVideoControlor.MASTER_VIDEO_URL,
                data.liveStream()?.get(0)?.fragments()?.liveStreamforGameFragment()?.liveRtmpUrl()
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
    }

    override fun onEpGameOver(msg: JSONObject?) {
        LogUtils.d(TAG, "onEpGameOver")
    }

    override fun onGameReady(timeLeft: Int) {
        super.onGameReady(timeLeft)
        LogUtils.d(TAG, "onGameReady")
        if (timeLeft == 9) {
            startGame()
        }
    }

    override fun startGame() {
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