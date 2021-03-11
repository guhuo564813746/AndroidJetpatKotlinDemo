package com.wawa.testapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.wawa.testapp.utils.loginutils.WechatUtils

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var  tv_test=findViewById<TextView>(R.id.tv_test)
        var wechatUtils=WechatUtils(this)
        tv_test.setOnClickListener {
            wechatUtils.wxLogin()
        }
    }
}