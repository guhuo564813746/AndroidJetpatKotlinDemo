package com.wawa.wawaandroid_ep

import androidx.lifecycle.MutableLiveData
import com.apollographql.apollo.ConfigDataQuery
import com.apollographql.apollo.UserQuery
import com.wawa.baselib.utils.viewmodel.BaseVM

/**
 *作者：create by 张金 on 2021/1/14 16:43
 *邮箱：564813746@qq.com
 */
class MainViewModule : BaseVM(){
    companion object{
        val TAG="MainViewModule"
        var mutableLiveuserData=MutableLiveData<UserQuery.User>()
        var userData: UserQuery.User?= null
        var configData: ConfigDataQuery.Config?= null
        var configMutableLiveData=MutableLiveData<ConfigDataQuery.Config>()

    }
    val isShowBottom=MutableLiveData<Boolean>()
    val isUserLogined=MutableLiveData<Boolean>()
    override fun onCleared() {
        super.onCleared()
    }

}