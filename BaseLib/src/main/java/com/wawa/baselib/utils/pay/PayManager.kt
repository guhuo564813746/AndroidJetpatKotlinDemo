package com.wawa.baselib.utils.pay

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.ChargeItemListQuery
import com.apollographql.apollo.CreateOrderItemMutation
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.blankj.utilcode.util.ToastUtils
import com.wawa.baselib.utils.dialog.PayTypeDialog
import com.wawa.baselib.utils.logutils.LogUtils
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlin.properties.Delegates

/**
 *作者：create by 张金 on 2021/3/4 17:23
 *邮箱：564813746@qq.com
 */
class PayManager(private val context: Context,
                 private val apolloClient: ApolloClient
                 ) : PayTypeDialog.PayTypeCallback, LifecycleObserver {
    companion object{
        val PAYTYPE_ZFB_H5=5
    }
    val TAG="PayManager"
    lateinit var mContext: Context
    lateinit var callback: PayCallback
    lateinit var payTypeDialog: PayTypeDialog
    private var isGoH5Pay=false
    private var payType: Int= PAYTYPE_ZFB_H5
    private val compositeDisposable = CompositeDisposable()
    private lateinit var orderSuccessDp: Disposable
    private lateinit var errDp: Disposable
    private var chargeItemId by Delegates.notNull<Int>()
    fun showPayTypeDialog(payGoods: ChargeItemListQuery.Goods,
                          chargeId: Int,
                          cb: PayCallback){
        this.mContext=context
        this.callback=cb
        this.chargeItemId=chargeId
        payTypeDialog= PayTypeDialog(this,payGoods)
        if (!payTypeDialog.isAdded){
            payTypeDialog.show((context as FragmentActivity).supportFragmentManager,"showPayTypeDialog")
        }
    }

    interface PayCallback{
        fun paySuccess(payType: Int)
        fun payErr()
    }

    override fun payTypeConfirm(payType: Int) {
        this.payType=payType
        creatOrder(payType)
    }

    fun creatOrder(payType: Int){
        val mutation=CreateOrderItemMutation(chargeItemId,payType,true)
        apolloClient.mutate(mutation)
            .enqueue(object: ApolloCall.Callback<CreateOrderItemMutation.Data>(){
                override fun onFailure(e: ApolloException) {
                    LogUtils.d(TAG,"creatOrder_onFailure_${e.message}")
                    ToastUtils.showShort(e.message)
                }

                override fun onResponse(response: Response<CreateOrderItemMutation.Data>) {
                    LogUtils.d(TAG,"creatOrder_onResponse_${response.toString()}")
                    when(payType){
                        5->{
                            //H5支付
                            var payRedirectUrl: String?=response.data?.createChargeOrder()?.payParams()?.fragments()?.payParamsFragment()?.payRedirectUrl()
                            payRedirectUrl.let {
                                go2H5_ZFB_pay(it)
                            }
                        }
                    }
                }
            })
    }

    fun go2H5_ZFB_pay(url: String?){
        isGoH5Pay=true
        val uri = Uri.parse(url)
        val intent =
            Intent(Intent.ACTION_VIEW, uri)
        mContext.startActivity(intent)
    }

    fun destroy(){
        compositeDisposable.dispose()
        compositeDisposable.clear()
    }

    override fun payTypeCancel() {
        ToastUtils.showShort("已取消支付")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart(owner: LifecycleOwner){
        LogUtils.d(TAG,"onStart--")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume(owner: LifecycleOwner){
        LogUtils.d(TAG,"onResume--")
        if (isGoH5Pay){
            if (callback != null){
                callback.paySuccess(payType)
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(owner: LifecycleOwner){
        LogUtils.d(TAG,"onDestroy--")
        destroy()
    }

}