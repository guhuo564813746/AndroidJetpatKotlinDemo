package com.wawa.wawaandroid_ep.fragment

import androidx.fragment.app.viewModels
import com.wawa.wawaandroid_ep.R
import com.wawa.wawaandroid_ep.base.fragment.BaseFragment
import com.wawa.wawaandroid_ep.databinding.FragmentLoginLayBinding
import com.wawa.wawaandroid_ep.fragment.viewmodule.LoginViewModel

/**
 *作者：create by 张金 on 2021/1/14 15:36
 *邮箱：564813746@qq.com
 */
class LoginFragment : BaseFragment<FragmentLoginLayBinding>() {
    private val loginViewModel: LoginViewModel by viewModels()
    override fun getLayoutId(): Int {
        return R.layout.fragment_login_lay
    }

    override fun initFragmentView() {

    }
}