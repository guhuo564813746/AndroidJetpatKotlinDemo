package com.wawa.wawaandroid_ep.fragment

import androidx.fragment.app.viewModels
import com.wawa.wawaandroid_ep.BR
import com.wawa.wawaandroid_ep.R
import com.wawa.wawaandroid_ep.base.fragment.BaseFragment
import com.wawa.wawaandroid_ep.databinding.SettingFmLayBinding
import com.wawa.wawaandroid_ep.fragment.viewmodule.SettingFragmentViewModel

/**
 *作者：create by 张金 on 2021/3/2 16:54
 *邮箱：564813746@qq.com
 */
class SettingFragment : BaseFragment<SettingFmLayBinding,SettingFragmentViewModel>(){
    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): SettingFragmentViewModel {
        val settingFragmentViewModel: SettingFragmentViewModel by viewModels()
        return settingFragmentViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.setting_fm_lay
    }

    override fun initFragmentView() {

    }

}