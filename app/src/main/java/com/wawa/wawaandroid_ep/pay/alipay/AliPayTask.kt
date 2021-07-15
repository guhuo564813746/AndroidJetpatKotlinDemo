package com.wawa.wawaandroid_ep.pay.alipay

import android.app.Activity
import android.content.Context
import com.alipay.sdk.app.PayTask
import com.wawa.baselib.BuildConfig
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

/**
 *作者：create by 张金 on 2021/3/15 11:10
 *邮箱：564813746@qq.com
 */
class AliPayTask(private val context: Context) {

    /**
     * 调用支付宝sdk
     */
    fun invokeAliPay(mPayInfo: String,callback: com.wawa.wawaandroid_ep.pay.PayManager.PayCallback) {
        if (BuildConfig.DEBUG){
//            EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX)
        }
        Observable.create<Map<String, String>>( ObservableOnSubscribe<Map<String, String>> {  //沙盒环境
            //                EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);

            val alipay = PayTask(context as Activity?)
            //执行支付，这是一个耗时操作，最后返回支付的结果，用handler发送到主线程
            val result: Map<String, String> =
                alipay.payV2(mPayInfo, false)
            it.onNext(result)
            it.onComplete()
        }).subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(Consumer<Map<String, String>> { result ->
                if ("9000" == result["resultStatus"]) {
                    callback.paySuccess(com.wawa.wawaandroid_ep.pay.PayManager.PAYTYPE_ALIPAY)
                } else {
                    result["memo"]?.let { callback.payErr(it) }
                }
//                mActivity = null
            })
    }

    fun invokeAliPayV1(mPayInfo: String,callback: com.wawa.wawaandroid_ep.pay.PayManager.PayCallback) {
        if (BuildConfig.DEBUG){
//            EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX)
        }
        Observable.create<String>( ObservableOnSubscribe<String> {  //沙盒环境
            //                EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);

            val alipay = PayTask(context as Activity?)
            //执行支付，这是一个耗时操作，最后返回支付的结果，用handler发送到主线程
            val result: String =
                alipay.pay(mPayInfo, false)
            it.onNext(result)
            it.onComplete()
        }).subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(Consumer<String> { result ->
                if ("9000".equals(result)) {
                    callback.paySuccess(com.wawa.wawaandroid_ep.pay.PayManager.PAYTYPE_ALIPAY)
                } else {
                    callback.payErr("pay failed")
                }
//                mActivity = null
            })
    }

}