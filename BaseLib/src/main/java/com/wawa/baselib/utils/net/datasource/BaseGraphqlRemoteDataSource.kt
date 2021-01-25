package com.wawa.baselib.utils.net.datasource

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Query
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.api.cache.http.HttpCachePolicy
import com.apollographql.apollo.exception.ApolloException
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

/**
 *作者：create by 张金 on 2021/1/20 14:12
 *邮箱：564813746@qq.com
 */
open class GraphqlRemoteDataSource() {

    companion object{

        val baseUrl: String ="http://robot.t.seafarer.me/api/v1/"
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY))
            .authenticator { _, response -> response.request()
                .newBuilder()
                .addHeader("Authorization", "Bearer <YOUR ACCESS TOKEN>")
                .build()
            }
            .build()

        open val apolloClient= ApolloClient.builder()
            .okHttpClient(okHttpClient)
            .serverUrl(baseUrl)
            .build()
    }
    //发起请求


}