package com.wawa.wawaandroid_ep.activity.web

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import com.robotwar.app.BR
import com.robotwar.app.R
import com.robotwar.app.databinding.WebLayBinding
import com.wawa.wawaandroid_ep.activity.viewmodule.WebViewModel
import com.wawa.wawaandroid_ep.base.activity.BaseActivity

/**
 *作者：create by 张金 on 2021/3/11 17:35
 *邮箱：564813746@qq.com
 */
class WebActivity : BaseActivity<WebLayBinding,WebViewModel>(){
    companion object{
        val TAG="WebActivity"
        val WEB_URL="web_url"
        val WEB_TITLE="web_title"
    }
    var webUrl=""
    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): WebViewModel {
        val webViewModel: WebViewModel by viewModels()
        return webViewModel
    }

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.web_lay
    }

    override fun initView() {
        var title=binding.baseTitleView.findViewById<TextView>(R.id.title)
        var imBack=binding.baseTitleView.findViewById<ImageView>(R.id.im_back)
        imBack.setOnClickListener {
            finish()
        }
        title.setText(intent.getStringExtra(WEB_TITLE).toString())
        webUrl= intent.getStringExtra(WEB_URL).toString()
        binding.web.webViewClient= WebViewClient()
        binding.web.webChromeClient= WebChromeClient()
        binding.web.settings.javaScriptEnabled=true
        binding.web.settings.domStorageEnabled=true
        if (!webUrl.isNullOrEmpty()){
            binding.web.loadUrl(webUrl)
        }

    }
}