package com.wawa.wawaandroid_ep.dialog.viewmodel

import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.wawa.baselib.utils.viewmodel.BaseVM

/**
 *作者：create by 张金 on 2021/7/15 15:18
 *邮箱：564813746@qq.com
 */
class VideoSetViewModel : BaseVM(){
    var curBuffer = ObservableInt(0)
    var bufferStr = ObservableField("")
    init {

    }
}