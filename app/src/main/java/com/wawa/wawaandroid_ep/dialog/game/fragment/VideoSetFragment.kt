package com.wawa.wawaandroid_ep.dialog.game.fragment

import androidx.fragment.app.viewModels
import com.robotwar.app.BR
import com.robotwar.app.R
import com.robotwar.app.databinding.VideoSetLayBinding
import com.wawa.wawaandroid_ep.base.fragment.BaseFragment
import com.wawa.wawaandroid_ep.dialog.viewmodel.VideoSetViewModel

/**
 *作者：create by 张金 on 2021/7/15 15:20
 *邮箱：564813746@qq.com
 */
class VideoSetFragment : BaseFragment<VideoSetLayBinding,VideoSetViewModel>(){
    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): VideoSetViewModel {
        val videoSetViewModel: VideoSetViewModel by viewModels()
        return videoSetViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.video_set_lay
    }

    override fun initFragmentView() {

    }
}