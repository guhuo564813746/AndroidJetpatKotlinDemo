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

    override fun getChargeOrderList(orderId: Int?) {

    }

    override fun getChargeItemList() {

    }

    override fun getGameRecordList() {

    }

    override fun getOrderList(orderId: Int?) {

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

    override fun getRoomList(categoryId: Int?, roomId: Int?) {
        val roomListQuery = RoomListQuery()
    }

    override fun getUserCoinLogList() {

    }

    override fun getUserPointLogList() {

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