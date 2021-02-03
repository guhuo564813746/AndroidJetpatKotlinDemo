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
    protected val chargeOrderListSubject: PublishSubject<List<ChargeOrderListQuery.ChargeOrderList>> = PublishSubject.create()
    protected val chargeItemListSubject: PublishSubject<List<ChargeItemListQuery.ChargeItemList>> = PublishSubject.create()
    protected val gameRecordListSubject: PublishSubject<List<GameRecordListQuery.GameRecordList>> = PublishSubject.create()
    protected val orderListSubject: PublishSubject<List<OrderListQuery.OrderList>> = PublishSubject.create()
    protected val roomCategoryListSubject: PublishSubject<List<RoomCategoryListQuery.RoomCategoryList>> = PublishSubject.create()
    protected val roomListSubject: PublishSubject<List<RoomListQuery.RoomList>> = PublishSubject.create()
    protected val userCoinLogListSubject: PublishSubject<List<UserCoinLogListQuery.UserCoinLogList>> = PublishSubject.create()
    protected val userPointLogListSubject: PublishSubject<List<UserPointLogListQuery.UserPointLogList>> = PublishSubject.create()
    protected val userDataSubject: PublishSubject<UserQuery.User> = PublishSubject.create()
    protected val roomInfoSubject: PublishSubject<List<RoomInfoQuery.RoomList>> = PublishSubject.create()

    val bannerList: Observable<List<BannerListQuery.BannerList>> =bannerListSubject.hide()
    val chargeOrderList: Observable<List<ChargeOrderListQuery.ChargeOrderList>> = chargeOrderListSubject.hide()
    val chargeItemList: Observable<List<ChargeItemListQuery.ChargeItemList>> = chargeItemListSubject.hide()
    val gameRecordList: Observable<List<GameRecordListQuery.GameRecordList>> = gameRecordListSubject.hide()
    val orderList: Observable<List<OrderListQuery.OrderList>> = orderListSubject.hide()
    val roomCategoryList: Observable<List<RoomCategoryListQuery.RoomCategoryList>> =roomCategoryListSubject.hide()
    val roomList: Observable<List<RoomListQuery.RoomList>> = roomListSubject.hide()
    val userCoinLogList: Observable<List<UserCoinLogListQuery.UserCoinLogList>> =userCoinLogListSubject.hide()
    val userPointLogList: Observable<List<UserPointLogListQuery.UserPointLogList>> =userPointLogListSubject.hide()
    val userData: Observable<UserQuery.User> = userDataSubject.hide()
    val roomInfo: Observable<List<RoomInfoQuery.RoomList>> = roomInfoSubject.hide()

    abstract fun  getBannerList(categoryId: Int)
    abstract fun getChargeOrderList()
    abstract fun getChargeItemList()
    abstract fun getGameRecordList()
    abstract fun getOrderList()
    abstract fun getRoomCategoryList()
    abstract fun getRoomList(categoryId: Int)
    abstract fun getUserCoinLogList()
    abstract fun getUserPointLogList()
    abstract fun getUserData()
    abstract fun getRoomInfoData(roodId: Int)
}