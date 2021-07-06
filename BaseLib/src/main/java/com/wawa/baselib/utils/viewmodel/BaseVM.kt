package com.wawa.baselib.utils.viewmodel

import android.view.View
import androidx.lifecycle.ViewModel
import com.wawa.baselib.utils.mvvm.binding.event.SingleLiveEvent

/**
 *作者：create by 张金 on 2021/3/2 10:18
 *邮箱：564813746@qq.com
 */
abstract class BaseVM : ViewModel(){
    var clicks=SingleLiveEvent<View>()
    fun onClick(view: View){
        clicks.value=view
    }

}