package com.wawa.baselib.utils.net.viewmodule

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wawa.baselib.utils.net.datasource.RemoteDataSource
import kotlinx.coroutines.CoroutineScope

/**
 *作者：create by 张金 on 2021/1/15 17:36
 *邮箱：564813746@qq.com
 * @Desc: ViewModel 基类
 */
open class BaseViewModule : ViewModel(), IViewModuleActionEvent{
    override val showLoadingEventLD = MutableLiveData<ShowLoadingEvent>()

    override val dismissLoadingEventLD = MutableLiveData<DismissLoadingEvent>()

    override val showToastEventLD = MutableLiveData<ShowToastEvent>()

    override val finishViewEventLD = MutableLiveData<FinishViewEvent>()

    override val lifecycleSuportScope: CoroutineScope
        get() = viewModelScope

}

open class BaseAndroidViewModel(application: Application) : AndroidViewModel(application),IViewModuleActionEvent{
    override val showLoadingEventLD = MutableLiveData<ShowLoadingEvent>()

    override val dismissLoadingEventLD = MutableLiveData<DismissLoadingEvent>()

    override val showToastEventLD = MutableLiveData<ShowToastEvent>()

    override val finishViewEventLD = MutableLiveData<FinishViewEvent>()

    override val lifecycleSuportScope: CoroutineScope
        get() = viewModelScope

}