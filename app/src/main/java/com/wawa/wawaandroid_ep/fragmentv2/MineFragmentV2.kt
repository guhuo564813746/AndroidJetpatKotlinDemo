package com.wawa.wawaandroid_ep.fragmentv2

import androidx.fragment.app.viewModels
import com.robotwar.app.BR
import com.robotwar.app.R
import com.robotwar.app.databinding.MineFmV2LayBinding
import com.wawa.wawaandroid_ep.base.fragment.BaseFragment
import com.wawa.wawaandroid_ep.fragmentv2.viewmodel.MineFmV2ViewModel

/**
 *作者：create by 张金 on 2021/6/21 10:02
 *邮箱：564813746@qq.com
 */
class MineFragmentV2 : BaseFragment<MineFmV2LayBinding,MineFmV2ViewModel>(){
    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): MineFmV2ViewModel {
        val mineFmV2ViewModel: MineFmV2ViewModel by viewModels()
        return mineFmV2ViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.mine_fm_v2_lay
    }

    override fun initFragmentView() {

    }
}