package com.wawa.baselib.utils.baseadapter.imp

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import com.wawa.baselib.utils.baseadapter.BaseRecyclerViewAdapter
import com.wawa.baselib.utils.baseadapter.BaseViewHolder

/**
 *作者：create by 张金 on 2021/6/25 15:37
 *邮箱：564813746@qq.com
 */
class ArrayListAdapter<M> : BaseRecyclerViewAdapter<ArrayListViewModel<M>>(){
    private val observableDataList=ObservableArrayList<ArrayListViewModel<M>>()

    init {
        observableDataList.addOnListChangedCallback(object: ObservableList.OnListChangedCallback<ObservableArrayList<ArrayListViewModel<M>>>(){
            override fun onChanged(sender: ObservableArrayList<ArrayListViewModel<M>>?) {
                notifyDataSetChanged()
            }

            override fun onItemRangeRemoved(
                sender: ObservableArrayList<ArrayListViewModel<M>>?,
                positionStart: Int,
                itemCount: Int
            ) {
                notifyItemRangeRemoved(positionStart, itemCount)
            }

            override fun onItemRangeMoved(
                sender: ObservableArrayList<ArrayListViewModel<M>>?,
                fromPosition: Int,
                toPosition: Int,
                itemCount: Int
            ) {
                notifyItemMoved(fromPosition,toPosition)
            }


            override fun onItemRangeInserted(
                sender: ObservableArrayList<ArrayListViewModel<M>>?,
                positionStart: Int,
                itemCount: Int
            ) {
                notifyItemRangeInserted(positionStart,itemCount)
            }

            override fun onItemRangeChanged(
                sender: ObservableArrayList<ArrayListViewModel<M>>?,
                positionStart: Int,
                itemCount: Int
            ) {
                notifyItemRangeChanged(positionStart, itemCount)
            }

        })
    }

    override fun getItem(position: Int): ArrayListViewModel<M> {
        return observableDataList[position]
    }

    override fun getItemCount(): Int {
        return observableDataList.size
    }

    fun addAllData(list: ObservableArrayList<ArrayListViewModel<M>>){
        observableDataList.addAll(list)
    }


    fun add(index: Int, element: ArrayListViewModel<M>){
        observableDataList.add(index, element)
    }

    fun add(element: ArrayListViewModel<M>){
        observableDataList.add(element)
    }

    fun set(index: Int,element: ArrayListViewModel<M>){
        observableDataList.set(index, element)
    }

    fun removeAt(index: Int): ArrayListViewModel<M>{
        return observableDataList.removeAt(index)
    }
    fun clearData(){
        observableDataList.clear()
    }
}