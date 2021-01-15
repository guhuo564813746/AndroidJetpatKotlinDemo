package com.wawa.baselib.utils.net.viewmodule

import androidx.lifecycle.MutableLiveData
import com.wawa.baselib.utils.net.coroutine.CoroutineEvent
import kotlinx.coroutines.Job

/**
 *作者：create by 张金 on 2021/1/15 16:23
 *邮箱：564813746@qq.com
 * @Desc: 用于定义 View 和  ViewModel 均需要实现的一些 UI 层行为
 */


interface IUIActionEvent : CoroutineEvent{
    fun showLoading(job: Job?)
    fun dismissLoading()
    fun showToast(msg: String)
    fun finishView()
}

interface IViewModuleActionEvent : IUIActionEvent{
    val showLoadingEventLD: MutableLiveData<ShowLoadingEvent>
    val dismissLoadingEventLD: MutableLiveData<DismissLoadingEvent>
    val showToastEventLD: MutableLiveData<ShowToastEvent>
    val finishViewEventLD: MutableLiveData<FinishViewEvent>

    override fun showLoading(job: Job?) {
        showLoadingEventLD.value= ShowLoadingEvent(job)
    }

    override fun dismissLoading() {
        dismissLoadingEventLD.value=DismissLoadingEvent
    }

    override fun showToast(msg: String) {
        showToastEventLD.value= ShowToastEvent(msg)
    }

    override fun finishView() {
        finishViewEventLD.value=FinishViewEvent
    }
}