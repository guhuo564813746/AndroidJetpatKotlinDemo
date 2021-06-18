package com.wawa.wawaandroid_ep.fragment.viewmodule

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.UserLoginByPhoneMutation
import com.apollographql.apollo.UserLoginByWechatMutation
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.blankj.utilcode.util.ToastUtils
import com.wawa.baselib.utils.SharePreferenceUtils
import com.wawa.baselib.utils.apollonet.MutationCallback
import com.wawa.wawaandroid_ep.WawaApp
import com.wawa.wawaandroid_ep.base.viewmodel.BaseVM
import com.wawa.wawaandroid_ep.fragment.LoginFragment
import com.wawa.wawaandroid_ep.httpcore.bean.ForecastsBean

/**
 *作者：create by 张金 on 2021/1/20 10:21
 *邮箱：564813746@qq.com
 */
class LoginViewModel : BaseVM(){

    val loginData=MutableLiveData<ForecastsBean>()
    val isLoginSuccess=MutableLiveData<Boolean>()

    fun phoneLogin(phoneNum: String,code: String){
        val userLoginByPhone=UserLoginByPhoneMutation(phoneNum,code)
        WawaApp.apolloClient
            .mutate(userLoginByPhone)
            .enqueue(object: ApolloCall.Callback<UserLoginByPhoneMutation.Data>(){
                override fun onFailure(e: ApolloException) {
                    Log.d("phoneLogin",e.message.toString())
                }

                override fun onResponse(response: Response<UserLoginByPhoneMutation.Data>) {
                    Log.d("phoneLogin",response?.toString())
                    val token=response.data?.userLoginByPhone()?.accessToken()
                    val uid=response.data?.userLoginByPhone()?.userId()
                    token?.let {
                        SharePreferenceUtils.saveToken(it)
                        uid?.let {
                            SharePreferenceUtils.saveUid(it.toString())
                            isLoginSuccess.postValue(true)
                        }
                    }
                    response?.errors?.let {
                        if (it.size >0 ){
                            SharePreferenceUtils.saveUid("")
                            SharePreferenceUtils.saveToken("")
                            ToastUtils.showShort(it.get(0)?.message)
                        }
                    }
                }
            })
    }
    fun  wxLogin(code: String){
        Log.d("wxLogin--",code)
        val userLoginByWechat=UserLoginByWechatMutation(code)
        WawaApp.apolloClient
            .mutate(userLoginByWechat)
            .enqueue(object: MutationCallback<UserLoginByWechatMutation.Data>(){

                override fun onFailure(e: ApolloException) {
                    super.onFailure(e)
                    Log.d("wxLogintest",e.message.toString())

                }

                override fun onResponse(response: Response<UserLoginByWechatMutation.Data>) {
                    super.onResponse(response)
                    val token=response?.data?.userLoginByWechat()?.accessToken()
                    val uid=response?.data?.userLoginByWechat()?.userId()
                    token?.let {
                        SharePreferenceUtils.saveToken(it)
                    }
                    uid?.let {
                        SharePreferenceUtils.saveUid(it.toString())
                        isLoginSuccess.postValue(true)
                    }
                    token.isNullOrEmpty()
                    response?.errors?.size?.let {
                        if (it > 0){
                            SharePreferenceUtils.saveUid("")
                            SharePreferenceUtils.saveToken("")
                            ToastUtils.showShort(response?.errors?.get(0)?.message)
                        }
                    }
                }
            })
    }



}