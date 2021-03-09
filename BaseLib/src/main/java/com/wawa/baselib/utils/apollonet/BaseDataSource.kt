package com.wawa.baselib.utils.apollonet

import com.apollographql.apollo.*
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 *
 * @ProjectName:    WawaAndroid_EP
 * @Package:        com.wawa.baselib.utils.apollonet
 * @ClassName:      BaseDataSource
 * @Description:     java类作用描述
 * @Author:         zhangjin
 * @CreateDate:     2021-01-31 11:43
 * @UpdateDate:     2021-01-31 11:43
 * @Version:        1.0
 */
abstract class BaseDataSource(protected val apolloClient: ApolloClient) {
    protected val exceptionSubject: PublishSubject<Throwable> = PublishSubject.create()
    val error: Observable<Throwable> = exceptionSubject.hide()

    protected val bannerListSubject: PublishSubject<List<BannerListQuery.BannerList>> = PublishSubject.create()
    protected val chargeOrderListSubject: PublishSubject<List<ChargeOrderListQuery.List>> = PublishSubject.create()
    protected val chargeItemListSubject: PublishSubject<List<ChargeItemListQuery.ChargeItemList>> = PublishSubject.create()
    protected val gameRecordListSubject: PublishSubject<List<GameRecordListQuery.List>> = PublishSubject.create()
    protected val orderListSubject: PublishSubject<List<OrderListQuery.List>> = PublishSubject.create()
    protected val roomCategoryListSubject: PublishSubject<List<RoomCategoryListQuery.RoomCategoryList>> = PublishSubject.create()
    protected val roomListSubject: PublishSubject<List<RoomListQuery.List>> = PublishSubject.create()
    protected val userCoinLogListSubject: PublishSubject<List<UserCoinLogListQuery.List>> = PublishSubject.create()
    protected val userPointLogListSubject: PublishSubject<List<UserPointLogListQuery.List>> = PublishSubject.create()
    protected val userDataSubject: PublishSubject<UserQuery.User> = PublishSubject.create()
    protected val roomInfoSubject: PublishSubject<List<RoomInfoQuery.List>> = PublishSubject.create()
    protected val configDataSubject: PublishSubject<ConfigDataQuery.Config> = PublishSubject.create()

    val bannerList: Observable<List<BannerListQuery.BannerList>> =bannerListSubject.hide()
    val chargeOrderList: Observable<List<ChargeOrderListQuery.List>> = chargeOrderListSubject.hide()
    val chargeItemList: Observable<List<ChargeItemListQuery.ChargeItemList>> = chargeItemListSubject.hide()
    val gameRecordList: Observable<List<GameRecordListQuery.List>> = gameRecordListSubject.hide()
    val orderList: Observable<List<OrderListQuery.List>> = orderListSubject.hide()
    val roomCategoryList: Observable<List<RoomCategoryListQuery.RoomCategoryList>> =roomCategoryListSubject.hide()
    val roomList: Observable<List<RoomListQuery.List>> = roomListSubject.hide()
    val userCoinLogList: Observable<List<UserCoinLogListQuery.List>> =userCoinLogListSubject.hide()
    val userPointLogList: Observable<List<UserPointLogListQuery.List>> =userPointLogListSubject.hide()
    val userData: Observable<UserQuery.User> = userDataSubject.hide()
    val roomInfo: Observable<List<RoomInfoQuery.List>> = roomInfoSubject.hide()
    val configData: Observable<ConfigDataQuery.Config> =configDataSubject.hide()

    abstract fun  getBannerList(categoryId: Int)
    abstract fun getChargeOrderList(index: Int)
    abstract fun getChargeItemList()
    abstract fun getGameRecordList(index: Int)
    abstract fun getOrderList(index: Int)
    abstract fun getRoomCategoryList()
    abstract fun getRoomList(categoryId: Int,index: Int)
    abstract fun getUserCoinLogList(index: Int)
    abstract fun getUserPointLogList(index: Int)
    abstract fun getUserData()
    abstract fun getRoomInfoData(roodId: Int)
    abstract fun getConfigData()
}