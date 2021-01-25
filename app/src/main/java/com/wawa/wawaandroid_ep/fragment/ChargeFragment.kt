package com.wawa.wawaandroid_ep.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.wawa.wawaandroid_ep.R
import com.wawa.wawaandroid_ep.base.fragment.BaseFragment
import com.wawa.wawaandroid_ep.databinding.FragmentChargeLayBinding
import com.wawa.wawaandroid_ep.fragment.viewmodule.ChargeFragmentViewModel

/**
 *作者：create by 张金 on 2021/1/14 11:29
 *邮箱：564813746@qq.com
 */
class ChargeFragment : BaseFragment<FragmentChargeLayBinding>(){
    private val chaegeViewModel: ChargeFragmentViewModel by viewModels()
    override fun getLayoutId(): Int {
        return R.layout.fragment_charge_lay
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chaegeViewModel.loadChargeList()
    }

    override fun initFragmentView() {

    }

}