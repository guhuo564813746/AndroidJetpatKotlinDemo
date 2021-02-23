package com.wawa.wawaandroid_ep.activity.game

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import com.apollographql.apollo.RoomInfoQuery
import com.wawa.baselib.utils.logutils.LogUtils
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
class RobotGameActivity : GameBaseActivity<RobotGameActivityLayBinding>(){
    private val TAG="RobotGameActivity"
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

    fun initGameMenuView(){
        var drawItemBeans=ArrayList<DrawableMenuLayout.DrawItemBean>()
        for (i in 1..3){
            var drawItemBean=DrawableMenuLayout.DrawItemBean()
            when(i){
                1 -> drawItemBean.itemImgSrc=R.mipmap.game_icon_setting
                2 -> drawItemBean.itemImgSrc=R.mipmap.game_icon_desc
                3 -> drawItemBean.itemImgSrc=R.mipmap.game_icon_quitroom
            }
            drawItemBeans.add(drawItemBean)
        }
        binding.menuPanel.drawItemBeans=drawItemBeans
        binding.menuPanel.setOnItemClickListener {
            when(it){
                0 -> openSet()
                1 -> openGameDesc()
                2 -> quitGameRoom()
            }
        }
    }

    fun openSet(){

    }

    fun openGameDesc(){

    }

    fun quitGameRoom(){

    }

    fun initGameVideo(data: RoomInfoQuery.RoomList){
        if (gameVideoControlor== null){
            gameVideoControlor=DaniuGameVideoControlor()
            var bundle=Bundle()
            bundle.putString(DaniuGameVideoControlor.MASTER_VIDEO_URL,data.liveStream()?.get(0)?.fragments()?.liveStreamforGameFragment()?.liveRtmpUrl())
            bundle.putString(DaniuGameVideoControlor.SLAVE_VIDEO_URL,"")
            (gameVideoControlor as DaniuGameVideoControlor)?.arguments=bundle
//        mLiveGameController.setLiveStreamUrl(mLiveBean.liveStreamV2List.get(streamDefaultQuility).liveRtmpUrl, null);
            var ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            ft.replace(R.id.stream_replaced, gameVideoControlor as DaniuGameVideoControlor).commitAllowingStateLoss()
        }
    }

}