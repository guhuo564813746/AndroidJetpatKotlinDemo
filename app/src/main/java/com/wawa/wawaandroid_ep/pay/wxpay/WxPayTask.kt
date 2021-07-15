package com.wawa.wawaandroid_ep.pay.wxpay

import android.content.Context
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ToastUtils
import com.robotwar.app.BuildConfig
import com.robotwar.app.R
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.wawa.wawaandroid_ep.pay.PayManager
import org.json.JSONObject


/**
 *作者：create by 张金 on 2021/7/13 16:31
 *邮箱：564813746@qq.com
 */
class WxPayTask(private val mContext: Context,private val callback: PayManager.PayCallback) {
    private var api: IWXAPI? = null
    private val TAG="WxPayTask"
    companion object{
        var onWxPayRes: MutableLiveData<BaseResp>?=null
    }
    init {
        api= WXAPIFactory.createWXAPI(mContext, BuildConfig.WX_APPID)
        (mContext as FragmentActivity).runOnUiThread {
            onWxPayRes?.observe(mContext, Observer {
                when (it.errCode) {
                    BaseResp.ErrCode.ERR_OK -> {
                        //支付成功
                        if (callback != null){
                            callback.paySuccess(PayManager.PAYTYPE_WX)
                        }
                    }
                    BaseResp.ErrCode.ERR_USER_CANCEL -> {
                        //支付取消
                        ToastUtils.showShort("支付取消")
                    }
                    else -> {
                        //支付失败
                        if (callback != null){
                            callback.payErr(mContext.getString(R.string.post_fee_pay_failed))
                        }
                    }
                }
                onWxPayRes=null
            })
        }
    }

    fun wxPay(json: String){
        Log.d(TAG,"wxPay--"+json)
        val wxPayJson= JSONObject(json)
        val req= PayReq()
        req.appId= BuildConfig.WX_APPID
        req.partnerId=wxPayJson.getString("partnerId")
        req.prepayId=wxPayJson.getString("prepayId")
        req.nonceStr=wxPayJson.getString("nonceStr")
        req.timeStamp=wxPayJson.getString("timeStamp")
        req.packageValue=wxPayJson.getString("package")
        req.sign=wxPayJson.getString("sign")
//        req.extData=""
        api?.sendReq(req)
    }

}