package com.wawa.wawaandroid_ep.fragment

import androidx.fragment.app.viewModels
import com.wawa.wawaandroid_ep.R
import com.wawa.wawaandroid_ep.base.fragment.BaseFragment
import com.wawa.wawaandroid_ep.databinding.FragmentMineLayBinding
import com.wawa.wawaandroid_ep.fragment.viewmodule.MineFragmentViewModel

/**
 *作者：create by 张金 on 2021/1/14 11:33
 *邮箱：564813746@qq.com
 */
class MineFragment : BaseFragment<FragmentMineLayBinding>(){
    val mineFragmentViewModel: MineFragmentViewModel by viewModels()
    override fun getLayoutId(): Int {

        return R.layout.fragment_mine_lay
    }

    override fun initFragmentView() {

    }
}