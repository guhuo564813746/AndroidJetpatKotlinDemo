package com.wawa.wawaandroid_ep.fragment

import androidx.fragment.app.viewModels
import com.wawa.wawaandroid_ep.BR
import com.wawa.wawaandroid_ep.R
import com.wawa.wawaandroid_ep.base.fragment.BaseFragment
import com.wawa.wawaandroid_ep.databinding.ProfileFmLayBinding
import com.wawa.wawaandroid_ep.fragment.viewmodule.ProfileFragmentViewModel

/**
 *作者：create by 张金 on 2021/3/2 16:57
 *邮箱：564813746@qq.com
 */
class ProfileFragment : BaseFragment<ProfileFmLayBinding,ProfileFragmentViewModel>(){
    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): ProfileFragmentViewModel {
        val profileFragmentViewModel: ProfileFragmentViewModel by viewModels()
        return profileFragmentViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.profile_fm_lay
    }

    override fun initFragmentView() {

    }
}