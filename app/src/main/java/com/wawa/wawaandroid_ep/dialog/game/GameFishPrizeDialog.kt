package com.wawa.wawaandroid_ep.dialog.game

import android.content.DialogInterface
import android.view.View
import androidx.fragment.app.viewModels
import com.robotwar.app.BR
import com.robotwar.app.R
import com.robotwar.app.databinding.GameFishprizeDialogLayBinding
import com.wawa.baselib.utils.dialog.BaseVMDialogFragment
import com.wawa.wawaandroid_ep.activity.viewmodule.FishGameViewModel
import com.wawa.wawaandroid_ep.dialog.viewmodel.GameFishPrizeDialogVM

/**
 *作者：create by 张金 on 2021/7/16 14:43
 *邮箱：564813746@qq.com
 */
class GameFishPrizeDialog : BaseVMDialogFragment<GameFishprizeDialogLayBinding,GameFishPrizeDialogVM>(){
    override fun initDialogParams() {

    }

    override fun getLayoutId(): Int {
        return R.layout.game_fishprize_dialog_lay
    }

    override fun initView(view: View) {

    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        FishGameViewModel.stopSound.value=true
    }

    override fun initViewModel(): GameFishPrizeDialogVM {
        val gameFishPrizeDialogVM: GameFishPrizeDialogVM by viewModels()
        return gameFishPrizeDialogVM
    }

}