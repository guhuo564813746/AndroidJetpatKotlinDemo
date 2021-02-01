package com.wawa.baselib.utils.apollonet.service

import com.apollographql.apollo.*
import com.apollographql.apollo.coroutines.await
import com.wawa.baselib.utils.apollonet.BaseDataSource
import kotlinx.coroutines.*
import kotlin.Exception
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

    private var job: Job?=null

    override fun getBannerList(categoryId: Int) {
        val bannerListQuery: BannerListQuery= BannerListQuery(categoryId)
        job= CoroutineScope(processContext).launch {
            try {
                val response=apolloClient.query(bannerListQuery).await()
                val bannerList=response?.data?.bannerList()?.filterNotNull().orEmpty()
                withContext(resultContext){
                    bannerListSubject.onNext(bannerList)
                }
            }catch (e: Exception){
                exceptionSubject.onNext(e)
            }
        }
    }

    override fun getChargeOrderList() {
        val chargeOrderListQuery=ChargeOrderListQuery()
        job= CoroutineScope(processContext).launch {
            try {
                val response=apolloClient.query(chargeOrderListQuery).await()
                val chargeOrderList=response?.data?.chargeOrderList()?.filterNotNull().orEmpty()
                withContext(resultContext){
                    chargeOrderListSubject.onNext(chargeOrderList)
                }
            }catch (e: Exception){
                exceptionSubject.onNext(e)
            }
        }
    }

    override fun getChargeItemList() {
        val chargeItemListQuery = ChargeItemListQuery()
        job= CoroutineScope(processContext).launch {
            try {
                val response=apolloClient.query(chargeItemListQuery).await()
                val chargeItemList=response?.data?.chargeItemList()?.filterNotNull().orEmpty()
                withContext(resultContext){
                    chargeItemListSubject.onNext(chargeItemList)
                }
            }catch (e: Exception){
                exceptionSubject.onNext(e)
            }
        }
    }

    override fun getGameRecordList() {
        val gameRecordListQuery=GameRecordListQuery()
        job= CoroutineScope(processContext).launch {
            try {
                val response=apolloClient.query(gameRecordListQuery).await()
                val gameRecordList=response?.data?.gameRecordList()?.filterNotNull().orEmpty()
                withContext(resultContext){
                    gameRecordListSubject.onNext(gameRecordList)
                }
            }catch (e: Exception){
                exceptionSubject.onNext(e)
            }
        }
    }

    override fun getOrderList() {
        val orderListQuery=OrderListQuery()
        job= CoroutineScope(processContext).launch {
            try {
                val response=apolloClient.query(orderListQuery).await()
                val orderList=response?.data?.orderList()?.filterNotNull().orEmpty()
                withContext(resultContext){
                    orderListSubject.onNext(orderList)
                }
            }catch (e: Exception){
                exceptionSubject.onNext(e)
            }
        }
    }

    override fun getRoomCategoryList() {
        val roomCategoryListQuery = RoomCategoryListQuery()
        job= CoroutineScope(processContext).launch {
            try {
                val response=apolloClient.query(roomCategoryListQuery).await()
                val categoryList=response?.data?.roomCategoryList()?.filterNotNull().orEmpty()
                withContext(resultContext){
                    roomCategoryListSubject.onNext(categoryList)
                }
            }catch (e: Exception){
                exceptionSubject.onNext(e)
            }
        }
    }

    override fun getRoomList() {
        val roomListQuery = RoomListQuery()
        job= CoroutineScope(processContext).launch {
            try {
                val response=apolloClient.query(roomListQuery).await()
                val roomList=response?.data?.roomList()?.filterNotNull().orEmpty()
                withContext(resultContext){
                    roomListSubject.onNext(roomList)
                }
            }catch (e: Exception){
                exceptionSubject.onNext(e)
            }
        }
    }

    override fun getUserCoinLogList() {
        val userCoinLogListQuery=UserCoinLogListQuery()
        job= CoroutineScope(processContext).launch {
            try {
                val response=apolloClient.query(userCoinLogListQuery).await()
                val userCoinLogList=response?.data?.userCoinLogList()?.filterNotNull().orEmpty()
                withContext(resultContext){
                    userCoinLogListSubject.onNext(userCoinLogList)
                }
            }catch (e: Exception){
                exceptionSubject.onNext(e)
            }
        }
    }

    override fun getUserPointLogList() {
        val userPointLogListQuery=UserPointLogListQuery()
        job= CoroutineScope(processContext).launch {
            try {
                val response=apolloClient.query(userPointLogListQuery).await()
                val userPointLogList=response?.data?.userPointLogList()?.filterNotNull().orEmpty()
                withContext(resultContext){
                    userPointLogListSubject.onNext(userPointLogList)
                }
            }catch (e: Exception){
                exceptionSubject.onNext(e)
            }
        }
    }

    override fun getUserData() {
        val userQuery=UserQuery()
        job= CoroutineScope(processContext).launch {
            try {
                val response=apolloClient.query(userQuery).await()
                val userData=response?.data?.user()
                withContext(resultContext){
                    if (userData != null) {
                        userDataSubject.onNext(userData)
                    }
                }
            }catch (e: Exception){
                exceptionSubject.onNext(e)
            }
        }
    }

}