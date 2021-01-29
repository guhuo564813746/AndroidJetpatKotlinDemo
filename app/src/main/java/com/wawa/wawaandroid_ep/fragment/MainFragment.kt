package com.wawa.wawaandroid_ep.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.wawa.wawaandroid_ep.R
import com.wawa.wawaandroid_ep.base.fragment.BaseFragment
import com.wawa.wawaandroid_ep.databinding.FragmentMainLayBinding
import com.wawa.wawaandroid_ep.fragment.viewmodule.MainFragmentViewModel

/**
 *作者：create by 张金 on 2021/1/13 18:08
 *邮箱：564813746@qq.com
 */
class MainFragment : BaseFragment<FragmentMainLayBinding>() {
    val mainFragmentViewModel: MainFragmentViewModel by viewModels()
    override fun getLayoutId(): Int {
        return R.layout.fragment_main_lay
    }

    override fun initFragmentView() {

    }

}