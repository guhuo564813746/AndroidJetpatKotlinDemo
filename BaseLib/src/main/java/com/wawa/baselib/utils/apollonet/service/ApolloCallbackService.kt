package com.wawa.baselib.utils.apollonet.service

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.BannerListQuery
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

    override fun getChargeOrderList(orderId: Int?) {
        TODO("Not yet implemented")
    }

    override fun getChargeItemList() {
        TODO("Not yet implemented")
    }

    override fun getGameRecordList() {
        TODO("Not yet implemented")
    }

    override fun getOrderList(orderId: Int?) {
        TODO("Not yet implemented")
    }

    override fun getRoomCategoryList() {
        TODO("Not yet implemented")
    }

    override fun getRoomList(categoryId: Int?, roomId: Int?) {
        TODO("Not yet implemented")
    }

    override fun getUserCoinLogList() {
        TODO("Not yet implemented")
    }

    override fun getUserPointLogList() {
        TODO("Not yet implemented")
    }

    override fun getUserData() {
        TODO("Not yet implemented")
    }

}