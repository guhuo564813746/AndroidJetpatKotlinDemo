package com.wawa.baselib.utils.pay.wxpay

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.wawa.baselib.utils.pay.PayManager


/**
 *作者：create by 张金 on 2021/7/13 16:31
 *邮箱：564813746@qq.com
 */
class WxPayTask(private val mContext: Context,private val callback: PayManager.PayCallback) {
    private var api: IWXAPI? = null
    companion object{
        var onWxPayRes: MutableLiveData<BaseResp>?=null
    }
    init {
        api= WXAPIFactory.createWXAPI(mContext, "wxb4ba3c02aa476ea1")
        onWxPayRes?.observe(mContext as LifecycleOwner, Observer {
            when (it.errCode) {
                BaseResp.ErrCode.ERR_OK -> {
                    //支付成功
                }
                BaseResp.ErrCode.ERR_USER_CANCEL -> {
                    //支付取消
                }
                else -> {
                    //支付失败
                }
            }
            onWxPayRes=null
        })
    }

    fun wxPay(json: String){
        val req= PayReq()
        req.appId=""
        req.partnerId=""
        req.prepayId=""
        req.nonceStr=""
        req.timeStamp=""
        req.packageValue=""
        req.sign=""
        req.extData=""
        api?.sendReq(req);
    }

}