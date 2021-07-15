package com.wawa.wawaandroid_ep.dialog.game.fragment

import androidx.fragment.app.viewModels
import com.robotwar.app.BR
import com.robotwar.app.R
import com.robotwar.app.databinding.AudioSetLayBinding
import com.wawa.wawaandroid_ep.base.fragment.BaseFragment
import com.wawa.wawaandroid_ep.dialog.viewmodel.AudioSetViewModel

/**
 *作者：create by 张金 on 2021/7/15 14:55
 *邮箱：564813746@qq.com
 */
class AudioSetFragment : BaseFragment<AudioSetLayBinding,AudioSetViewModel>(){
    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): AudioSetViewModel {
        val audioViewModel: AudioSetViewModel by viewModels()
        return audioViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.audio_set_lay
    }

    override fun initFragmentView() {

    }

}