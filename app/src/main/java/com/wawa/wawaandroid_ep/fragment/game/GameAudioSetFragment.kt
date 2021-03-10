package com.wawa.wawaandroid_ep.fragment.game

import androidx.fragment.app.viewModels
import com.robotwar.app.BR
import com.robotwar.app.R
import com.robotwar.app.databinding.GameLansAudiosetLayBinding
import com.wawa.baselib.utils.SharePreferenceUtils
import com.wawa.wawaandroid_ep.base.fragment.BaseFragment
import com.wawa.wawaandroid_ep.fragment.viewmodule.game.GameAudioSetViewModel

/**
 *作者：create by 张金 on 2021/3/10 09:54
 *邮箱：564813746@qq.com
 */
class GameAudioSetFragment : BaseFragment<GameLansAudiosetLayBinding,GameAudioSetViewModel>(){
    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): GameAudioSetViewModel {
        val gameAudioSetViewModel: GameAudioSetViewModel by viewModels()
        return gameAudioSetViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.game_lans_audioset_lay
    }

    override fun initFragmentView() {
        binding.imGamebgClose.isSelected=!SharePreferenceUtils.getSwitch(SharePreferenceUtils.BGM)
        binding.imGamebgOpen.isSelected=SharePreferenceUtils.getSwitch(SharePreferenceUtils.BGM)
        binding.imGamebtClose.isSelected=!SharePreferenceUtils.getSwitch(SharePreferenceUtils.BGM_KEY)
        binding.imGamebtOpen.isSelected=SharePreferenceUtils.getSwitch(SharePreferenceUtils.BGM_KEY)
        binding.imGamevideoClose.isSelected=!SharePreferenceUtils.getSwitch(SharePreferenceUtils.BGM_LIVE)
        binding.imGamevideoOpen.isSelected=SharePreferenceUtils.getSwitch(SharePreferenceUtils.BGM_LIVE)
        binding.imGamevoiceClose.isSelected=!SharePreferenceUtils.getSwitch(SharePreferenceUtils.BGM_GAME)
        binding.imGamevoiceOpen.isSelected=SharePreferenceUtils.getSwitch(SharePreferenceUtils.BGM_GAME)
        binding.imGamebgClose.setOnClickListener {
            SharePreferenceUtils.saveSwitch(SharePreferenceUtils.BGM,false)
            binding.imGamebgClose.isSelected=true
            binding.imGamebgOpen.isSelected=false
        }
        binding.imGamebgOpen.setOnClickListener {
            SharePreferenceUtils.saveSwitch(SharePreferenceUtils.BGM,true)
            binding.imGamebgClose.isSelected=false
            binding.imGamebgOpen.isSelected=true
        }
        binding.imGamebtClose.setOnClickListener {
            SharePreferenceUtils.saveSwitch(SharePreferenceUtils.BGM_KEY,false)
            binding.imGamebtClose.isSelected=true
            binding.imGamebtOpen.isSelected=false
        }
        binding.imGamebtOpen.setOnClickListener {
            SharePreferenceUtils.saveSwitch(SharePreferenceUtils.BGM_KEY,true)
            binding.imGamebtClose.isSelected=false
            binding.imGamebtOpen.isSelected=true
        }
        binding.imGamevideoClose.setOnClickListener {
            SharePreferenceUtils.saveSwitch(SharePreferenceUtils.BGM_LIVE,false)
            binding.imGamevideoClose.isSelected=true
            binding.imGamevideoOpen.isSelected=false
        }
        binding.imGamevideoOpen.setOnClickListener {
            SharePreferenceUtils.saveSwitch(SharePreferenceUtils.BGM_LIVE,true)
            binding.imGamevideoClose.isSelected=false
            binding.imGamevideoOpen.isSelected=true
        }
        binding.imGamevoiceClose.setOnClickListener {
            SharePreferenceUtils.saveSwitch(SharePreferenceUtils.BGM_GAME,false)
            binding.imGamevoiceClose.isSelected=true
            binding.imGamevoiceOpen.isSelected=false
        }
        binding.imGamevoiceOpen.setOnClickListener {
            SharePreferenceUtils.saveSwitch(SharePreferenceUtils.BGM_GAME,true)
            binding.imGamevoiceClose.isSelected=false
            binding.imGamevoiceOpen.isSelected=true
        }

    }

}