package com.wawa.wawaandroid_ep.httpcore.apiservice

import com.wawa.wawaandroid_ep.httpcore.bean.ForecastsBean
import com.wawa.wawaandroid_ep.httpcore.bean.HttpWrapBean
import retrofit2.http.GET
import retrofit2.http.Query

/**
 *作者：create by 张金 on 2021/1/20 10:57
 *邮箱：564813746@qq.com
 */
interface ApiService {
    @GET("weather/weatherInfo?extensions=all")
    suspend fun getWeather(@Query("city") city: String): HttpWrapBean<List<ForecastsBean>>
}