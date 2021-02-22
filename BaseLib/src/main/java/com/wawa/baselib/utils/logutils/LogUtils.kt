package com.wawa.baselib.utils.logutils

import android.content.Context
import android.util.Log
import com.wawa.baselib.BuildConfig

/**
 *作者：create by 张金 on 2021/2/22 11:37
 *邮箱：564813746@qq.com
 */
class LogUtils {
    companion object{
        fun d(tag: String,content: String){
            if (BuildConfig.DEBUG){
                Log.d(tag,content)
            }
        }

        fun e(tag: String,errStr: String){
            if (BuildConfig.DEBUG){
                Log.e(tag,errStr)
            }
        }

    }
}