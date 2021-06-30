package com.wawa.wawaandroid_ep.activity.viewmodule

import android.view.View
import androidx.databinding.ObservableInt
import com.robotwar.app.R

/**
 *作者：create by 张金 on 2021/6/24 10:25
 *邮箱：564813746@qq.com
 */
class FishGameViewModel : BaseGameViewModel(){
    var countdownImgVisibility=ObservableInt(View.GONE)
    init {
        startGameBtnRes.set(R.drawable.startgame_btn_bg)
    }
}