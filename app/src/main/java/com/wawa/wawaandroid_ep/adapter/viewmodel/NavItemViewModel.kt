package com.wawa.wawaandroid_ep.adapter.viewmodel

import android.widget.ImageView
import android.widget.TextView
import com.apollographql.apollo.fragment.PageOptionFragment
import com.robotwar.app.R
import com.wawa.baselib.utils.baseadapter.imp.ArrayListAdapter
import com.wawa.baselib.utils.baseadapter.imp.ArrayListViewModel
import com.wawa.baselib.utils.glide.loader.ImageLoader

/**
 *作者：create by 张金 on 2021/6/29 11:10
 *邮箱：564813746@qq.com
 */
class NavItemViewModel : ArrayListViewModel<PageOptionFragment.HomeMenu>() {

    override fun onBindAdapter(adapter: ArrayListAdapter<PageOptionFragment.HomeMenu>) {
        viewHolder?.view?.findViewById<TextView>(R.id.tv_main_navigation)?.setText(model?.fragments()?.appMenu()?.name())
        viewHolder?.view?.findViewById<ImageView>(R.id.im_navigation_tag)?.let {
            ImageLoader.with(viewHolder?.view?.context)
                .url(model?.fragments()?.appMenu()?.url())
                .into(it)
        }
    }

    override fun getLayoutRes(): Int {
        return R.layout.navlist_item_lay
    }
}