package com.wawa.baselib.utils.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.wawa.baselib.R

/**
 *作者：create by 张金 on 2021/3/10 17:26
 *邮箱：564813746@qq.com
 */
class ConfirmDialogFatory(private val positiveStr: String,
                          private val negativeStr: String,
                          private val title: String,
                          private val content: String) : DialogFragment(){

    var dialogSelectInterface: DialogSelectInterface?= null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var builder: AlertDialog.Builder?=null
        activity?.let {
            builder =
                AlertDialog.Builder(it)
            builder?.setMessage(content)
                ?.setTitle(title)
                ?.setPositiveButton(positiveStr, object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, id: Int) {
                        // FIRE ZE MISSILES!
                        dialogSelectInterface?.let {
                            it.onPositiveClick()
                        }
                    }
                })
                ?.setNegativeButton(negativeStr, object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, id: Int) {
                        dialogSelectInterface?.let {
                            it.onNegativeClick()
                        }
                    }

                })

        }
        return builder?.create()!!
    }

    fun showConfirmDialog(manager: FragmentManager){
        if (!isAdded){
            show(manager,"ConfirmDialogFatory")
        }
    }

    interface DialogSelectInterface{
        fun onPositiveClick()
        fun onNegativeClick()
    }
}