package com.wawa.wawaandroid_ep

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ConfigDataQuery
import com.apollographql.apollo.UserQuery
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.wawa.baselib.utils.net.datasource.GraphqlRemoteDataSource
import com.wawa.wawaandroid_ep.base.viewmodel.BaseVM

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
    }
    val isShowBottom=MutableLiveData<Boolean>()
    val isUserLogined=MutableLiveData<Boolean>()
    override fun onCleared() {
        super.onCleared()

    }

}