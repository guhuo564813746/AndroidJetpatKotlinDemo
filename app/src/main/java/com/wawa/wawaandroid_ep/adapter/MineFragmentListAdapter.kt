package com.wawa.wawaandroid_ep.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.robotwar.app.R
import com.wawa.wawaandroid_ep.bean.mine.MineListBean
import com.wawa.wawaandroid_ep.fragment.MineFragment

/**
 *作者：create by 张金 on 2021/3/2 15:04
 *邮箱：564813746@qq.com
 */
class MineFragmentListAdapter(private val fragment: MineFragment
        ,private val list: List<MineListBean>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(fragment.activity)
            .inflate(R.layout.mine_fragment_list_item_lay, parent, false)
        return MineFragmentListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var mineFragmentListViewHolder= (holder as MineFragmentListViewHolder)
        mineFragmentListViewHolder.icon.setImageResource(list.get(position).itemImSrc)
        mineFragmentListViewHolder.name.setText(list.get(position).title)
        holder.itemView.setOnClickListener {
            if (position==0){
                (fragment as MineFragment).goRecordListPage(it)
            }else if (position ==1){
                (fragment as MineFragment).goSettingPage(it)
            }
        }
    }

    inner class MineFragmentListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        lateinit var icon: ImageView
        lateinit var name: TextView
        init {
            icon=itemView.findViewById(R.id.icon)
            name=itemView.findViewById(R.id.name)
        }
    }

}