package com.wawa.baselib.utils.baseadapter

import android.content.Context
import android.util.SparseArray
import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView

/**
 *作者：create by 张金 on 2021/6/25 12:02
 *邮箱：564813746@qq.com
 */
class BaseViewHolder(val view: View) : RecyclerView.ViewHolder(view){
    //views 緩存
    private val views: SparseArray<View> = SparseArray()
    private val mContext: Context=view.context
    fun <T: View> getView(@IdRes  viewId: Int) : T?{
        return retrieveView(viewId)
    }

    private fun <T: View> retrieveView(@IdRes viewId: Int): T?{
        var view=views[viewId]
        if (view == null){
            view=itemView.findViewById(viewId)
            if (view == null){
                return null
            }
            views.put(viewId,view)
        }
        return view as T
    }

}