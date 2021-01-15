package com.wawa.baselib.utils.net.exception

import com.wawa.baselib.utils.net.bean.IHttpWrapBean

/**
 *作者：create by 张金 on 2021/1/15 14:16
 *邮箱：564813746@qq.com
 */
open class BaseHttpException(val err_code: Int,val err_msg: String,val realException: Throwable?) : Exception(err_msg){
    companion object{
        const val CODE_ERROR_LOCAL_UNKNOWN = -1024520
    }

    /*
    * 是否是服务器返回的code ！= successCode的异常*/

    val isServiceBadException: Boolean
        get() = this is ServiceBadCodeException

    val isLocalBadException: Boolean
    get() = this is LocalBadException

}

/*
* API 请求成功了，但是不是successCode
* */
class ServiceBadCodeException(errCode: Int,errMsg: String) : BaseHttpException(errCode,errMsg,null){
    constructor(bean: IHttpWrapBean<*>) : this(bean.httpCode,bean.httpMsg)
}

/*
* 请求过程中出现的异常
* */
class LocalBadException(throwable: Throwable) : BaseHttpException(CODE_ERROR_LOCAL_UNKNOWN,throwable.message ?: "",throwable){

}