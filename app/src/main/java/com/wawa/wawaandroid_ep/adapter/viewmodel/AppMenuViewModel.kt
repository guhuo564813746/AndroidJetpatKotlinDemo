package com.wawa.wawaandroid_ep.adapter.viewmodel

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.apollographql.apollo.fragment.PageOptionFragment
import com.robotwar.app.R
import com.wawa.baselib.utils.baseadapter.imp.ArrayListAdapter
import com.wawa.baselib.utils.baseadapter.imp.ArrayListViewModel
import com.wawa.baselib.utils.glide.loader.ImageLoader
import com.wawa.baselib.utils.logutils.LogUtils

/**
 *作者：create by 张金 on 2021/6/29 15:09
 *邮箱：564813746@qq.com
 */
class AppMenuViewModel : ArrayListViewModel<PageOptionFragment.AppMenu>() {
    val TAG="AppMenuViewModel"
    override fun onBindAdapter(adapter: ArrayListAdapter<PageOptionFragment.AppMenu>) {
        var iconUrl: String?=null
        var menuName: String?=""
        iconUrl=model?.fragments()?.appMenu()?.url()
        menuName=model?.fragments()?.appMenu()?.name()
        Log.d(TAG,"iconUrl--"+iconUrl +"menuName--"+menuName)
        viewHolder?.view?.let {
            Log.d(TAG,"onBindAdapter--")
            it.findViewById<TextView>(R.id.name).setText(menuName)
            iconUrl?.let {
                ImageLoader.with(viewHolder?.view?.context)
                    .url(it)
                    .into(viewHolder?.view?.findViewById<ImageView>(R.id.icon))
            }
            it.setOnClickListener {
                var modelType: String?=model?.fragments()?.appMenu()?.type()?.rawValue()
                LogUtils.d(TAG,"ModelType--"+modelType)
            }
        }
    }

    override fun getLayoutRes(): Int {
        return R.layout.appmenu_item_lay
    }

}