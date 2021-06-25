package com.wawa.baselib.utils.mvvm.binding.viewadapter.view

import android.view.View
import androidx.databinding.BindingAdapter
import java.util.concurrent.TimeUnit

/**
 *作者：create by 张金 on 2021/6/24 18:06
 *邮箱：564813746@qq.com
 */
class ViewAdapter {
    //防重复点击间隔(秒)
    val CLICK_INTERVAL = 1

    /**
     * requireAll 是意思是是否需要绑定全部参数, false为否
     * View的onClick事件绑定
     * onClickCommand 绑定的命令,
     * isThrottleFirst 是否开启防止过快点击
     */
    @BindingAdapter(value = ["onClickCommand", "isThrottleFirst"], requireAll = false)
    fun onClickCommand(
        view: View?,
        isThrottleFirst: Boolean
    ) {

    }

}