package com.wawa.baselib.utils.net.datasource

import com.wawa.baselib.utils.net.bean.IHttpWrapBean
import com.wawa.baselib.utils.net.callback.RequestCallback
import com.wawa.baselib.utils.net.callback.RequestPairCallback
import com.wawa.baselib.utils.net.exception.ServiceBadCodeException
import com.wawa.baselib.utils.net.viewmodule.IUIActionEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

/**
 *作者：create by 张金 on 2021/1/18 16:43
 *邮箱：564813746@qq.com
 * 提供了 两个/三个 接口同时并发请求的方法
 * 当所有接口都请求成功时，会通过 onSuccess 方法传出请求结果
 * 当包含的某个接口请求失败时，则会直接回调 onFail 方法
 */
abstract class RemoteExtendDataSource<Api :Any>(iUIActionEvent: IUIActionEvent?,apiServiceClass: Class<Api>) : RemoteDataSource<Api>(iUIActionEvent,apiServiceClass){
    fun <Data1,Data2> enquequeLoading(apiFun1:suspend Api.()-> IHttpWrapBean<Data1>,
                                        apiFun2: suspend Api.() -> IHttpWrapBean<Data2>,
                                    callbackFun: (RequestPairCallback<Data1,Data2>.() -> Unit)?= null) : Job{
        return enqueue(apiFun1=apiFun1,apiFun2=apiFun2,showloading = true,callbackFun = callbackFun)
    }

    fun <Data1,Data2> enqueue(apiFun1: suspend Api.() -> IHttpWrapBean<Data1>,
                                apiFun2: suspend Api.() -> IHttpWrapBean<Data2>,
                                showloading: Boolean =false,
                                callbackFun: (RequestPairCallback<Data1,Data2>.()-> Unit)? = null) :Job{
        return launchMain {
            val callback = if (callbackFun == null) null else RequestPairCallback<Data1, Data2>().apply {
                callbackFun.invoke(this)
            }
            try {
                if (showloading) {
                    showLoading(coroutineContext[Job])
                }
                callback?.onStart?.invoke()
                val responseList: List<IHttpWrapBean<out Any?>>
                try {
                    responseList = listOf(
                            async { apiFun1.invoke(getApiService()) },
                            async { apiFun2.invoke(getApiService()) }
                    ).awaitAll()
                    val failed = responseList.find { it.httpIsFailed }
                    if (failed != null) {
                        throw ServiceBadCodeException(failed)
                    }
                } catch (throwable: Throwable) {
                    handleException(throwable, callback)
                    return@launchMain
                }
                onGetResponse(callback, responseList)
            } finally {
                try {
                    callback?.onFinaly?.invoke()
                } finally {
                    if (showloading) {
                        dismissLoading()
                    }
                }
            }
        }
    }

    private suspend fun <DataA, DataB> onGetResponse(callback: RequestPairCallback<DataA, DataB>?,
                                                     responseList: List<IHttpWrapBean<out Any?>>) {
        callback?.let {
            withNonCancelable {
                callback.onSuccess?.let {
                    withMain {
                        it.invoke(responseList[0].httpData as DataA, responseList[1].httpData as DataB)
                    }
                }
                callback.onSuccessIO?.let {
                    withIO {
                        it.invoke(responseList[0].httpData as DataA, responseList[1].httpData as DataB)
                    }
                }
            }
        }
    }
}