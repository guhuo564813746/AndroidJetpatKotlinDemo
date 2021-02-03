package com.wawa.wawaandroid_ep.activity.viewmodule

import android.view.View
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModel

/**
 *作者：create by 张金 on 2021/2/3 14:41
 *邮箱：564813746@qq.com
 */
open class BaseGameViewModel : ViewModel(){
    var userDataGroupVisibility = ObservableInt(View.VISIBLE)
    var fee = ObservableField("")
    var coin=ObservableField("")
    var roomUserAmountText = ObservableField<String>("")
    var guestPanelVisibility= ObservableInt(View.VISIBLE)
    var queueCount = ObservableField<String>("")
    var gamePanelVisibility =ObservableInt(View.GONE)
    var countdownText = ObservableField<String>("")
    var countdownVisibility= ObservableInt(View.GONE)
}