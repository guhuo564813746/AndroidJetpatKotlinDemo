package com.wawa.wawaandroid_ep.dialog.game

import android.content.DialogInterface
import android.view.View
import androidx.fragment.app.viewModels
import com.robotwar.app.BR
import com.robotwar.app.R
import com.robotwar.app.databinding.GifDialogLayBinding
import com.wawa.baselib.utils.dialog.BaseVMDialogFragment
import com.wawa.wawaandroid_ep.activity.viewmodule.FishGameViewModel
import com.wawa.wawaandroid_ep.base.viewmodel.BaseImpViewModel

/**
 *作者：create by 张金 on 2021/7/16 14:43
 *邮箱：564813746@qq.com
 */
class GifDialog : BaseVMDialogFragment<GifDialogLayBinding,BaseImpViewModel>(){
    override fun initDialogParams() {

    }


    override fun getLayoutId(): Int {
        return R.layout.gif_dialog_lay
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

    override fun initViewModel(): BaseImpViewModel {
        val baseImpViewModel: BaseImpViewModel by viewModels()
        return baseImpViewModel
    }

}