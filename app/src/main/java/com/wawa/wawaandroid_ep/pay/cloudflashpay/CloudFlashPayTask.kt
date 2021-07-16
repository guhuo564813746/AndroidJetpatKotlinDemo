package com.wawa.wawaandroid_ep.pay.cloudflashpay

import android.content.Context
import android.util.Log
import com.wawa.wawaandroid_ep.pay.PayManager

/**
 *作者：create by 张金 on 2021/7/13 17:39
 *邮箱：564813746@qq.com
 */
class CloudFlashPayTask(private val mContext: Context, private val callback: PayManager.PayCallback) {
    val TAG="CloudFlashPayTask"
    init {

    }
    fun cloudFlashPay(json: String){
        Log.d(TAG,"cloudFlashPay--"+json)
    }
}