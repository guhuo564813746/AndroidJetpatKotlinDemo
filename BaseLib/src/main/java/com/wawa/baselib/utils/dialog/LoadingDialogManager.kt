package com.wawa.baselib.utils.dialog

import android.app.Dialog
import android.content.Context
import android.view.View
import android.widget.TextView
import com.wawa.baselib.R

/**
 *作者：create by 张金 on 2021/3/10 17:42
 *邮箱：564813746@qq.com
 */
class LoadingDialogManager {
    companion object{
        fun loadBigDialog(
            context: Context,
            text: String
        ): Dialog? {
            val dialog = Dialog(context, R.style.dialogLoading)
            dialog.setContentView(R.layout.loading_dialog_lay)
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
            if ("" != text) {
                val titleView =
                    dialog.findViewById<View>(R.id.text) as TextView
                titleView.text = text
            }
            return dialog
        }
    }
}