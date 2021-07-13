package com.wawa.baselib.utils.dialog

import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.widget.TextView
import com.blankj.utilcode.util.SizeUtils
import com.wawa.baselib.R

/**
 *作者：create by 张金 on 2021/3/8 15:52
 *邮箱：564813746@qq.com
 */
class GameReadyDialog : BaseDialogFragment(){
    private var timeLeft=9
    private var isViewInit=false
    private var isConfirm=false
    private var isCancel=false
    var gameReadyInterface: GameReadyInterface?= null
    var btContinuteGame:TextView?= null
    override fun initDialogParams() {
        dialogWidth=SizeUtils.dp2px(290f)
        dialogHeight=SizeUtils.dp2px(350f)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    fun setTimes(time: Int){
        this.timeLeft=time
        if (btContinuteGame != null && isViewInit){
            btContinuteGame?.setText(getString(R.string.start_game)+"(${timeLeft}s)")
        }
        if (timeLeft==0){
            if (gameReadyInterface != null && isViewInit){
                gameReadyInterface?.cancelGame()
                dismissAllowingStateLoss()
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.game_ready_dialog_lay
    }

    override fun initView(view: View) {
//        isCancelable=false
        var btnCancel=view.findViewById<TextView>(R.id.btn_cancel)
        btContinuteGame=view.findViewById(R.id.btn_confirm)
        btnCancel.setOnClickListener {
            if (gameReadyInterface != null){
                gameReadyInterface?.cancelGame()
            }
            isCancel=true
            dismissAllowingStateLoss()
        }
        btContinuteGame?.setOnClickListener {
            if (gameReadyInterface != null){
                gameReadyInterface?.continuteGame()
            }
            isConfirm=true
            dismissAllowingStateLoss()
        }
        isViewInit=true
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (!isCancel && !isConfirm){
            if (gameReadyInterface != null){
                gameReadyInterface?.cancelGame()
            }
        }
    }

    interface GameReadyInterface{
        fun continuteGame()
        fun cancelGame()
    }

}