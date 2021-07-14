package com.wawa.baselib.utils.apollonet.service

import android.util.Log
import com.apollographql.apollo.*
import com.apollographql.apollo.coroutines.await
import com.wawa.baselib.utils.apollonet.BaseDataSource
import com.wawa.baselib.utils.logutils.LogUtils
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
    private val TAG="ApolloCoroutinesService"
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

    override fun getChargeOrderList(index: Int) {
        val chargeOrderListQuery=ChargeOrderListQuery(index)
        job= CoroutineScope(processContext).launch {
            try {
                val response=apolloClient.query(chargeOrderListQuery).await()
                val chargeOrderList=response?.data?.chargeOrderList()?.list()?.filterNotNull().orEmpty()
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

    override fun getGameRecordList(index: Int) {
        val gameRecordListQuery=GameRecordListQuery(index)
        job= CoroutineScope(processContext).launch {
            try {
                val response=apolloClient.query(gameRecordListQuery).await()
                val gameRecordList=response?.data?.gameRecordList()?.list()?.filterNotNull().orEmpty()
                withContext(resultContext){
                    gameRecordListSubject.onNext(gameRecordList)
                }
            }catch (e: Exception){
                exceptionSubject.onNext(e)
            }
        }
    }

    override fun getOrderList(index: Int) {
        val orderListQuery=OrderListQuery(index)
        job= CoroutineScope(processContext).launch {
            try {
                val response=apolloClient.query(orderListQuery).await()
                val orderList=response?.data?.orderList()?.list()?.filterNotNull().orEmpty()
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

    override fun getRoomList(categoryId: Int,index: Int) {
        LogUtils.d(TAG,"getRoomList")
        val roomListQuery = RoomListQuery(categoryId,index)
        job= CoroutineScope(processContext).launch {
            try {
                val response=apolloClient.query(roomListQuery).await()
                val roomList=response?.data?.roomList()?.list()?.filterNotNull().orEmpty()
                withContext(resultContext){
                    roomListSubject.onNext(roomList)
                }
            }catch (e: Exception){
                exceptionSubject.onNext(e)
            }
        }
    }

    override fun getUserCoinLogList(index: Int) {
        val userCoinLogListQuery=UserCoinLogListQuery(index)
        job= CoroutineScope(processContext).launch {
            try {
                val response=apolloClient.query(userCoinLogListQuery).await()
                val userCoinLogList=response?.data?.userCoinLogList()?.list()?.filterNotNull().orEmpty()
                withContext(resultContext){
                    userCoinLogListSubject.onNext(userCoinLogList)
                }
            }catch (e: Exception){
                exceptionSubject.onNext(e)
            }
        }
    }

    override fun getUserPointLogList(index: Int) {
        val userPointLogListQuery=UserPointLogListQuery(index)
        job= CoroutineScope(processContext).launch {
            try {
                val response=apolloClient.query(userPointLogListQuery).await()
                val userPointLogList=response?.data?.userPointLogList()?.list()?.filterNotNull().orEmpty()
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

    override fun getRoomInfoData(roodId: Int) {
        val roomInfoQuery=RoomInfoQuery(roodId)
        job= CoroutineScope(processContext).launch {
            try {
                val response=apolloClient.query(roomInfoQuery).await()
                val roomInfo=response?.data?.roomList()?.list()?.filterNotNull().orEmpty()
                withContext(resultContext){
                    if (roomInfo.isNotEmpty()) {
                        roomInfoSubject.onNext(roomInfo)
                    }
                }
            }catch (e: Exception){
                exceptionSubject.onNext(e)
            }
        }
    }

    override fun getConfigData() {
        val configQuery=ConfigDataQuery()
        job=CoroutineScope(processContext).launch {
            try {
                val response=apolloClient.query(configQuery).await()
                val config=response?.data?.config()
                withContext(resultContext){
                    config?.let {
                        configDataSubject.onNext(config)
                    }
                }
            }catch (e: Exception){
                exceptionSubject.onNext(e)
            }
        }
    }

    override fun getMalfunctionList(machine: String, index: Int) {
        val malfunctionQuery=MalfunctionListQuery(machine,1)
        job= CoroutineScope(processContext).launch {
            try {
                val response=apolloClient.query(malfunctionQuery).await()
                val malfunctionList= response?.data?.malfunctionList()
                withContext(resultContext){
                    malfunctionList?.let {
                        malfunctionListSubject.onNext(it)
                    }
                }
            }catch (e: Exception){
                exceptionSubject.onNext(e)
            }
        }
    }

    override fun getFeedbackList(index: Int,feedbackId: Int) {
        Log.d(TAG,"getFeedbackList--"+feedbackId)
        val feedBackQuery=FeedBackListQuery(index)
        job= CoroutineScope(processContext).launch {
            try {
                val response=apolloClient.query(feedBackQuery).await()
                val feedBackList= response?.data?.feedback()
                withContext(resultContext){
                    feedBackList?.let {
                        feedbackListSubject.onNext(it)
                    }
                }
            }catch (e: Exception){
                exceptionSubject.onNext(e)
            }
        }
    }

    override fun getFeedbackWithId(index: Int, feedbackId: Int) {
        val feedbackQuery= FeedBackListWithIdQuery(feedbackId,index)
        job= CoroutineScope(processContext).launch {
            try {
                val response=apolloClient.query(feedbackQuery).await()
                val feedBackList= response?.data?.feedback()
                withContext(resultContext){
                    feedBackList?.let {
                        feedbackListWithIdSubject.onNext(it)
                    }
                }
            }catch (e: Exception){
                exceptionSubject.onNext(e)
            }
        }
    }

    override fun getFbCommentList(feedbackId: Int,index: Int){
        Log.d(TAG,"getFbCommentList--")
        val feedBackCommentListQuery = FeedbackCommentQuery(feedbackId,index)
        job = CoroutineScope(processContext).launch {
            try {
                val response=apolloClient.query(feedBackCommentListQuery).await()
                val feedBackCommentList= response?.data
                withContext(resultContext){
                    feedBackCommentList?.let {
                        feedbackCommentListSubject.onNext(it)
                    }
                }
            }catch (e: Exception){
                exceptionSubject.onNext(e)
            }
        }
    }

}