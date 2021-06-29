package com.wawa.wawaandroid_ep.adapter.viewmodel

import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apollographql.apollo.fragment.PageOptionFragment
import com.robotwar.app.R
import com.wawa.baselib.utils.baseadapter.BaseRecyclerViewAdapter
import com.wawa.baselib.utils.baseadapter.BaseRecyclerViewModel
import com.wawa.baselib.utils.baseadapter.BaseViewHolder
import com.wawa.baselib.utils.baseadapter.imp.ArrayListAdapter
import com.wawa.baselib.utils.baseadapter.imp.ArrayListViewModel

/**
 *作者：create by 张金 on 2021/6/29 15:44
 *邮箱：564813746@qq.com
 */
class AppMenuListViewModel : ArrayListViewModel<List<PageOptionFragment.AppMenu>>() {
    val menuAdapter= ArrayListAdapter<PageOptionFragment.AppMenu>()
    override fun onBindAdapter(adapter: ArrayListAdapter<List<PageOptionFragment.AppMenu>>) {
        viewHolder?.view?.let {
            var lvMenu: RecyclerView=viewHolder!!.view.findViewById<RecyclerView>(R.id.lv_appmenus_item)
            lvMenu.setHasFixedSize(true)
            val manager=LinearLayoutManager(viewHolder!!.view.context,LinearLayoutManager.VERTICAL,false)
            lvMenu.bindAdapter(menuAdapter,manager)
            model?.size?.let {
                if (it > 0){
                    var menuList= ObservableArrayList<PageOptionFragment.AppMenu>()
                    for (i in 0..(model!!.size-1)){
                        val appMenuItemVM=AppMenuViewModel()
                        appMenuItemVM.model=model!!.get(i)
                        menuAdapter.add(i,appMenuItemVM)
                    }
                }
            }
        }
    }

    override fun getLayoutRes(): Int {
        return R.layout.appmenu_listitem_lay
    }

    fun <VM : BaseRecyclerViewModel<*, BaseViewHolder>> RecyclerView.bindAdapter(listAdapter: BaseRecyclerViewAdapter<VM>
                                                                                 , layoutManager: RecyclerView.LayoutManager?= null){
        this.layoutManager=layoutManager
        this.adapter=listAdapter
    }

}