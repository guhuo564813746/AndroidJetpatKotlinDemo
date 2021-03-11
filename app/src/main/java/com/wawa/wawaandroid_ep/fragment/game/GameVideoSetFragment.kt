package com.wawa.wawaandroid_ep.fragment.game

import androidx.fragment.app.viewModels
import com.robotwar.app.BR
import com.robotwar.app.R
import com.robotwar.app.databinding.LansgameVideoSetBinding
import com.wawa.wawaandroid_ep.base.fragment.BaseFragment
import com.wawa.wawaandroid_ep.fragment.viewmodule.game.GameVidioSetFragmentViewModel

/**
 *作者：create by 张金 on 2021/3/10 10:33
 *邮箱：564813746@qq.com
 */
class GameVideoSetFragment : BaseFragment<LansgameVideoSetBinding,GameVidioSetFragmentViewModel>(){
    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): GameVidioSetFragmentViewModel {
        val gameVideoSetFragmentViewModel: GameVidioSetFragmentViewModel by viewModels()
        return gameVideoSetFragmentViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.lansgame_video_set
    }

    override fun initFragmentView() {

    }
}