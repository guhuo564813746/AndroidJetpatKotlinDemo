package com.wawa.wawaandroid_ep.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.activity.viewModels
import com.robotwar.app.BR
import com.robotwar.app.R
import com.robotwar.app.databinding.LauncherActivityLayBinding
import com.wawa.baselib.utils.SharePreferenceUtils
import com.wawa.wawaandroid_ep.MainActivity
import com.wawa.wawaandroid_ep.activity.viewmodule.LauncherActivityVm
import com.wawa.wawaandroid_ep.base.activity.BaseActivity

/*
 *作者：create by 张金 on 2021/1/14 14:25
 *邮箱：564813746@qq.com
 */



class LauncherActivity : BaseActivity<LauncherActivityLayBinding,LauncherActivityVm>(){

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.launcher_activity_lay
    }

    override fun initView() {
        if (!TextUtils.isEmpty(SharePreferenceUtils.readUid()) && !TextUtils.isEmpty(
                SharePreferenceUtils.readToken())){
            val mainIntent=Intent()
            mainIntent.setClass(this,MainActivity::class.java)
            startActivity(mainIntent)
            finish()
        }else{
            val loginIntent=Intent(this,LoginActivity::class.java)
            startActivity(loginIntent)
            finish()
        }
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): LauncherActivityVm {
        val launcherActivityVm: LauncherActivityVm by viewModels()
        return launcherActivityVm
    }

    override fun back(view: View) {

    }


}
