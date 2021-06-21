package com.wawa.wawaandroid_ep.fragmentv2.viewmodel

import androidx.databinding.ObservableField
import com.wawa.wawaandroid_ep.base.viewmodel.BaseVM

/**
 *作者：create by 张金 on 2021/6/21 10:59
 *邮箱：564813746@qq.com
 */
class MineFmV2ViewModel : BaseVM(){
    var scores = ObservableField("")
    var diamons = ObservableField("")
    var coins = ObservableField("")
    var userId = ObservableField("")
    var userLevel = ObservableField("")
    var userName = ObservableField("")
}