package com.wawa.baselib.utils.apollonet.service

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.BannerListQuery
import com.apollographql.apollo.rx2.rxQuery
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
        val bannerListQuery: BannerListQuery=BannerListQuery(categoryId)
        val disposable=apolloClient.rxQuery(bannerListQuery)
            .subscribeOn(processScheduler)
            .observeOn(resultScheduler)
            .map { response ->
                response?.data?.bannerList()?.filterNotNull().orEmpty()
            }
            .subscribe(
                bannerListSubject::onNext,
                exceptionSubject::onNext
            )
        compositeDisposable.add(disposable)
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
        TODO("Not yet implemented")
    }

}