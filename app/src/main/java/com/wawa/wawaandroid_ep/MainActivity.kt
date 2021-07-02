package com.wawa.wawaandroid_ep

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.TextUtils
import android.text.style.ClickableSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.view.LayoutInflaterCompat
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.apollographql.apollo.ConfigDataQuery
import com.apollographql.apollo.UserQuery
import com.blankj.utilcode.util.ToastUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.robotwar.app.BR
import com.robotwar.app.R
import com.robotwar.app.databinding.ActivityMainBinding
import com.wawa.baselib.utils.SharePreferenceUtils
import com.wawa.baselib.utils.apollonet.BaseDataSource
import com.wawa.baselib.utils.logutils.LogUtils
import com.wawa.wawaandroid_ep.activity.LoginActivity
import com.wawa.wawaandroid_ep.activity.LongTextActivity
import com.wawa.wawaandroid_ep.base.activity.BaseActivity
import com.wawa.wawaandroid_ep.utils.DialogUitl
import com.wawa.wawaandroid_ep.utils.GoPageUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainActivity : BaseActivity<ActivityMainBinding,MainViewModule>() {
    lateinit var navBottom: BottomNavigationView
    lateinit var  navControlor: NavController
    private var loginAgreementDialog: Dialog? = null
    private val compositeDisposable = CompositeDisposable()
    val dataSource: BaseDataSource by lazy {
        (application as WawaApp).getDataSource(WawaApp.ServiceTypes.COROUTINES)
    }
    companion object{
        private val TAG: String="MainActivity"
    }
    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        window.setBackgroundDrawableResource(R.color.white)
        val navHostFragment=supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navControlor=navHostFragment.navController
        MainViewModule.configMutableLiveData.observe(this,Observer{
            MainViewModule.configData=it
        })
        viewModel.isUserLogined.observe(this, Observer {
            Log.d(TAG,"isUserLogined"+it.toString())
            if (it){
//                GraphqlRemoteDataSource().initTokenAndUid(Utils.readToken(),Utils.readUid())
//                binding.viewMainBg.visibility=View.GONE
                navControlor.navigate(R.id.mainFragment)
            }else{
                SharePreferenceUtils.saveToken("")
                SharePreferenceUtils.saveUid("")
                //跳转登陆逻辑
//                navControlor.navigate(R.id.loginFragment)
                val intent =Intent()
                intent.setClass(this,LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        })
        if (!TextUtils.isEmpty(SharePreferenceUtils.readUid()) && !TextUtils.isEmpty(SharePreferenceUtils.readToken())){
            viewModel.isShowBottom.postValue(true)
            viewModel.isUserLogined.postValue(true)
        }else{
            viewModel.isShowBottom.postValue(false)
            viewModel.isUserLogined.postValue(false)
        }
        navBottom=binding.navMainBottom
        navBottom.itemIconTintList=null
        navBottom.setupWithNavController(navControlor)
        navControlor.addOnDestinationChangedListener{
            controller, destination, arguments ->
            Log.d(TAG,"addOnDestinationChangedListener--")
            when(destination.id){
                R.id.mainFragment -> Log.d(TAG,"main")
                R.id.chargeFragment -> Log.d(TAG,"charge")
                R.id.mineFragment -> Log.d(TAG,"mine")
            }
        }
        viewModel.isShowBottom.observe(this, Observer {
            if (it){
                navBottom.visibility=View.VISIBLE
            }else{
                navBottom.visibility=View.GONE
            }
        })
        initConfigData()
    }

    fun initConfigData(){
        val successConfigDataDp=dataSource.configData
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleSuccessConfigData)
        val errorConfigDataDp=dataSource.error
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleErrorData)
        compositeDisposable.add(successConfigDataDp)
        compositeDisposable.add(errorConfigDataDp)
        dataSource.getConfigData()
    }


    fun handleSuccessConfigData(data: ConfigDataQuery.Config){
        Log.d("handleSuccessConfigData",data?.toString())
        data?.let {
            MainViewModule.configMutableLiveData.value=data
        }
    }

     fun setUpDataSource(){
         LogUtils.d(TAG,"setUpDataSource--")
        val successUserDisposable=dataSource.userData
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleSuccessUserData)

        val errorUserDisposable=dataSource.error
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleErrorData)
        compositeDisposable.add(successUserDisposable)
        compositeDisposable.add(errorUserDisposable)
        dataSource.getUserData()
    }

    private fun handleSuccessUserData(userData: UserQuery.User){
        LogUtils.d(TAG,"handleSuccessUserData--${userData.toString()}")
        MainViewModule.mutableLiveuserData.value=userData
        MainViewModule.userData=userData
    }

    private fun handleErrorData(error: Throwable?){
        Log.d(TAG,"handleErrorData--")
        runOnUiThread {
            ToastUtils.showShort(error?.message)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()

    }

    override fun initVariableId(): Int {
       return BR.viewModel
    }

    override fun initViewModel(): MainViewModule {
        val mainViewModel: MainViewModule by viewModels()
        return mainViewModel
    }

    fun showUserAgreementDialog(){
        if (!SharePreferenceUtils.getSwitch(SharePreferenceUtils.LOGIN_AGREEMENT)) {
            val agreementStr="请你务必谨慎阅读，充分理解“用户协议”和“隐私策略”各条款，包括但不限于：为了更好地使用登录、内容分享等服务，我们需要收集你的设备信息，操作日志等个人信息。你可以在“个人中心”中查看。\n" +
                    "你可阅读《用户协议》和《隐私策略》了解详细信息。如你同意，请点击“同意”开始接受我们的服务。"
            val agreementSps= SpannableString(agreementStr)
            val userAgreementStart=agreementStr.indexOf("《")
            /* val spStr = SpannableString(
                 """
                         请你务必谨慎阅读，充分理解“用户协议”和“隐私策略”各条款，包括但不限于：为了更好地使用登录、内容分享等服务，我们需要收集你的设备信息，操作日志等个人信息。你可以在“个人中心”中查看。
                         你可阅读《用户协议》和《隐私策略》了解详细信息。如你同意，请点击“同意”开始接受我们的服务。
                         """.trimIndent()
             )*/
            val userXY: ClickableSpan =
                object : ClickableSpan() {
                    override fun onClick(view: View) {
//                        L.d("UserAgreementClick","6666");
//                            val pageOptions: ConfigBean.PageOption =
//                                App.getInstance().getConfigBean().getPageOptions()
                        goAgreeMentNativePage(LongTextActivity.TYPE_USER_AGREEMENT)
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        ds.color = Color.BLUE
                        ds.isUnderlineText = false
                    }
                }

            agreementSps.setSpan(userXY, userAgreementStart, userAgreementStart+6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            val privacyStart=agreementStr.lastIndexOf("《")
            val privacyXY: ClickableSpan =
                object : ClickableSpan() {
                    override fun onClick(view: View) {
                        goAgreeMentNativePage(LongTextActivity.TYPE_PRIVACY_POLICY)
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        ds.color = Color.BLUE
                        ds.isUnderlineText = false
                    }
                }
            agreementSps.setSpan(privacyXY, privacyStart, privacyStart+6, 0)
            loginAgreementDialog = DialogUitl.confirmDialog(
                this,
                "用户协议和隐私政策",
                agreementSps,
                "同意",
                "暂不使用",
                false,
                object : DialogUitl.Callback {
                    override fun confirm(dialog: Dialog) {
                        SharePreferenceUtils.saveSwitch(SharePreferenceUtils.LOGIN_AGREEMENT,true)
                        dialog.dismiss()
                    }

                    override fun cancel(dialog: Dialog) {
                        finish()
                        dialog.dismiss()
                    }
                })
            loginAgreementDialog!!.show()
        }
    }

    fun goAgreeMentNativePage(type: String){
        val intent = Intent(
            this,
            LongTextActivity::class.java
        )
        intent.putExtra(
            LongTextActivity.INTENT_TITLE,
            getString(R.string.user_service_terms)
        )
        intent.putExtra(LongTextActivity.INTENT_CONTENT,type)
        startActivity(intent)
    }

    fun goPage(v: View){
        when(v.id){
            R.id.ll_users,R.id.rl_Head ->{
                GoPageUtils.goPage(this,"profile","","")
            }
        }
    }

}