package com.wawa.baselib.utils.net.datasource

import android.util.Log
import com.apollographql.apollo.ApolloClient
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor

/**
 *作者：create by 张金 on 2021/1/20 14:12
 *邮箱：564813746@qq.com
 */
open class GraphqlRemoteDataSource() {

    companion object{
        val baseUrl: String ="http://robot.t.seafarer.me/api/v1/graphql"
        var okHttpClientBuilder=OkHttpClient.Builder()
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor { chain: Interceptor.Chain ->
                    var request: Request=chain.request()
                    var newRequest: Request=request.newBuilder()
                        .header("X-ENV", "APP")
                        .header("X-APP-SYSTEM", "Android")
                        .header("X-APP-VERSION", "1")
                        .header("X-APP-ASHOP-ID", "0")
                        .header("X-APP-CHANNEL", "sansung")
                        .header("Accept-Language", "en") //LanguageUtils
                        .header("X-USER-UID", "3")
                        .header("X-USER-TOKEN", "4dd088a4bf4f2c407718d36aa33e6874ead4d9e0")
                        .method(request.method(),request.body())
                        .build()

                    chain.proceed(newRequest)
                }
           /* .authenticator { _, response -> response.request()
                .newBuilder()
//                    .addHeader("Authorization", "Bearer <YOUR ACCESS TOKEN>")
                .build()
            }*/
            .build()

    }

   /* open fun initTokenAndUid(token: String?,uid: String?){
        Log.d("initTokenAndUid",token+uid)
        okHttpClient=okHttpClientBuilder
            .addInterceptor(HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor {
            var request: Request=it.request()
            var newRequest: Request=request.newBuilder()
                .header("X-ENV", "APP")
                .header("X-APP-SYSTEM", "Android")
                .header("X-APP-VERSION", "1")
                .header("X-APP-ASHOP-ID", "0")
                .header("X-APP-CHANNEL", "sansung")
                .header("Accept-Language", "en")
                .header("X-USER-UID", uid)
                .header("X-USER-TOKEN", token)
                .method(request.method(),request.body())
                .build()
            it.proceed(newRequest)
        }
            .build()

    }*/
    //发起请求
    open fun apolloClient(): ApolloClient{
        return ApolloClient.builder()
            .okHttpClient(okHttpClient)
            .serverUrl(baseUrl)
            .build()
    }


}