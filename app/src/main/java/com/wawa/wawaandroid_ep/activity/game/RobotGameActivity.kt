package com.wawa.wawaandroid_ep.activity.game

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import com.apollographql.apollo.RoomInfoQuery
import com.wawa.wawaandroid_ep.R
import com.wawa.wawaandroid_ep.activity.viewmodule.RobotGameViewModel
import com.wawa.wawaandroid_ep.databinding.RobotGameActivityLayBinding
import com.wawa.wawaandroid_ep.gamevideopager.DaniuGameVideoControlor

/**
 *作者：create by 张金 on 2021/2/3 14:27
 *邮箱：564813746@qq.com
 */
class RobotGameActivity : GameBaseActivity<RobotGameActivityLayBinding>(){
    val robotGameActivityViewModel: RobotGameViewModel by viewModels()


    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.robot_game_activity_lay
    }

    override fun initView() {
//        binding.streamReplaced
        baseGameViewModel.roomInfoData?.observe(this, Observer {
            initGameVideo(it)
        })
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