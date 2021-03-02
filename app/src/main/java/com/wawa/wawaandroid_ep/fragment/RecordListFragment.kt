package com.wawa.wawaandroid_ep.fragment

import androidx.fragment.app.viewModels
import com.wawa.wawaandroid_ep.BR
import com.wawa.wawaandroid_ep.R
import com.wawa.wawaandroid_ep.base.fragment.BaseFragment
import com.wawa.wawaandroid_ep.databinding.UserRecordLayBinding
import com.wawa.wawaandroid_ep.fragment.viewmodule.RecordListFragmentViewModel

/**
 *作者：create by 张金 on 2021/3/2 16:59
 *邮箱：564813746@qq.com
 */
class RecordListFragment : BaseFragment<UserRecordLayBinding,RecordListFragmentViewModel>(){
    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): RecordListFragmentViewModel {
        val recordListFragmentViewModel: RecordListFragmentViewModel by viewModels()
        return recordListFragmentViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.user_record_lay
    }

    override fun initFragmentView() {

    }

}