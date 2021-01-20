package com.wawa.wawaandroid_ep.httpcore.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 *作者：create by 张金 on 2021/1/20 11:13
 *邮箱：564813746@qq.com
 */
class FilterInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val httpBuilder = originalRequest.url().newBuilder()
//        httpBuilder.addEncodedQueryParameter(HttpConfig.KEY, HttpConfig.KEY_MAP)
        val requestBuilder = originalRequest.newBuilder()
                .url(httpBuilder.build())
        return chain.proceed(requestBuilder.build())
    }

}