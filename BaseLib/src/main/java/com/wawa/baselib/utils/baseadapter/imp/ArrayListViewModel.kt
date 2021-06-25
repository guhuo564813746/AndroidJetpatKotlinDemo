package com.wawa.baselib.utils.baseadapter.imp

import androidx.recyclerview.widget.RecyclerView
import com.wawa.baselib.utils.baseadapter.BaseRecyclerViewModel
import com.wawa.baselib.utils.baseadapter.BaseViewHolder

/**
 *作者：create by 张金 on 2021/6/25 15:41
 *邮箱：564813746@qq.com
 */
abstract class ArrayListViewModel<M> : BaseRecyclerViewModel<M,BaseViewHolder>(){

    override fun onBindView(adapter: RecyclerView.Adapter<BaseViewHolder>) {
        onBindAdapter(adapter = adapter as ArrayListAdapter<M>)
    }

    abstract fun onBindAdapter(adapter: ArrayListAdapter<M>)

}