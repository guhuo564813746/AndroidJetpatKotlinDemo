package com.wawa.wawaandroid_ep.httpcore.bean

import com.google.gson.annotations.SerializedName
import com.wawa.baselib.utils.net.bean.IHttpWrapBean

class HttpWrapBean<T>(
        @SerializedName("status") var code: Int = 0,
        @SerializedName("info") var message: String? = null,
        @SerializedName("districts", alternate = ["forecasts"]) var data: T) : IHttpWrapBean<T> {

    companion object {

        fun <T> success(data: T): HttpWrapBean<T> {
            return HttpWrapBean(0, "success", data)
        }

        fun <T> failed(data: T): HttpWrapBean<T> {
            return HttpWrapBean(-200, "服务器停止维护了~~", data)
        }

    }

    override val httpCode: Int
        get() = code

    override val httpMsg: String
        get() = message ?: ""

    override val httpData: T
        get() = data

    override val httpIsSuccess: Boolean
        get() = code == 0 || message == "OK"

    override fun toString(): String {
        return "HttpResBean(code=$code, message=$message, data=$data)"
    }

}