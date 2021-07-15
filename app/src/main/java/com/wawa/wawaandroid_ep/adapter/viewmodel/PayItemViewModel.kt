package com.wawa.wawaandroid_ep.adapter.viewmodel

import com.apollographql.apollo.ChargeItemListQuery
import com.robotwar.app.R
import com.wawa.baselib.utils.baseadapter.imp.ArrayListAdapter
import com.wawa.baselib.utils.baseadapter.imp.ArrayListViewModel

/**
 *作者：create by 张金 on 2021/7/15 18:09
 *邮箱：564813746@qq.com
 */
class PayItemViewModel : ArrayListViewModel<ChargeItemListQuery.ChargeItemList>() {
    override fun onBindAdapter(adapter: ArrayListAdapter<ChargeItemListQuery.ChargeItemList>) {

    }

    override fun getLayoutRes(): Int {
        return R.layout.paylist_item_lay
    }
}