package com.wawa.wawaandroid_ep.dialog.viewmodel

import androidx.databinding.ObservableBoolean
import com.wawa.baselib.utils.SharePreferenceUtils
import com.wawa.baselib.utils.viewmodel.BaseVM

/**
 *作者：create by 张金 on 2021/7/15 15:35
 *邮箱：564813746@qq.com
 */
class GameOperationSetViewModel : BaseVM(){
    var isFishGetRemindOpen= ObservableBoolean(true)
    var isGameFishAutoTime = ObservableBoolean(false)
    init {
        isFishGetRemindOpen.set(SharePreferenceUtils.getSwitch(SharePreferenceUtils.FISHGAME_GETFISH_REMIND,true))
        isGameFishAutoTime.set(SharePreferenceUtils.getSwitch(SharePreferenceUtils.FISHGAME_AUTO_TIME,true))
    }
}