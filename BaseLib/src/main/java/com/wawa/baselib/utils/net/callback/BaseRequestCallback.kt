package com.wawa.baselib.utils.net.callback

import com.wawa.baselib.utils.net.exception.BaseHttpException

/**
 *作者：create by 张金 on 2021/1/15 15:24
 *邮箱：564813746@qq.com
 */
open class BaseRequestCallback(internal var onStart: (()->Unit)?=null,
                               internal var onCancel: (()->Unit)?=null,
                               internal var onFailed: ((BaseHttpException)->Unit)?=null,
                               internal var onFailToast: (()->Boolean)?=null,
                                internal var onFinaly: (()->Unit)?=null) {
    /**
     * 在显示 Loading 之后且开始网络请求之前执行
     */
    fun onStart(block: ()->Unit){
        this.onStart=block
    }

    /**
     * 如果外部主动取消了网络请求，不会回调 onFail，而是回调此方法，随后回调 onFinally
     * 但如果当取消网络请求时已回调了 onSuccess / onSuccessIO 方法，则不会回调此方法
     */
    fun onCancel(block: () -> Unit){
        this.onCancel=block
    }

    /**
     * 当网络请求失败时会调用此方法，在 onFinally 被调用之前执行
     */
    fun onFailed(block: (BaseHttpException) -> Unit){
        this.onFailed=block
    }

    /**
     * 用于控制是否当网络请求失败时 Toast 失败原因
     * 默认为 true，即进行 Toast 提示
     */
    fun onFailToast(block: () -> Boolean){
        this.onFailToast=block
    }

    /**
     * 在网络请求结束之后（不管请求成功与否）且隐藏 Loading 之前执行
     */
    fun onFinaly(block: () -> Unit){
        this.onFinaly=block
    }

}