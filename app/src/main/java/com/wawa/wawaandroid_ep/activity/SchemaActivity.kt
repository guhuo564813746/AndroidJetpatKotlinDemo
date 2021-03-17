package com.wawa.wawaandroid_ep.activity

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.wawa.wawaandroid_ep.base.activity.BaseActivity

/**
 *作者：create by 张金 on 2021/3/17 18:26
 *邮箱：564813746@qq.com
 */
class SchemaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        var data=intent.data
        Log.i("TAG", "host = ${data?.host} path = ${data?.path} query = ${data?.query}")
//        val param = data?.getQueryParameter("goodsId")
//        Log.i("TAG", "param: $param")
    }
}