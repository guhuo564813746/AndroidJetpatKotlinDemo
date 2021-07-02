package com.wawa.wawaandroid_ep.fragment

import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.robotwar.app.BR
import com.robotwar.app.R
import com.robotwar.app.databinding.FeedbackDetailsFmLayBinding
import com.wawa.wawaandroid_ep.base.fragment.BaseFragment
import com.wawa.wawaandroid_ep.fragment.viewmodule.FeedBackDetailsFmVm

/**
 *作者：create by 张金 on 2021/7/2 14:42
 *邮箱：564813746@qq.com
 */
class FeedBackDetailsFragment : BaseFragment<FeedbackDetailsFmLayBinding,FeedBackDetailsFmVm>(){

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): FeedBackDetailsFmVm {
        val feedBackDetailsFmVm: FeedBackDetailsFmVm by viewModels()
        return feedBackDetailsFmVm
    }

    override fun getLayoutId(): Int {
        return R.layout.feedback_details_fm_lay
    }

    override fun initFragmentView() {
        viewModel.clicks.observe(this, Observer {
            when(it.id){
                R.id.rl_addphoto ->{

                }
                R.id.tv_commitfeedback ->{

                }
            }
        })
    }
}