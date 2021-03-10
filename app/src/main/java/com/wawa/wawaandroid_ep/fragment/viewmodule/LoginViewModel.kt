package com.wawa.wawaandroid_ep.fragment.viewmodule

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.UserLoginByPhoneMutation
import com.apollographql.apollo.UserLoginByWechatMutation
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.wawa.baselib.utils.SharePreferenceUtils
import com.wawa.wawaandroid_ep.WawaApp
import com.wawa.wawaandroid_ep.base.viewmodel.BaseVM
import com.wawa.wawaandroid_ep.httpcore.bean.ForecastsBean

/**
 *作者：create by 张金 on 2021/1/20 10:21
 *邮箱：564813746@qq.com
 */
class LoginViewModel : BaseVM(){

    val loginData=MutableLiveData<ForecastsBean>()
    val isLoginSuccess=MutableLiveData<Boolean>()
    val isL=ObservableBoolean(false)
    fun phoneLogin(phoneNum: String,code: String){
        val userLoginByPhone=UserLoginByPhoneMutation(phoneNum,"Panda_Game")
        WawaApp.apolloClient
            .mutate(userLoginByPhone)
            .enqueue(object: ApolloCall.Callback<UserLoginByPhoneMutation.Data>(){
                override fun onFailure(e: ApolloException) {
                    Log.d("phoneLogin",e.message.toString())
                }

                override fun onResponse(response: Response<UserLoginByPhoneMutation.Data>) {
                    Log.d("phoneLogin",response?.toString())
                    val token=response.data?.userLoginByPhone()?.accessToken().toString()
                    val uid=response.data?.userLoginByPhone()?.userId().toString()
                    if (!token.isNullOrEmpty() && !uid.isNullOrEmpty()){
                        Log.d("tokenandUidIsBlank","true")
                        SharePreferenceUtils.saveToken(token)
                        SharePreferenceUtils.saveUid(uid)
                        isLoginSuccess.postValue(true)
                        isL.set(true)
                    }
                }
            })
    }
    fun wxLogin(code: String){
        val userLoginByWechat=UserLoginByWechatMutation(code)
        WawaApp.apolloClient
            .mutate(userLoginByWechat)
            .enqueue(object: ApolloCall.Callback<UserLoginByWechatMutation.Data>(){

                override fun onFailure(e: ApolloException) {
                    Log.d("wxLogintest",e.message.toString())
                }

                override fun onResponse(response: Response<UserLoginByWechatMutation.Data>) {
                    Log.d("wxLogintest",response.toString())
                    val token=response?.data?.userLoginByWechat()?.accessToken()
                    val uid=response?.data?.userLoginByWechat()?.userId()
                    token?.let {
                        SharePreferenceUtils.saveToken(it)
                    }
                    uid?.let {
                        SharePreferenceUtils.saveUid(it.toString())
                    }
                    if (!token.isNullOrEmpty() && !uid.toString().isNullOrEmpty()){
                        isLoginSuccess.postValue(true)
                    }
                }
            })
    }



}