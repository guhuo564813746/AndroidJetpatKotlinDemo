package com.wawa.baselib.utils.apollonet.service

import android.util.Log
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.BannerListQuery
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.fetcher.ApolloResponseFetchers
import com.wawa.baselib.utils.apollonet.BaseDataSource

/**
 *
 * @ProjectName:    WawaAndroid_EP
 * @Package:        com.wawa.baselib.utils.apollonet.service
 * @ClassName:      ApolloWatcherService
 * @Description:     java类作用描述
 * @Author:         zhangjin
 * @CreateDate:     2021-01-31 12:23
 * @UpdateDate:     2021-01-31 12:23
 * @Version:        1.0
 */
class ApolloWatcherService(apolloClient: ApolloClient) : BaseDataSource(apolloClient){
    override fun getBannerList(categoryId: Int) {
        val bannerListQuery=BannerListQuery(categoryId)
        val callback=createCallback<BannerListQuery.Data> {
            bannerListSubject.onNext(it?.data?.bannerList()?.filterNotNull().orEmpty())
        }
        apolloClient.query(bannerListQuery)
            .responseFetcher(ApolloResponseFetchers.NETWORK_ONLY)
            .watcher()
            .enqueueAndWatch(callback)

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

    }

    override fun getRoomInfoData(roodId: Int) {

    }

    override fun getConfigData() {

    }

    private fun <T : Operation.Data> createCallback(onResponse: (response: Response<T>) -> Unit) =
        object : ApolloCall.Callback<T>() {
            override fun onResponse(response: Response<T>) = onResponse(response)

            override fun onFailure(e: ApolloException) {
                exceptionSubject.onNext(e)
            }

            override fun onStatusEvent(event: ApolloCall.StatusEvent) {
                Log.d("ApolloWatcherService", "Apollo Status Event: $event")
            }
        }

}