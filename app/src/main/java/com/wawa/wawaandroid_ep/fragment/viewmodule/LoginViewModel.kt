package com.wawa.wawaandroid_ep.fragment.viewmodule

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.UserLoginByWechatMutation
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.wawa.baselib.utils.net.datasource.GraphqlRemoteDataSource
import com.wawa.baselib.utils.net.viewmodule.BaseViewModule
import com.wawa.wawaandroid_ep.base.viewmodel.BaseViewModel
import com.wawa.wawaandroid_ep.httpcore.bean.ForecastsBean

/**
 *作者：create by 张金 on 2021/1/20 10:21
 *邮箱：564813746@qq.com
 */
class LoginViewModel : BaseViewModel(){

    val loginData=MutableLiveData<ForecastsBean>()
    fun wxLogintest(code: String){
        val userLoginByWechat=UserLoginByWechatMutation(code)
        GraphqlRemoteDataSource.apolloClient
            .mutate(userLoginByWechat)
            .enqueue(object: ApolloCall.Callback<UserLoginByWechatMutation.Data>(){

                override fun onFailure(e: ApolloException) {

                }

                override fun onResponse(response: Response<UserLoginByWechatMutation.Data>) {
                    Log.d("wxLogintest",response.toString())
                }
            })
    }
    fun wxLogin(code: String){
        remoteDataSource.enqueueLoading({
            getWeather(code)
        }){
            onSuccess {

            }
        }
    }
}