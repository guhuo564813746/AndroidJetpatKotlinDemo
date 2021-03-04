package com.wawa.baselib.utils.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.wawa.baselib.R

/**
 *作者：create by 张金 on 2021/3/4 17:46
 *邮箱：564813746@qq.com
 */
class PayTypeDialog(
                    private var callback:PayTypeCallback
    ) : DialogFragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        var view =inflater.inflate(R.layout.paytype_port_dialog_lay, container, false)
        initView(view)
        return view
    }

    fun initView(view: View){

    }

    interface PayTypeCallback{
        fun payTypeConfirm(payType: Int)
        fun payTypeCancel()
    }
}