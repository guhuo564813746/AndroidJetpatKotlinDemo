package com.wawa.wawaandroid_ep.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.wawa.wawaandroid_ep.R
import com.wawa.wawaandroid_ep.base.fragment.BaseFragment
import com.wawa.wawaandroid_ep.databinding.FragmentLoginLayBinding
import com.wawa.wawaandroid_ep.fragment.viewmodule.LoginViewModel
import com.wawa.wawaandroid_ep.utils.loginutils.PhoneLoginDialog
import com.wawa.wawaandroid_ep.utils.loginutils.WechatUtils
import com.wowgotcha.robot.wxapi.WXEntryActivity.WXLOGIN_ACTION

/**
 *作者：create by 张金 on 2021/1/14 15:36
 *邮箱：564813746@qq.com
 */
class LoginFragment : BaseFragment<FragmentLoginLayBinding>() {
    companion object{
        private val TAG: String ="LoginFragment"
    }
    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var wechatUtils: WechatUtils
    private lateinit var loginDialog: PhoneLoginDialog
    override fun getLayoutId(): Int {
        return R.layout.fragment_login_lay
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginDialog=PhoneLoginDialog.Builder(activity)
            .setWxBtnOnClickListener {

            }
            .setConfirmOnClickListener { v, phoneNum, code ->

            }
            .create()
    }

    override fun initFragmentView() {
        wechatUtils= WechatUtils(activity)

        binding.btnWx.setOnClickListener {
            Log.d(TAG,"wxLogin--")
            wechatUtils.wxLogin()
        }
        binding.btnPhoneShow.setOnClickListener{
            loginDialog.show()
        }
    }

    private inner class LoginBroarcastReceiver : BroadcastReceiver() {
        override fun onReceive(
            context: Context,
            intent: Intent
        ) {
            val action = intent.action
            if (!TextUtils.isEmpty(action) && action == WXLOGIN_ACTION) {
                val wxCode: String? = intent.getStringExtra("wxCode")
                //微信登陆
                if (!TextUtils.isEmpty(wxCode)) {
                    loginViewModel.wxLogin(wxCode?:"")
                } else {
                    Toast.makeText(activity,"wechat auth failed",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}