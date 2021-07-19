package com.wawa.wawaandroid_ep.dialog.game

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.robotwar.app.BR
import com.robotwar.app.R
import com.robotwar.app.databinding.QuitGamePortdialogLayBinding
import com.wawa.baselib.utils.AppUtils
import com.wawa.baselib.utils.dialog.BaseVMDialogFragment
import com.wawa.wawaandroid_ep.dialog.viewmodel.GameQuitPortDialogVm

/**
 *作者：create by 张金 on 2021/7/6 15:26
 *邮箱：564813746@qq.com
 */
class GameQuit_PortDialog : BaseVMDialogFragment<QuitGamePortdialogLayBinding,GameQuitPortDialogVm>(){
    companion object{
        val TAG="GameQuit_PortDialog"
    }
    var isQuit=true
    var listener: GameQuitDialogCallback?=null

    override fun initDialogParams() {
        dialogWidth=AppUtils.dp2px(activity,290f)
        dialogHeight=AppUtils.dp2px(activity,204f)
    }

    override fun getLayoutId(): Int {
        return R.layout.quit_game_portdialog_lay
    }

    override fun initView(view: View) {
        viewModel.clicks.observe(this, Observer {
            when(it.id){
                R.id.bt_quitgame_room ->{
                    dismissAllowingStateLoss()
                    activity?.finish()
                }
                R.id.tv_keepgame ->{
                    dismissAllowingStateLoss()
                }
                R.id.tv_quitgame ->{
                    listener?.let {
                        it.onQuitGame()
                    }
                    dismissAllowingStateLoss()
                }
            }
        })
        if (isQuit){
            binding.llTwobtBottom.setVisibility(View.VISIBLE)
            binding.btQuitgameRoom.setVisibility(View.GONE)
//            binding.tvGameClearingTips.setText(getString(R.string.GAME_LOCK_TIPS5))
        }else{
            binding.llTwobtBottom.setVisibility(View.GONE)
            binding.btQuitgameRoom.setVisibility(View.VISIBLE)
//            binding.tvGameClearingTips.setText(getString(R.string.GAME_LOCK_TIPS6))
        }
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): GameQuitPortDialogVm {
        val gameQuitPortDialogVm: GameQuitPortDialogVm by viewModels()
        return gameQuitPortDialogVm
    }

    interface GameQuitDialogCallback{
        fun onQuitGame()
    }

}