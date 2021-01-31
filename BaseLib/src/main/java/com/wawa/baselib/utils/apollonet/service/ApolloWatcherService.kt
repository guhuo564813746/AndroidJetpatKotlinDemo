package com.wawa.baselib.utils.apollonet.service

import com.apollographql.apollo.ApolloClient
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
        TODO("Not yet implemented")
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