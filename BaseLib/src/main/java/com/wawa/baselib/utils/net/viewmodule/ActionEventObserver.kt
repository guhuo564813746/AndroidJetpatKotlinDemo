package com.wawa.baselib.utils.net.viewmodule

import android.content.Context
import androidx.lifecycle.*
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

interface IUIActionEventObserver :IUIActionEvent{
    val lContext: Context?
    val lLifecycleOwner: LifecycleOwner

    fun <VM> getViewModel(clazz: Class<VM>,factory: ViewModelProvider.Factory?=null,
                            initializer: (VM.(lifecycleOwner: LifecycleOwner)-> Unit)?= null) : Lazy<VM> where VM : ViewModel,VM : IViewModuleActionEvent{
        return lazy {
            getViewModelFast(clazz,factory,initializer)
        }
    }

    fun <VM> getViewModelFast(clazz: Class<VM>, factory: ViewModelProvider.Factory? =null,
                            initializer: (VM.(lifecycleOwner: LifecycleOwner)-> Unit)?= null) :VM where VM :ViewModel, VM : IViewModuleActionEvent{
        return when(val localValue=lLifecycleOwner){
            is ViewModelStoreOwner -> {
                if (factory ==null){
                    ViewModelProvider(localValue).get(clazz)
                } else{
                    ViewModelProvider(localValue,factory).get(clazz)
                }
            }
            else -> {
                factory?.create(clazz) ?: clazz.newInstance()
            }

        }.apply {
            genarateActionEvent(this)
            initializer?.invoke(this,lLifecycleOwner)
        }
    }

    fun <VM> genarateActionEvent(viewModel: VM) where VM : ViewModel , VM : IViewModuleActionEvent{
        viewModel.showLoadingEventLD.observe(lLifecycleOwner, Observer {
            this@IUIActionEventObserver.showLoading(it.job)
        })

        viewModel.dismissLoadingEventLD.observe(lLifecycleOwner, Observer {
            this@IUIActionEventObserver.dismissLoading()
        })

        viewModel.showToastEventLD.observe(lLifecycleOwner, Observer {
            if (it.message.isNotBlank()){
                this@IUIActionEventObserver.showToast(it.message)
            }
        })

        viewModel.finishViewEventLD.observe(lLifecycleOwner, Observer {
            this@IUIActionEventObserver.finishView()
        })
    }
}