package com.wawa.wawaandroid_ep

import android.app.Application
import android.content.Context
import android.graphics.Typeface
import android.util.Log
import androidx.multidex.MultiDex
import com.apollographql.apollo.ApolloAndroidLogger
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.UserQuery
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.api.ResponseField
import com.apollographql.apollo.api.cache.http.HttpCachePolicy
import com.apollographql.apollo.cache.http.ApolloHttpCache
import com.apollographql.apollo.cache.http.DiskLruHttpCacheStore
import com.apollographql.apollo.cache.normalized.CacheKey
import com.apollographql.apollo.cache.normalized.CacheKeyResolver
import com.apollographql.apollo.cache.normalized.sql.SqlNormalizedCacheFactory
import com.robotwar.app.R
import com.scwang.smart.refresh.footer.BallPulseFooter
import com.scwang.smart.refresh.header.BezierRadarHeader
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.constant.SpinnerStyle
import com.wawa.baselib.utils.LanguageUtils
import com.wawa.baselib.utils.SharePreferenceUtils
import com.wawa.baselib.utils.apollonet.BaseDataSource
import com.wawa.baselib.utils.apollonet.service.ApolloCallbackService
import com.wawa.baselib.utils.apollonet.service.ApolloCoroutinesService
import com.wawa.baselib.utils.apollonet.service.ApolloRxService
import com.wawa.baselib.utils.apollonet.service.ApolloWatcherService
import com.wawa.baselib.utils.net.datasource.GraphqlRemoteDataSource
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File


/**
 *作者：create by 张金 on 2021/1/14 16:34
 *邮箱：564813746@qq.com
 */
class WawaApp : Application(){

    companion object{
        lateinit var  lContext: Context
//        var apolloClient: ApolloClient=GraphqlRemoteDataSource.apolloClient
        lateinit var apolloClient: ApolloClient
        var mMainTypeface: Typeface?=null
    }
    val dataSource: BaseDataSource by lazy {
        getDataSource(ServiceTypes.COROUTINES)
    }

    private val baseUrl = "https://app.robotwar.cc/api/v1/graphql"
    /*by lazy {
        val logInterceptor = HttpLoggingInterceptor(
            object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    Log.d("OkHttp", message)
                }
            }
        ).apply { level = HttpLoggingInterceptor.Level.BODY }

        val okHttpClient = OkHttpClient.Builder()
            .addNetworkInterceptor { chain ->
                val request = chain.request().newBuilder()
//                    .addHeader("Authorization", "bearer ${BuildConfig.GITHUB_OAUTH_TOKEN}")
                    .header("X-ENV", "APP")
                    .header("X-APP-SYSTEM", "Android")
//                    .header("X-APP-VERSION", "1")
                    .header("X-APP-ASHOP-ID", "0")
//                    .header("X-APP-CHANNEL", "sansung")
                    .header("Accept-Language", "en") //LanguageUtils
//                    .header("X-USER-ID", "3")
//                    .header("X-ACCESS-TOKEN", "4dd088a4bf4f2c407718d36aa33e6874ead4d9e0")
                    .build()

                chain.proceed(request)
            }
            .addInterceptor(logInterceptor)
            .build()

        val sqlNormalizedCacheFactory = SqlNormalizedCacheFactory(this, "robot")
        val cacheKeyResolver = object : CacheKeyResolver() {
            override fun fromFieldRecordSet(field: ResponseField, recordSet: Map<String, Any>): CacheKey {
                return if (recordSet["__typename"] == "Repository") {
                    CacheKey(recordSet["id"] as String)
                } else {
                    CacheKey.NO_KEY
                }
            }

            override fun fromFieldArguments(field: ResponseField, variables: Operation.Variables): CacheKey {
                return CacheKey.NO_KEY
            }
        }

        // Create the http response cache store
        val cacheStore = DiskLruHttpCacheStore(File(cacheDir, "apolloCache"), 1024 * 1024)
        val logger = ApolloAndroidLogger()

        ApolloClient.builder()
            .serverUrl(baseUrl)
//            .normalizedCache(sqlNormalizedCacheFactory, cacheKeyResolver)
//            .httpCache(ApolloHttpCache(cacheStore, logger))
            .defaultHttpCachePolicy(HttpCachePolicy.NETWORK_ONLY)
            .okHttpClient(okHttpClient)
            .build()
    }*/

    fun refreshApolloClient(){
        val logInterceptor = HttpLoggingInterceptor(
            object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    Log.d("OkHttp", message)
                }
            }
        ).apply { level = HttpLoggingInterceptor.Level.BODY }

        val okHttpClient = OkHttpClient.Builder()
            .addNetworkInterceptor { chain ->
                val request = chain.request().newBuilder()
//                    .addHeader("Authorization", "bearer ${BuildConfig.GITHUB_OAUTH_TOKEN}")
                    .header("X-ENV", "APP")
                    .header("X-APP-SYSTEM", "Android")
//                    .header("X-APP-VERSION", "1")
                    .header("X-APP-ASHOP-ID", "0")
//                    .header("X-APP-CHANNEL", "sansung")
                    .header("Accept-Language", SharePreferenceUtils.getStr(SharePreferenceUtils.LOCALE_LAN)) //LanguageUtils
                    .header("X-USER-ID", SharePreferenceUtils.readUid())
                    .header("X-ACCESS-TOKEN", SharePreferenceUtils.readToken())
                    .build()

                chain.proceed(request)
            }
            .addInterceptor(logInterceptor)
            .build()

        apolloClient=ApolloClient.builder()
            .serverUrl(baseUrl)
//            .normalizedCache(sqlNormalizedCacheFactory, cacheKeyResolver)
//            .httpCache(ApolloHttpCache(cacheStore, logger))
            .defaultHttpCachePolicy(HttpCachePolicy.NETWORK_ONLY)
            .okHttpClient(okHttpClient)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        lContext=this
        MultiDex.install(lContext)
        SharePreferenceUtils.initSp(this)
        refreshApolloClient()
        initRefreshLayoutConfig()
        
    }

    fun initRefreshLayoutConfig(){
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            layout.setPrimaryColorsId(R.color.white, android.R.color.black) //全局设置主题颜色
//            val header= BezierRadarHeader(context)
            ClassicsHeader(context)
//            header.setEnableHorizontalDrag(true)
//            ClassicsHeader(context) //.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
        }
        //设置全局的Footer构建器
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout -> //指定为经典Footer，默认是 BallPulseFooter
            val footer= BallPulseFooter(context)
            footer.setSpinnerStyle(SpinnerStyle.Scale)

        }
    }

    fun getDataSource(serviceTypes: ServiceTypes = ServiceTypes.CALLBACK): BaseDataSource {
        return when (serviceTypes) {
            ServiceTypes.CALLBACK -> ApolloCallbackService(apolloClient)
            ServiceTypes.RX_JAVA -> ApolloRxService(apolloClient)
            ServiceTypes.COROUTINES -> ApolloCoroutinesService(apolloClient)
            ServiceTypes.WATCHER -> ApolloWatcherService(apolloClient)
        }
    }

    enum class ServiceTypes {
        CALLBACK,
        RX_JAVA,
        COROUTINES,
        WATCHER
    }
}