package com.wawa.wawaandroid_ep.dialog.game

import android.view.View
import androidx.fragment.app.viewModels
import com.robotwar.app.BR
import com.robotwar.app.R
import com.robotwar.app.databinding.GameBuytimeDialogLayBinding
import com.wawa.baselib.utils.AppUtils
import com.wawa.baselib.utils.dialog.BaseVMDialogFragment
import com.wawa.wawaandroid_ep.dialog.viewmodel.GameFishBuyTimeDialogVM

/**
 *作者：create by 张金 on 2021/7/19 18:56
 *邮箱：564813746@qq.com
 */
class GameFishBuyTimeDialog : BaseVMDialogFragment<GameBuytimeDialogLayBinding,GameFishBuyTimeDialogVM>(){
    override fun initDialogParams() {
        dialogWidth=AppUtils.dp2px(activity,290f)
        dialogHeight=AppUtils.dp2px(activity,440f)
    }

    override fun getLayoutId(): Int {
        return R.layout.game_buytime_dialog_lay
    }

    override fun initView(view: View) {

    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): GameFishBuyTimeDialogVM {
        val gameFishBuyTimeDialog: GameFishBuyTimeDialogVM by viewModels()
        return gameFishBuyTimeDialog
    }
}