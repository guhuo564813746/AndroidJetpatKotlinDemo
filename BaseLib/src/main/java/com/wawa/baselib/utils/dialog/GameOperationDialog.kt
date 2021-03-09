package com.wawa.baselib.utils.dialog

import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView
import com.blankj.utilcode.util.SizeUtils
import com.wawa.baselib.R

/**
 *作者：create by 张金 on 2021/3/9 17:21
 *邮箱：564813746@qq.com
 */
class GameOperationDialog : BaseDialogFragment(){
    companion object{
        val DIALOG_URL="dialog_url"
        val DIALOG_TITLE="dialog_title"
    }
    override fun initDialogParams() {
        dialogWidth=SizeUtils.dp2px(420f)
        dialogHeight=SizeUtils.dp2px(300f)
    }

    override fun getLayoutId(): Int {
        return R.layout.game_operation_dailog_lay
    }

    override fun initView(view: View) {
        var dialogUrl=arguments?.getString(DIALOG_URL)
        var dialogTitle=arguments?.getString(DIALOG_TITLE)
        var tvGameopTitle=view.findViewById<TextView>(R.id.tv_gameop_title)
        tvGameopTitle.setText(dialogTitle)
        var imLangameopCancel=view.findViewById<ImageView>(R.id.im_langameop_cancel)
        var webOp=view.findViewById<WebView>(R.id.web_op)
        webOp.setBackgroundColor(resources.getColor(R.color.transparent))
        webOp.getSettings().setJavaScriptEnabled(true)
        webOp.setWebViewClient(WebViewClient())
        webOp.setWebChromeClient(WebChromeClient())
        webOp.loadUrl(dialogUrl.toString())
        imLangameopCancel.setOnClickListener {
            dismissAllowingStateLoss()
        }

    }
}