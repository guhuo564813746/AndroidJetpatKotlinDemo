package com.wawa.wawaandroid_ep

import android.app.Application
import com.wawa.baselib.utils.Utils

/**
 *作者：create by 张金 on 2021/1/14 16:34
 *邮箱：564813746@qq.com
 */
class WawaApp : Application(){
    override fun onCreate() {
        super.onCreate()
        Utils.initSp(this)
    }
}