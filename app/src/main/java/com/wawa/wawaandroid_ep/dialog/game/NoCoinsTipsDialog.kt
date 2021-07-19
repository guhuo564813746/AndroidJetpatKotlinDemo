package com.wawa.wawaandroid_ep.dialog.game

import android.os.Handler
import android.os.Message
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.robotwar.app.BR
import com.robotwar.app.R
import com.robotwar.app.databinding.NocoinstipsDialogLayBinding
import com.wawa.baselib.utils.AppUtils
import com.wawa.baselib.utils.dialog.BaseVMDialogFragment
import com.wawa.wawaandroid_ep.activity.game.GameBaseActivity
import com.wawa.wawaandroid_ep.dialog.viewmodel.NoCoinsTipsDialogVM

/**
 *作者：create by 张金 on 2021/7/19 11:06
 *邮箱：564813746@qq.com
 */
class NoCoinsTipsDialog : BaseVMDialogFragment<NocoinstipsDialogLayBinding,NoCoinsTipsDialogVM>(){
    private var mHandler: Handler?=null
    private val TOPUP_COUNTER=0
    private var timeCount=10

    override fun initDialogParams() {
        dialogWidth=AppUtils.dp2px(activity,290f)
        dialogHeight=AppUtils.dp2px(activity,245f)
    }

    override fun getLayoutId(): Int {
        return R.layout.nocoinstips_dialog_lay
    }

    override fun initView(view: View) {
        mHandler=object: Handler(){
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                when(msg.what){
                    TOPUP_COUNTER->{
                        if (timeCount > 0){
                            binding.tvTimecounter.setText("$timeCount s")
                            timeCount--
                            mHandler?.sendEmptyMessageDelayed(TOPUP_COUNTER,1000)
                        }else{
                            dismissAllowingStateLoss()
                        }
                    }
                }
            }
        }
        mHandler?.sendEmptyMessage(TOPUP_COUNTER)
        initViewObservable()
    }

    fun initViewObservable(){
        activity?.let {
            viewModel.clicks.observe(this, Observer {
                when(it.id){
                    R.id.btn_leave_game ->{
                        dismissAllowingStateLoss()
                    }
                    R.id.btn_topup_now ->{
                        (activity as GameBaseActivity<*, *>).viewModel.topUpTipsDialogShow.value=true
                        dismissAllowingStateLoss()
                    }
                }
            })
        }
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): NoCoinsTipsDialogVM {
        val noCoinsTipsDialogVM: NoCoinsTipsDialogVM by viewModels()
        return noCoinsTipsDialogVM
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mHandler?.let {
            it.removeCallbacksAndMessages(null)
            mHandler=null
        }
    }

}