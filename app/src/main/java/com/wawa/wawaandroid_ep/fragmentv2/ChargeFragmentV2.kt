package com.wawa.wawaandroid_ep.fragmentv2

import androidx.fragment.app.viewModels
import com.robotwar.app.BR
import com.robotwar.app.R
import com.robotwar.app.databinding.ChargeFmV2LayBinding
import com.wawa.wawaandroid_ep.base.fragment.BaseFragment
import com.wawa.wawaandroid_ep.fragmentv2.viewmodel.ChargeFmV2ViewModel

/**
 *作者：create by 张金 on 2021/6/21 10:04
 *邮箱：564813746@qq.com
 */
class ChargeFragmentV2 : BaseFragment<ChargeFmV2LayBinding, ChargeFmV2ViewModel>() {
    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): ChargeFmV2ViewModel {
        val chargeFmV2ViewModel: ChargeFmV2ViewModel by viewModels()
        return chargeFmV2ViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.charge_fm_v2_lay
    }

    override fun initFragmentView() {

    }
}