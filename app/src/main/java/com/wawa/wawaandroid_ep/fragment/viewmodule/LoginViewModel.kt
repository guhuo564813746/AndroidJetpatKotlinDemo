package com.wawa.wawaandroid_ep.fragment.viewmodule

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.UserLoginByWechatMutation
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.wawa.baselib.utils.net.datasource.GraphqlRemoteDataSource
import com.wawa.baselib.utils.net.viewmodule.BaseViewModule
import com.wawa.wawaandroid_ep.base.viewmodel.BaseViewModel
import com.wawa.wawaandroid_ep.httpcore.bean.ForecastsBean
import com.wawa.wawaandroid_ep.utils.loginutils.WechatUtils

/**
 *作者：create by 张金 on 2021/1/20 10:21
 *邮箱：564813746@qq.com
 */
class LoginViewModel : ViewModel(){

    val loginData=MutableLiveData<ForecastsBean>()
    fun wxLogin(code: String){
        val userLoginByWechat=UserLoginByWechatMutation(code)
        GraphqlRemoteDataSource.apolloClient
            .mutate(userLoginByWechat)
            .enqueue(object: ApolloCall.Callback<UserLoginByWechatMutation.Data>(){

                override fun onFailure(e: ApolloException) {
                    Log.d("wxLogintest",e.message.toString())
                }

                override fun onResponse(response: Response<UserLoginByWechatMutation.Data>) {
                    Log.d("wxLogintest",response.toString())
                }
            })
    }
}