package com.wawa.wawaandroid_ep.activity.game

import android.os.Bundle
import androidx.activity.viewModels
import com.wawa.wawaandroid_ep.R
import com.wawa.wawaandroid_ep.activity.viewmodule.RobotGameViewModel
import com.wawa.wawaandroid_ep.databinding.RobotGameActivityLayBinding

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

    }

}