package com.wawa.wawaandroid_ep.fragmentv2

import androidx.fragment.app.viewModels
import com.blankj.utilcode.util.BarUtils
import com.robotwar.app.BR
import com.robotwar.app.R
import com.robotwar.app.databinding.MainFmV2LayBinding
import com.wawa.wawaandroid_ep.base.fragment.BaseFragment
import com.wawa.wawaandroid_ep.fragmentv2.viewmodel.MainFmV2ViewModel

/**
 *作者：create by 张金 on 2021/6/21 10:38
 *邮箱：564813746@qq.com
 */
class MainFragmentV2 : BaseFragment<MainFmV2LayBinding,MainFmV2ViewModel>(){
    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): MainFmV2ViewModel {
        val mainFmV2ViewModel: MainFmV2ViewModel by viewModels()
        return mainFmV2ViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.main_fm_v2_lay
    }

    override fun initFragmentView() {

    }
}