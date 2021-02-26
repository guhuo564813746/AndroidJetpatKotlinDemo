package com.wawa.wawaandroid_ep.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wawa.wawaandroid_ep.R

/**
 *作者：create by 张金 on 2021/2/26 17:27
 *邮箱：564813746@qq.com
 */
class GameOnlineUserListAdapter(private val context: Context
                                ,private val list: List<Any>) : RecyclerView.Adapter<GameOnlineUserListAdapter.GameOnlineUserViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameOnlineUserViewHolder {
        var view=LayoutInflater.from(context).inflate(R.layout.game_userlist_item_lay,parent,false)
        var holder=GameOnlineUserViewHolder(view)
        return holder
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: GameOnlineUserViewHolder, position: Int) {

    }

    inner class GameOnlineUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    }

}