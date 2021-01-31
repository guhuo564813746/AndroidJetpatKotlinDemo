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
    protected val chargeOrderListSubject: PublishSubject<ChargeOrderListQuery.ChargeOrderList> = PublishSubject.create()
    protected val chargeItemListSubject: PublishSubject<ChargeItemListQuery.ChargeItemList> = PublishSubject.create()
    protected val gameRecordListSubject: PublishSubject<GameRecordListQuery.GameRecordList> = PublishSubject.create()
    protected val orderListSubject: PublishSubject<OrderListQuery.OrderList> = PublishSubject.create()
    protected val roomCategoryListSubject: PublishSubject<RoomCategoryListQuery.RoomCategoryList> = PublishSubject.create()
    protected val roomListSubject: PublishSubject<RoomListQuery.RoomList> = PublishSubject.create()
    protected val userCoinLogListSubject: PublishSubject<UserCoinLogListQuery.UserCoinLogList> = PublishSubject.create()
    protected val userPointLogListSubject: PublishSubject<UserPointLogListQuery.UserPointLogList> = PublishSubject.create()
    protected val userDataSubject: PublishSubject<UserQuery.User> = PublishSubject.create()

    val bannerList: Observable<List<BannerListQuery.BannerList>> =bannerListSubject.hide()
    val chargeOrderList: Observable<ChargeOrderListQuery.ChargeOrderList> = chargeOrderListSubject.hide()
    val chargeItemList: Observable<ChargeItemListQuery.ChargeItemList> = chargeItemListSubject.hide()
    val gameRecordList: Observable<GameRecordListQuery.GameRecordList> = gameRecordListSubject.hide()
    val orderList: Observable<OrderListQuery.OrderList> = orderListSubject.hide()
    val roomCategoryList: Observable<RoomCategoryListQuery.RoomCategoryList> =roomCategoryListSubject.hide()
    val roomList: Observable<RoomListQuery.RoomList> = roomListSubject.hide()
    val userCoinLogList: Observable<UserCoinLogListQuery.UserCoinLogList> =userCoinLogListSubject.hide()
    val userPointLogList: Observable<UserPointLogListQuery.UserPointLogList> =userPointLogListSubject.hide()
    val userData: Observable<UserQuery.User> = userDataSubject.hide()

    abstract fun  getBannerList(categoryId: Int)
    abstract fun getChargeOrderList(orderId: Int?)
    abstract fun getChargeItemList()
    abstract fun getGameRecordList()
    abstract fun getOrderList(orderId:Int?)
    abstract fun getRoomCategoryList()
    abstract fun getRoomList(categoryId: Int?,roomId:Int?)
    abstract fun getUserCoinLogList()
    abstract fun getUserPointLogList()
    abstract fun getUserData()
}