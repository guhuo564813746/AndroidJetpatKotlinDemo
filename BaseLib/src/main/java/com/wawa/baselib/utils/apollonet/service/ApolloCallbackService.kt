package com.wawa.baselib.utils.apollonet.service

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.BannerListQuery
import com.apollographql.apollo.UserQuery
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.wawa.baselib.utils.apollonet.BaseDataSource

/**
 *
 * @ProjectName:    WawaAndroid_EP
 * @Package:        com.wawa.baselib.utils.apollonet.service
 * @ClassName:      ApolloCallbackService
 * @Description:     java类作用描述
 * @Author:         zhangjin
 * @CreateDate:     2021-01-31 11:45
 * @UpdateDate:     2021-01-31 11:45
 * @Version:        1.0
 */
class ApolloCallbackService(apolloClient: ApolloClient) : BaseDataSource(apolloClient){
    override fun getBannerList(categoryId: Int) {
        val bannerListQuery=BannerListQuery(categoryId)
        val callback=object:  ApolloCall.Callback<BannerListQuery.Data>(){
            override fun onFailure(e: ApolloException) {
                exceptionSubject.onNext(e)
            }

            override fun onResponse(response: Response<BannerListQuery.Data>) {
                val bannerList=response?.data?.bannerList()?.filterNotNull().orEmpty()
                bannerListSubject.onNext(bannerList)
            }
        }

        apolloClient.query(bannerListQuery).enqueue(callback)
    }

    override fun getChargeOrderList(index: Int) {

    }

    override fun getChargeItemList() {

    }

    override fun getGameRecordList(index: Int) {

    }

    override fun getOrderList(index: Int) {

    }

    override fun getRoomCategoryList() {

    }

    override fun getRoomList(categoryId: Int,index: Int) {

    }

    override fun getUserCoinLogList(index: Int) {

    }

    override fun getUserPointLogList(index: Int) {

    }

    override fun getUserData() {
        val userQuery=UserQuery()
        val callback=object:  ApolloCall.Callback<UserQuery.Data>(){
            override fun onFailure(e: ApolloException) {
                exceptionSubject.onNext(e)
            }

            override fun onResponse(response: Response<UserQuery.Data>) {
                val userData=response?.data?.user()
                if (userData != null) {
                    userDataSubject.onNext(userData)
                }
            }
        }
    }

    override fun getRoomInfoData(roodId: Int) {

    }

    override fun getConfigData() {
        TODO("Not yet implemented")
    }

}