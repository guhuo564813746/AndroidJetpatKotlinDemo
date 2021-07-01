package com.wawa.baselib.utils.baseadapter

import android.content.Context
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

/**
 *作者：create by 张金 on 2021/6/25 14:42
 *邮箱：564813746@qq.com
 */
abstract class BaseRecyclerViewModel<M, VH : BaseViewHolder> {
    var model: M?=null
    var viewHolder: VH?= null
    lateinit var mContext: Context
    abstract fun onBindView(adapter: RecyclerView.Adapter<VH> )
    fun getItemViewType(): Int{
        return getLayoutRes()
    }
    @LayoutRes
    abstract fun getLayoutRes(): Int
}