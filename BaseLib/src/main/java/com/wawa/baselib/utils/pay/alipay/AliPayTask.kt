package com.wawa.baselib.utils.pay.alipay

import android.app.Activity
import android.content.Context
import com.alipay.sdk.app.PayTask
import com.wawa.baselib.utils.pay.PayManager
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.internal.util.HalfSerializer.onComplete
import io.reactivex.internal.util.HalfSerializer.onNext
import io.reactivex.schedulers.Schedulers

/**
 *作者：create by 张金 on 2021/3/15 11:10
 *邮箱：564813746@qq.com
 */
class AliPayTask(private val context: Context) {

    /**
     * 调用支付宝sdk
     */
    fun invokeAliPay(mPayInfo: String,callback: PayManager.PayCallback) {
        Observable.create<Map<String, String>>( ObservableOnSubscribe<Map<String, String>> {  //沙盒环境
            //                EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);

            val alipay = PayTask(context as Activity?)
            //执行支付，这是一个耗时操作，最后返回支付的结果，用handler发送到主线程
            val result: Map<String, String> =
                alipay.payV2(mPayInfo, true)
            it.onNext(result)
            it.onComplete()
        }).subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(Consumer<Map<String, String>> { result ->
                if ("9000" == result["resultStatus"]) {
                    callback.paySuccess(PayManager.PAYTYPE_ALIPAY)
                } else {
                    callback.payErr()
                }
//                mActivity = null
            })
    }

}