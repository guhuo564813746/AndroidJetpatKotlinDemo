package com.wawa.wawaandroid_ep.dialog.game

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.robotwar.app.BR
import com.robotwar.app.R
import com.robotwar.app.databinding.FishResetDialogLayBinding
import com.wawa.baselib.utils.AppUtils
import com.wawa.baselib.utils.dialog.BaseVMDialogFragment
import com.wawa.wawaandroid_ep.activity.game.FishGameRoomActivity
import com.wawa.wawaandroid_ep.dialog.viewmodel.FishResetDialogVM

/**
 *
 * @ProjectName:    WawaAndroid_EP
 * @Package:        com.wawa.wawaandroid_ep.dialog.game
 * @ClassName:      GameFishResetDialog
 * @Description:     java类作用描述
 * @Author:         zhangjin
 * @CreateDate:     2021-07-19 22:19
 * @UpdateDate:     2021-07-19 22:19
 * @Version:        1.0
 */
class GameFishResetDialog : BaseVMDialogFragment<FishResetDialogLayBinding,FishResetDialogVM>(){
    override fun initDialogParams() {
        dialogWidth=AppUtils.dp2px(activity,290f)
        dialogHeight=AppUtils.dp2px(activity,440f)
    }

    override fun getLayoutId(): Int {
        return R.layout.fish_reset_dialog_lay
    }

    override fun initView(view: View) {
        viewModel.clicks.observe(this, Observer {
            when(it.id){
                R.id.tv_cancel ->{
                    dismissAllowingStateLoss()
                }
                R.id.tv_confirm ->{
                    activity?.let {
                        (it as FishGameRoomActivity).controlFishing("reset")
                    }
                    dismissAllowingStateLoss()
                }
            }
        })
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): FishResetDialogVM {
        val fishResetDialogVM: FishResetDialogVM by viewModels()
        return fishResetDialogVM
    }
}