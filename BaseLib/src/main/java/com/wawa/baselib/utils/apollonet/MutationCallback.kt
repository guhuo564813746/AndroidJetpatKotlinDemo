package com.wawa.baselib.utils.apollonet

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.blankj.utilcode.util.ToastUtils
import com.wawa.baselib.utils.dialog.LoadingDialogManager

/**
 *作者：create by 张金 on 2021/3/12 10:49
 *邮箱：564813746@qq.com
 */
open class MutationCallback<T> : ApolloCall.Callback<T>(){
    override fun onFailure(e: ApolloException) {
        LoadingDialogManager.dismissLoading()
        ToastUtils.showShort(e.message)
    }

    override fun onResponse(response: Response<T>) {
        LoadingDialogManager.dismissLoading()
        response?.errors?.let {
            if (it.size > 0){
                ToastUtils.showShort(it.get(0).message)
            }
        }
    }
}