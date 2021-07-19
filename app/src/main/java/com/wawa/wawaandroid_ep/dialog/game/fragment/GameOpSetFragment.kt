package com.wawa.wawaandroid_ep.dialog.game.fragment

import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.robotwar.app.BR
import com.robotwar.app.R
import com.robotwar.app.databinding.GameOpsetLayBinding
import com.wawa.baselib.utils.SharePreferenceUtils
import com.wawa.wawaandroid_ep.base.fragment.BaseFragment
import com.wawa.wawaandroid_ep.dialog.game.GameSettingDialog
import com.wawa.wawaandroid_ep.dialog.viewmodel.GameOperationSetViewModel

/**
 *作者：create by 张金 on 2021/7/15 15:44
 *邮箱：564813746@qq.com
 */
class GameOpSetFragment : BaseFragment<GameOpsetLayBinding,GameOperationSetViewModel>(){
    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): GameOperationSetViewModel {
        val gameOpSetVM: GameOperationSetViewModel by viewModels()
        return gameOpSetVM
    }

    override fun getLayoutId(): Int {
        return R.layout.game_opset_lay
    }

    override fun initFragmentView() {
        GameSettingDialog.confirmSubmit.observe(this, Observer {
            if(it){
                SharePreferenceUtils.saveSwitch(SharePreferenceUtils.FISHGAME_GETFISH_REMIND,viewModel.isFishGetRemindOpen.get())
                SharePreferenceUtils.saveSwitch(SharePreferenceUtils.FISHGAME_AUTO_TIME,viewModel.isGameFishAutoTime.get())
            }
        })
    }
}