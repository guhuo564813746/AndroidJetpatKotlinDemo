package com.wawa.baselib.utils.pay

import android.content.Context

/**
 *作者：create by 张金 on 2021/3/4 17:23
 *邮箱：564813746@qq.com
 */
class PayManager() {

    lateinit var mContext: Context
    lateinit var callback: PayCallback

    fun showPayTypeDialog(context: Context,cb: PayCallback){
        this.mContext=context
        this.callback=cb

    }

    interface PayCallback{
        fun paySuccess()
        fun payErr()
    }
}