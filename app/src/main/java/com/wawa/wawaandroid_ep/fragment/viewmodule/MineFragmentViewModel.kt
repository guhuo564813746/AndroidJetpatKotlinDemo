package com.wawa.wawaandroid_ep.fragment.viewmodule

import androidx.databinding.ObservableField
import com.wawa.baselib.utils.viewmodel.BaseVM

/**
 *作者：create by 张金 on 2021/1/29 11:44
 *邮箱：564813746@qq.com
 */
class MineFragmentViewModel : BaseVM(){
    var scores = ObservableField("")
    var diamons = ObservableField("")
    var coins = ObservableField("")
    var userId = ObservableField("")
    var userLevel = ObservableField("")
    var userName = ObservableField("")
}