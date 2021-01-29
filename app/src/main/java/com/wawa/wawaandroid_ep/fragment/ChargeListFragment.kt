package com.wawa.wawaandroid_ep.fragment

import androidx.fragment.app.viewModels
import com.wawa.wawaandroid_ep.R
import com.wawa.wawaandroid_ep.base.fragment.BaseFragment
import com.wawa.wawaandroid_ep.databinding.ChargeListFmLayBinding
import com.wawa.wawaandroid_ep.fragment.viewmodule.ChargeItemViewModel

/**
 *作者：create by 张金 on 2021/1/29 11:28
 *邮箱：564813746@qq.com
 */
class ChargeListFragment : BaseFragment<ChargeListFmLayBinding>() {
    val chargeItemViewModel: ChargeItemViewModel by viewModels()
    override fun getLayoutId(): Int {
        return R.layout.charge_list_fm_lay
    }

    override fun initFragmentView() {

    }

}