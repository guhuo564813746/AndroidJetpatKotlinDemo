package com.wawa.wawaandroid_ep.dialog.game

import android.view.View
import androidx.fragment.app.viewModels
import com.robotwar.app.BR
import com.robotwar.app.R
import com.robotwar.app.databinding.GameSettingDialogLayBinding
import com.wawa.baselib.utils.dialog.BaseVMDialogFragment
import com.wawa.wawaandroid_ep.dialog.viewmodel.GameSettingVM

/**
 *作者：create by 张金 on 2021/7/13 15:12
 *邮箱：564813746@qq.com
 */
class GameSettingDialog : BaseVMDialogFragment<GameSettingDialogLayBinding,GameSettingVM>(){
    override fun initDialogParams() {

    }

    override fun getLayoutId(): Int {
        return R.layout.game_setting_dialog_lay
    }

    override fun initView(view: View) {

    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): GameSettingVM {
        val gameSettingVM: GameSettingVM by viewModels()
        return gameSettingVM
    }
}