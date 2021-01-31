package com.wawa.baselib.utils.apollonet.service

import com.apollographql.apollo.ApolloClient
import com.wawa.baselib.utils.apollonet.BaseDataSource
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 *
 * @ProjectName:    WawaAndroid_EP
 * @Package:        com.wawa.baselib.utils.apollonet.service
 * @ClassName:      ApolloRxService
 * @Description:     java类作用描述
 * @Author:         zhangjin
 * @CreateDate:     2021-01-31 12:15
 * @UpdateDate:     2021-01-31 12:15
 * @Version:        1.0
 */
class ApolloRxService(apolloClient: ApolloClient,
                      private val compositeDisposable: CompositeDisposable = CompositeDisposable(),
                      private val processScheduler: Scheduler = Schedulers.io(),
                      private val resultScheduler: Scheduler = AndroidSchedulers.mainThread()) : BaseDataSource(apolloClient){
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