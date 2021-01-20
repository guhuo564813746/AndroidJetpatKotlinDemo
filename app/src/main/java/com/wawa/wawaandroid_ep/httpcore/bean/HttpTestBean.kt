package com.wawa.wawaandroid_ep.httpcore.bean

/**
 *作者：create by 张金 on 2021/1/20 11:31
 *邮箱：564813746@qq.com
 */
data class ForecastsBean(
        val city: String, val adcode: String, val province: String,
        val reporttime: String, val casts: List<CastsBean>
)

data class CastsBean(
        val date: String, val week: String, val dayweather: String, val nightweather: String, val daytemp: String,
        val nighttemp: String, val daywind: String, val nightwind: String, val daypower: String, val nightpower: String
)

data class DistrictBean(
        val adcode: String,
        val center: String,
        val level: String,
        val name: String,
        val districts: List<DistrictBean>
)