package com.wawa.wawaandroid_ep.fragment.viewmodule

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ChargeItemListQuery
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.wawa.baselib.utils.net.datasource.GraphqlRemoteDataSource

/**
 *作者：create by 张金 on 2021/1/25 11:10
 *邮箱：564813746@qq.com
 */
class ChargeFragmentViewModel : ViewModel(){
    var coin=ObservableField("0")
    var diamond = ObservableField("0")
    private val chargeList: MutableLiveData<List<ChargeItemListQuery.ChargeItemList>> by lazy {
        MutableLiveData<List<ChargeItemListQuery.ChargeItemList>>().also {
            loadChargeList()
        }
    }

    fun getChargeList(): LiveData<List<ChargeItemListQuery.ChargeItemList>> {
        return chargeList
    }

    public fun loadChargeList(){
        val chargeItemListQuery=ChargeItemListQuery()
        GraphqlRemoteDataSource().apolloClient()
            .query(chargeItemListQuery)
            .enqueue(object: ApolloCall.Callback<ChargeItemListQuery.Data>(){
                override fun onFailure(e: ApolloException) {
                    e.message?.let { Log.d("loadChargeList--onFail", it) }
                }

                override fun onResponse(response: Response<ChargeItemListQuery.Data>) {
                    response?.let {
                        Log.d("loadChargeList--success",response.data().toString())
//                        chargeList=response
                    }
                }
            })

    }
}