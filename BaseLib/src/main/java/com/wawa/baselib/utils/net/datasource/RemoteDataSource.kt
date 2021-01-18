package com.wawa.baselib.utils.net.datasource

import com.wawa.baselib.utils.net.bean.IHttpWrapBean
import com.wawa.baselib.utils.net.callback.RequestCallback
import com.wawa.baselib.utils.net.exception.BaseHttpException
import com.wawa.baselib.utils.net.exception.ServiceBadCodeException
import com.wawa.baselib.utils.net.viewmodule.IUIActionEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking

/**
 *作者：create by 张金 on 2021/1/18 15:15
 *邮箱：564813746@qq.com
 */
abstract class RemoteDataSource<Api: Any>(iUIActionEvent: IUIActionEvent?,apiServiceClass: Class<Api>) : BaseRemoteDataSource<Api>(iUIActionEvent,apiServiceClass){

    fun <Data> enqueueLoading(apiFun: suspend Api.() -> IHttpWrapBean<Data>,
                              baseUrl: String = "",
                              callbackFun: (RequestCallback<Data>.() -> Unit)? = null): Job {
        return enqueue(apiFun = apiFun, showLoading = true, baseUrl = baseUrl, callbackFun = callbackFun)
    }

    fun <Data> enqueue(apiFun: suspend Api.() -> IHttpWrapBean<Data>,
                       showLoading: Boolean = false,
                       baseUrl: String = "",
                       callbackFun: (RequestCallback<Data>.() -> Unit)? = null): Job {
        return launchMain {
            val callback = if (callbackFun == null) null else RequestCallback<Data>().apply {
                callbackFun.invoke(this)
            }
            try {
                if (showLoading) {
                    showLoading(coroutineContext[Job])
                }
                callback?.onStart?.invoke()
                val response: IHttpWrapBean<Data>
                try {
                    response = apiFun.invoke(getApiService(baseUrl))
                    if (!response.httpIsSuccess) {
                        throw ServiceBadCodeException(response)
                    }
                } catch (throwable: Throwable) {
                    handleException(throwable, callback)
                    return@launchMain
                }
                onGetResponse(callback, response.httpData)
            } finally {
                try {
                    callback?.onFinaly?.invoke()
                } finally {
                    if (showLoading) {
                        dismissLoading()
                    }
                }
            }
        }
    }

    fun <Data> enqueueOriginLoading(apiFun: suspend Api.() -> Data,
                                    baseUrl: String = "",
                                    callbackFun: (RequestCallback<Data>.() -> Unit)? = null): Job {
        return enqueueOrigin(apiFun = apiFun, showloading = true, baseUrl = baseUrl, callbackFun = callbackFun)
    }

    fun <Data> enqueueOrigin(apiFun: suspend Api.()-> Data,
                            showloading: Boolean=false,
                            baseUrl: String="",
                            callbackFun: (RequestCallback<Data>.()->Unit)?=null):Job{
        return launchMain {
            val callback =if (callbackFun == null) null else RequestCallback<Data>().apply {
                callbackFun.invoke(this)
            }
            try {
                if (showloading){
                    showLoading(coroutineContext[Job])
                }
                callback?.onStart?.invoke()
                val response: Data
                try {
                    response=apiFun.invoke(getApiService(baseUrl))
                }catch (throwable: Throwable){
                    handleException(throwable,callback)
                    return@launchMain
                }
                onGetResponse(callback,response)

            }finally {
                try {
                    callback?.onFinaly?.invoke()
                }finally {
                    if (showloading){
                        dismissLoading()
                    }
                }
            }
        }
    }

    private suspend fun <Data> onGetResponse(callback: RequestCallback<Data>?,httpData: Data){
        callback?.let {
            withNonCancelable {
                callback.onSuccess?.let {
                    withMain {
                        it.invoke(httpData)
                    }
                }

                callback.onSuccessIO?.let {
                    withIO {
                        it.invoke(httpData)
                    }
                }
            }
        }
    }

    /**
     * 同步请求，可能会抛出异常，外部需做好捕获异常的准备
     * @param apiFun
     */
    @Throws(BaseHttpException::class)
    fun <Data> execute(apiFun: suspend Api.()-> IHttpWrapBean<Data>,baseUrl: String=""): Data{
        return runBlocking {
            try {
                val asyncIO=asynIO {
                    apiFun.invoke(getApiService(baseUrl))
                }
                val response=asyncIO.await()
                if (response.httpIsSuccess){
                    return@runBlocking response.httpData
                }
                throw ServiceBadCodeException(response)
            }catch (throwable: Throwable){
                throw generateExceptionReal(throwable)
            }
        }
    }
}