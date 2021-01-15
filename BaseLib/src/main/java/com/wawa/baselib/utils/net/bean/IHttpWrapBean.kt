package com.wawa.baselib.utils.net.bean

/**
 *作者：create by 张金 on 2021/1/15 14:37
 *邮箱：564813746@qq.com
 * 规范后台返回的数据
 */
interface IHttpWrapBean<Data> {
    val httpCode: Int
    val httpMsg: String
    val httpData: Data
    val httpIsSuccess: Boolean
}