package com.wawa.wawaandroid_ep.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.FragmentTransaction
import com.robotwar.app.BR
import com.robotwar.app.R
import com.robotwar.app.databinding.LoginActivityLayBinding
import com.wawa.wawaandroid_ep.activity.viewmodule.LoginActivityVm
import com.wawa.wawaandroid_ep.base.activity.BaseActivity
import com.wawa.wawaandroid_ep.fragment.LoginFragment

/**
 *作者：create by 张金 on 2021/6/22 15:31
 *邮箱：564813746@qq.com
 */
class LoginActivity : BaseActivity<LoginActivityLayBinding,LoginActivityVm>(){
    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): LoginActivityVm {
        val loginActivityVm: LoginActivityVm by viewModels()
        return loginActivityVm
    }

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.login_activity_lay
    }

    override fun initView() {
        val loginFragment=LoginFragment()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_container,loginFragment)
            addToBackStack(null)
        }.commitAllowingStateLoss()
    }

}