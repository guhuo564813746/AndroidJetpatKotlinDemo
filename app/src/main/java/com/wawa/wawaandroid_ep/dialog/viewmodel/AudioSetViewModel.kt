package com.wawa.wawaandroid_ep.dialog.viewmodel

import androidx.databinding.ObservableBoolean
import com.wawa.baselib.utils.SharePreferenceUtils
import com.wawa.baselib.utils.viewmodel.BaseVM

/**
 *作者：create by 张金 on 2021/7/15 14:39
 *邮箱：564813746@qq.com
 */
class AudioSetViewModel : BaseVM(){
    var isBgVoiceSet = ObservableBoolean(false)
    var isVideoVoiceSet = ObservableBoolean(false)
    var isGameVoiceSet = ObservableBoolean(false)
    var isBtVoiceSet = ObservableBoolean(false)
    var isAppRunBgVoiceSet = ObservableBoolean(true)
    var isTanMuOpen = ObservableBoolean(true)
    init {
        isBgVoiceSet.set(SharePreferenceUtils.getSwitch(SharePreferenceUtils.BGM,true))
        isVideoVoiceSet.set(SharePreferenceUtils.getSwitch(SharePreferenceUtils.BGM_LIVE,true))
        isGameVoiceSet.set(SharePreferenceUtils.getSwitch(SharePreferenceUtils.BGM_GAME,true))
        isBtVoiceSet.set(SharePreferenceUtils.getSwitch(SharePreferenceUtils.BGM_KEY,true))
    }

}