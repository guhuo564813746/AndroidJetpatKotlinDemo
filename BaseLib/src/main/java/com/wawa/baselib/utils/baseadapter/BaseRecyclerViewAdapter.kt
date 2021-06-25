package com.wawa.baselib.utils.baseadapter

import android.util.SparseIntArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 *作者：create by 张金 on 2021/6/25 14:52
 *邮箱：564813746@qq.com
 */
abstract class BaseRecyclerViewAdapter<VM : BaseRecyclerViewModel<*,BaseViewHolder>> : RecyclerView.Adapter<BaseViewHolder>(){
    protected val layouts: SparseIntArray by lazy(LazyThreadSafetyMode.NONE) {
        SparseIntArray()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(LayoutInflater.from(parent.context).inflate(layouts[viewType],parent,false))
    }

    override fun getItemViewType(position: Int): Int {
        val item=getItem(position)
        layouts.append(item.getItemViewType(),item.getLayoutRes())
        return item.getItemViewType()
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val  item=getItem(position)
        item.onBindView(this)
    }

    abstract fun getItem(position: Int): VM
}