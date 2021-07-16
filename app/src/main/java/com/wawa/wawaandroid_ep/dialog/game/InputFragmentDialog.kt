package com.wawa.wawaandroid_ep.dialog.game

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.drawable.GradientDrawable
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ScreenUtils
import com.robotwar.app.BR
import com.robotwar.app.R
import com.robotwar.app.databinding.InputFmLayBinding
import com.wawa.baselib.utils.AppUtils
import com.wawa.baselib.utils.dialog.BaseVMDialogFragment
import com.wawa.baselib.utils.keyboard.KeyboardUtils
import com.wawa.wawaandroid_ep.WawaApp
import com.wawa.wawaandroid_ep.fragment.viewmodule.InputFragmentVM

/**
 *作者：create by 张金 on 2021/7/7 10:34
 *邮箱：564813746@qq.com
 */
class InputFragmentDialog : BaseVMDialogFragment<InputFmLayBinding,InputFragmentVM>(){
    companion object{
        val TAG="InputFragmentDialog"
    }
    var msgSendable=false
    val sendBg= GradientDrawable()
    override fun initDialogParams() {
        dialogWidth=WindowManager.LayoutParams.MATCH_PARENT
        activity?.let {
            dialogHeight=AppUtils.dp2px(it,40f)
        }
    }


    override fun createDialog(): Dialog {
        Log.d(TAG,"createDialog--")
        activity?.let {
            val dialog = Dialog(it, R.style.dialog2)
            dialog.setCanceledOnTouchOutside(true)
            dialog.setCancelable(true)
            val window = dialog.window
            val params = window!!.attributes
            params.width = dialogWidth
            params.height = dialogHeight
            params.gravity = Gravity.BOTTOM
            window!!.attributes = params
            window?.setWindowAnimations(com.wawa.baselib.R.style.bottomToTopAnim)
            return dialog
        }
        return super.createDialog()
    }

    override fun getLayoutId(): Int {
        return R.layout.input_fm_lay
    }

    override fun initView(view: View) {
        viewModel.clicks.observe(this, Observer {
            when(it.id){
                R.id.btn_send ->{
                    sendMsg()
                }
            }
        })
        sendBg.cornerRadius=AppUtils.dp2px(activity,20f).toFloat()
        sendBg.setColor(resources.getColor(R.color.lineColor))
        activity?.let {
            KeyboardUtils.registerSoftInputChangedListener(it,object:
                KeyboardUtils.OnSoftInputChangedListener {
                override fun onSoftInputChanged(height: Int) {
                    Log.d(TAG,"onSoftInputChanged--"+height)
                    if (height == 0){
                        if (isAdded){
                            dismissAllowingStateLoss()
                        }
                    }
                }
            })
        }
        binding.input.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEND){
                //发送消息
                sendMsg()
                true
            }
            false
        }
        binding.input.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s == null || s.length ==0){
                    msgSendable=false
                    sendBg.setColor(resources.getColor(R.color.lineColor))
                }else{
                    msgSendable=true
                    sendBg.setColor(resources.getColor(R.color.roseRed))
                }
                binding.btnSend.background=sendBg
            }
        })
        binding.input.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                true
            }
            false
        }
        binding.btnSend.background=sendBg

        showKeyBoard()
    }

    fun showKeyBoard(){
        KeyboardUtils.showSoftInput(binding.input)
    }

    fun hideKeyBoard(){
        KeyboardUtils.hideKeyboard(binding.input)
    }

    fun sendMsg(){
        if (msgSendable){
            activity?.let {
                (it.application as WawaApp).sendMsg.value=binding.input.text.toString().trim()
            }
            binding.input.setText("")
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        Log.d(TAG,"onCancel--")
        hideKeyBoard()
        super.onCancel(dialog)
    }

    override fun onDismiss(dialog: DialogInterface) {
        Log.d(TAG,"onDismiss--")
        super.onDismiss(dialog)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): InputFragmentVM {
        val inputFragmentVM: InputFragmentVM by viewModels()
        return inputFragmentVM
    }
}