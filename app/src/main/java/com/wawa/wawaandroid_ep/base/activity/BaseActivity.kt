package com.wawa.wawaandroid_ep.base.activity

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 *作者：create by 张金 on 2021/1/13 16:17
 *邮箱：564813746@qq.com
 */
abstract class BaseActivity<V : ViewDataBinding> : AppCompatActivity() {
    protected lateinit var binding : V
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,initContentView(savedInstanceState))
        initView()
    }

    abstract fun initContentView(savedInstanceState: Bundle?): Int
    abstract fun initView()

}