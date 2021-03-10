package com.wawa.wawaandroid_ep.datasource

import android.widget.Toast
import com.robotwar.app.BuildConfig
import com.wawa.baselib.utils.net.datasource.RemoteExtendDataSource
import com.wawa.baselib.utils.net.viewmodule.IUIActionEvent
import com.wawa.wawaandroid_ep.WawaApp
import com.wawa.wawaandroid_ep.httpcore.apiservice.ApiService
import com.wawa.wawaandroid_ep.httpcore.interceptor.FilterInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 *作者：create by 张金 on 2021/1/20 10:54
 *邮箱：564813746@qq.com
 */
class SelfRemoteDataSource(iuiActionEvent: IUIActionEvent?) : RemoteExtendDataSource<ApiService>(iuiActionEvent,ApiService::class.java) {

    companion object {

        private val httpClient: OkHttpClient by lazy {
            createHttpClient()
        }

        private fun createHttpClient(): OkHttpClient {
            val builder = OkHttpClient.Builder()
                    .readTimeout(1000L, TimeUnit.MILLISECONDS)
                    .writeTimeout(1000L, TimeUnit.MILLISECONDS)
                    .connectTimeout(1000L, TimeUnit.MILLISECONDS)
                    .retryOnConnectionFailure(true)
                    .addInterceptor(FilterInterceptor())
//                    .addInterceptor(MonitorInterceptor(WawaApp.lContext))
            return builder.build()
        }

    }

    /**
     * 允许子类自己来实现创建 Retrofit 的逻辑
     * 外部无需缓存 Retrofit 实例，ReactiveHttp 内部已做好缓存处理
     * 但外部需要自己判断是否需要对 OKHttpClient 进行缓存
     * @param baseUrl
     */
    override fun createRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
                .client(httpClient)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }


    override val baseUrl: String
        get() = BuildConfig.BaseURL

    override fun showToast(msg: String) {
        Toast.makeText(WawaApp.lContext, msg, Toast.LENGTH_SHORT).show()
    }
}