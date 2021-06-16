package com.wawa.wawaandroid_ep.fragment

import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.TextUtils
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.blankj.utilcode.util.ToastUtils
import com.robotwar.app.BR
import com.robotwar.app.R
import com.robotwar.app.databinding.FragmentLoginLayBinding
import com.robotwar.app.wxapi.WXEntryActivity
import com.robotwar.app.wxapi.WXEntryActivity.WXLOGIN_ACTION
import com.wawa.baselib.utils.AppUtils
import com.wawa.baselib.utils.SharePreferenceUtils
import com.wawa.baselib.utils.dialog.LoadingDialogManager
import com.wawa.wawaandroid_ep.MainActivity
import com.wawa.wawaandroid_ep.MainViewModule
import com.wawa.wawaandroid_ep.WawaApp
import com.wawa.wawaandroid_ep.activity.LongTextActivity
import com.wawa.wawaandroid_ep.activity.web.WebActivity
import com.wawa.wawaandroid_ep.base.fragment.BaseFragment
import com.wawa.wawaandroid_ep.fragment.viewmodule.LoginViewModel
import com.wawa.wawaandroid_ep.utils.DialogUitl
import com.wawa.wawaandroid_ep.utils.loginutils.PhoneLoginDialog
import com.wawa.wawaandroid_ep.utils.loginutils.WechatUtils

/**
 *作者：create by 张金 on 2021/1/14 15:36
 *邮箱：564813746@qq.com
 */
class LoginFragment : BaseFragment<FragmentLoginLayBinding,LoginViewModel>() {
    companion object{
        private val TAG: String ="LoginFragment"
    }
    private lateinit var loginReceiver: LoginBroarcastReceiver
    private lateinit var wechatUtils: WechatUtils
    private lateinit var loginDialog: PhoneLoginDialog
    private var loginAgreementDialog: Dialog? = null
    private var app_chanel: String? = null
    override fun getLayoutId(): Int {
        return R.layout.fragment_login_lay
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginDialog=PhoneLoginDialog.Builder(activity)
            .setWxBtnOnClickListener {

            }
            .setConfirmOnClickListener { v, phoneNum, code ->

                if (phoneNum.isNullOrEmpty()){
                    ToastUtils.showShort(getString(R.string.please_input_phone_num))
                }else if (code.isNullOrEmpty()){
                    ToastUtils.showShort(getString(R.string.please_input_validate_code))
                }else{
                    viewModel.phoneLogin(phoneNum, code)
                }

            }
            .create()
        val backPressCallback=requireActivity().onBackPressedDispatcher.addCallback (this){
            requireActivity().finish()
        }
        backPressCallback.isEnabled
    }

    fun isAgreeCheck(): Boolean{
        var isCheck=binding.checkboxAgreement.isChecked
        return isCheck
    }


    override fun initFragmentView() {
        loginReceiver=LoginBroarcastReceiver()
        activity?.registerReceiver(loginReceiver,IntentFilter(WXEntryActivity.WXLOGIN_ACTION))
        binding.textAgreement.setOnClickListener {
            goAgreeMentWebPage()
        }
        activity?.findViewById<View>(R.id.view_main_bg)?.visibility=View.GONE
        binding.tvLoginTips.setText(
            String.format(resources.getString(R.string.LOGIN_NEW_USER_GIFT),getString(R.string.COIN)))
        wechatUtils= WechatUtils(activity)
        binding.btnWx.setOnClickListener {
            Log.d(TAG,"wxLogin--")
            if (!isAgreeCheck()){
                ToastUtils.showShort(getString(R.string.user_service_terms))
            }else{
                wechatUtils.wxLogin()
            }
        }
        binding.btnPhoneShow.setOnClickListener{
            if (!isAgreeCheck()){
                ToastUtils.showShort(getString(R.string.user_service_terms))
            }else{
                loginDialog.show()
            }
        }

        viewModel.isLoginSuccess.observe(this){
            activity?.runOnUiThread{
                if (it){
                    Log.d("isLoginSuccess","true")
                    if (loginDialog?.isShowing){
                        loginDialog?.dismiss()
                    }
                    (activity?.application as WawaApp).refreshApolloClient()
                    (activity as MainActivity).viewModel.isUserLogined.postValue(true)
                }
            }
        }

        //判断是不是已经同意过用户协议
        app_chanel = AppUtils.getMetaData(activity, "APP_CHANNEL")
        if (app_chanel == null){
            return
        }
        if (app_chanel!!.contains("qq")  || app_chanel!!.contains("huawei") || app_chanel!!.contains(
                "wandoujia"
            )
        ) {
            if (!SharePreferenceUtils.getSwitch(SharePreferenceUtils.LOGIN_AGREEMENT)) {
                val spStr = SpannableString(
                    """
                        请你务必谨慎阅读，充分理解“用户协议”和“隐私策略”各条款，包括但不限于：为了更好地使用登录、内容分享等服务，我们需要收集你的设备信息，操作日志等个人信息。你可以在“个人中心”中查看。
                        你可阅读《用户协议》和《隐私策略》了解详细信息。如你同意，请点击“同意”开始接受我们的服务。
                        """.trimIndent()
                )
                val userXY: ClickableSpan =
                    object : ClickableSpan() {
                        override fun onClick(view: View) {
//                        L.d("UserAgreementClick","6666");
//                            val pageOptions: ConfigBean.PageOption =
//                                App.getInstance().getConfigBean().getPageOptions()
                            if (app_chanel!!.contains("vivo") || TextUtils.isEmpty(MainViewModule.configData?.page()?.fragments()?.pageOptionFragment()?.userAgreementUrl())) {
                                val intent = Intent(
                                    activity,
                                    LongTextActivity::class.java
                                )
                                intent.putExtra(
                                    LongTextActivity.INTENT_TITLE,
                                    getString(R.string.user_service_terms)
                                )
                                startActivity(intent)
                                return
                            }
                            goAgreeMentWebPage()
                        }

                        override fun updateDrawState(ds: TextPaint) {
                            ds.color = Color.BLUE
                            ds.isUnderlineText = false
                        }
                    }
                spStr.setSpan(userXY, 97, 110, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                loginAgreementDialog = DialogUitl.confirmDialog(
                    activity,
                    "用户协议和隐私政策",
                    spStr,
                    "同意",
                    "暂不使用",
                    false,
                    object : DialogUitl.Callback {
                       override fun confirm(dialog: Dialog) {
                            SharePreferenceUtils.saveSwitch(SharePreferenceUtils.LOGIN_AGREEMENT,true)
                            binding.checkboxAgreement.setChecked(true)
                            dialog.dismiss()
                        }

                        override fun cancel(dialog: Dialog) {
                            activity?.finish()
                            dialog.dismiss()
                        }
                    })
                loginAgreementDialog!!.show()
            }
        }
    }

    fun goAgreeMentWebPage(){
        activity?.let {
            Log.d("goAgreeMentWebPage",MainViewModule.configData?.page()?.fragments()?.pageOptionFragment()?.userAgreementUrl().toString())
            var intent=Intent()
            intent.setClass(it,WebActivity::class.java)
            intent.putExtra(WebActivity.WEB_TITLE,getString(R.string.user_service_terms))
            intent.putExtra(WebActivity.WEB_URL,MainViewModule.configData?.page()?.fragments()?.pageOptionFragment()?.userAgreementUrl())
            startActivity(intent)
        }
    }

    private inner class LoginBroarcastReceiver : BroadcastReceiver() {
        override fun onReceive(
            context: Context,
            intent: Intent
        ) {
            val action = intent.action
            if (!TextUtils.isEmpty(action) && action.equals(WXLOGIN_ACTION) ) {
                val wxCode: String? = intent.getStringExtra("wxCode")
                //微信登陆
                wxCode?.let {
                    activity?.let {
                        LoadingDialogManager.loadBigDialog(it,getString(R.string.login_ing))
                    }
                    viewModel.wxLogin(wxCode)
                }
                /*if (!TextUtils.isEmpty(wxCode)) {
                    viewModel.wxLogin(wxCode?:"")
                } else {
                    Toast.makeText(activity,"wechat auth failed",Toast.LENGTH_SHORT).show()
                }*/
            }
        }
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): LoginViewModel {
        val loginViewModel: LoginViewModel by viewModels()
        return loginViewModel
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.unregisterReceiver(loginReceiver)
    }
}