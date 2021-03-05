package com.wawa.baselib.utils.pay

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.apollographql.apollo.*
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.manager.LifecycleListener
import com.wawa.baselib.utils.apollonet.BaseDataSource
import com.wawa.baselib.utils.dialog.PayTypeDialog
import com.wawa.baselib.utils.logutils.LogUtils
import com.wawa.baseresourcelib.MainActivity
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
    val TAG="PayManager"
    lateinit var mContext: Context
    lateinit var callback: PayCallback
    lateinit var payTypeDialog: PayTypeDialog
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
        fun paySuccess()
        fun payErr()
    }

    override fun payTypeConfirm(payType: Int) {
        creatOrder(payType)
    }

    fun creatOrder(payType: Int){
        val mutation=CreateOrderItemMutation(chargeItemId,payType)
        apolloClient.mutate(mutation)
            .enqueue(object: ApolloCall.Callback<CreateOrderItemMutation.Data>(){
                override fun onFailure(e: ApolloException) {
                    LogUtils.d(TAG,"creatOrder_onFailure_${e.message}")
                }

                override fun onResponse(response: Response<CreateOrderItemMutation.Data>) {
                    LogUtils.d(TAG,"creatOrder_onResponse_${response.toString()}")
                }
            })
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

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(owner: LifecycleOwner){
        LogUtils.d(TAG,"onDestroy--")
        destroy()
    }

}