package com.wawa.baselib.utils.apollonet

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.wawa.baselib.utils.dialog.LoadingDialogManager

/**
 *作者：create by 张金 on 2021/3/12 10:49
 *邮箱：564813746@qq.com
 */
open class MutationCallback<T> : ApolloCall.Callback<T>(){
    override fun onFailure(e: ApolloException) {
        LoadingDialogManager.dismissLoading()
    }

    override fun onResponse(response: Response<T>) {
        LoadingDialogManager.dismissLoading()
    }
}