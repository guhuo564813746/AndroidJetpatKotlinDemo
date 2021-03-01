package com.wawa.wawaandroid_ep.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils
import com.wawa.wawaandroid_ep.R
import com.wawa.wawaandroid_ep.bean.game.GameRoomUsers

/**
 *作者：create by 张金 on 2021/2/26 17:27
 *邮箱：564813746@qq.com
 */
class GameOnlineUserListAdapter(private val context: Context
                                ,private val list: List<GameRoomUsers>) : RecyclerView.Adapter<GameOnlineUserListAdapter.GameOnlineUserViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameOnlineUserViewHolder {
        var view=LayoutInflater.from(context).inflate(R.layout.game_userlist_item_lay,parent,false)
        var holder=GameOnlineUserViewHolder(view)
        return holder
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: GameOnlineUserViewHolder, position: Int) {
        initViewHolder(holder,position)
    }

    fun initViewHolder(holder: GameOnlineUserViewHolder,position: Int){
        val lp =
            RelativeLayout.LayoutParams(holder.itemView.getLayoutParams())
        if (position == list.size - 1) {
            lp.setMargins(0, 0, 0, 0)
        } else {
            lp.setMargins(SizeUtils.dp2px(-10f), 0, 0, 0)
        }
        holder.itemView.setLayoutParams(lp)
    }

    inner class GameOnlineUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private lateinit var imHead: ImageView
        init {
            imHead=itemView.findViewById(R.id.imHead)
        }
    }

}