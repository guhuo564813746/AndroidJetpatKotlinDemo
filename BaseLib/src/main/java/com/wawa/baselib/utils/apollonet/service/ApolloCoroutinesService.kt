package com.wawa.baselib.utils.apollonet.service

import com.apollographql.apollo.ApolloClient
import com.wawa.baselib.utils.apollonet.BaseDataSource
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**
 *
 * @ProjectName:    WawaAndroid_EP
 * @Package:        com.wawa.baselib.utils.apollonet.service
 * @ClassName:      ApolloCoroutinesService
 * @Description:     java类作用描述
 * @Author:         zhangjin
 * @CreateDate:     2021-01-31 12:08
 * @UpdateDate:     2021-01-31 12:08
 * @Version:        1.0
 */
class ApolloCoroutinesService(apolloClient: ApolloClient,
                              private val processContext: CoroutineContext = Dispatchers.IO,
                              private val resultContext: CoroutineContext = Dispatchers.Main) : BaseDataSource(apolloClient) {
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