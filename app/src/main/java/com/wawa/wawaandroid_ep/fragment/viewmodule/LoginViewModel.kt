package com.wawa.wawaandroid_ep.fragment.viewmodule

import androidx.lifecycle.MutableLiveData
import com.wawa.baselib.utils.net.viewmodule.BaseViewModule
import com.wawa.wawaandroid_ep.base.viewmodel.BaseViewModel
import com.wawa.wawaandroid_ep.httpcore.bean.ForecastsBean

/**
 *作者：create by 张金 on 2021/1/20 10:21
 *邮箱：564813746@qq.com
 */
class LoginViewModel : BaseViewModel(){
    val loginData=MutableLiveData<ForecastsBean>()
    fun wxLogin(code: String){
        remoteDataSource.enqueueLoading({
            getWeather(code)
        }){
            onSuccess {

            }
        }
    }
}