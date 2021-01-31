package com.wawa.wawaandroid_ep

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.UserQuery
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.wawa.baselib.utils.net.datasource.GraphqlRemoteDataSource

/**
 *作者：create by 张金 on 2021/1/14 16:43
 *邮箱：564813746@qq.com
 */
class MainViewModule : ViewModel(){
    companion object{
        val TAG="MainViewModule"
         var userData=MutableLiveData<UserQuery.Data>()
    }
    val isShowBottom=MutableLiveData<Boolean>()
    val isUserLogined=MutableLiveData<Boolean>()
    override fun onCleared() {
        super.onCleared()

    }

    fun getUserData(){
        val userDataQuery= UserQuery()
        WawaApp.apolloClient
            .query(userDataQuery)
            .enqueue(object : ApolloCall.Callback<UserQuery.Data>(){
                override fun onFailure(e: ApolloException) {
                    Log.d(TAG,e.message.toString())
                }

                override fun onResponse(response: Response<UserQuery.Data>) {
                    Log.d(TAG,"getUserData--"+response?.data()?.user()?.nickName()
                        +response?.data()?.user()?.phoneNo()+response?.data()?.user()?.userId()
                    )
                    userData?.value=response?.data()
                }
            })

    }
}