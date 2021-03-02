/*
package com.wawa.wawaandroid_ep.base.activity

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.wawa.baselib.utils.net.viewmodule.IUIActionEventObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

*/
/**
 *作者：create by 张金 on 2021/1/18 17:27
 *邮箱：564813746@qq.com
 *//*

abstract open class BaseRetrofitActivity<VB : ViewDataBinding> : BaseActivity<VB>(),IUIActionEventObserver{
    protected var loadDialog: ProgressDialog?=null

    override val lifecycleSuportScope: CoroutineScope
        get() = lifecycleScope

    override val lContext: Context?
        get() = this
    override val lLifecycleOwner: LifecycleOwner
        get() = this

    override fun showLoading(job: Job?) {
        dismissLoading()
        loadDialog=ProgressDialog(lContext).apply {
            setCancelable(true)
            setCanceledOnTouchOutside(false)
            //用于实现当弹窗销毁的时候同时取消网络请求
//            setOnDismissListener {
//                job?.cancel()
//            }
            show()
        }
    }

    override fun dismissLoading() {
        loadDialog?.takeIf { it.isShowing }?.dismiss()
        loadDialog=null
    }

    override fun showToast(msg: String) {
        if (msg.isNotBlank()){
            Toast.makeText(this,msg,Toast.LENGTH_SHORT)
        }
    }

    override fun finishView() {
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissLoading()
    }

}*/
