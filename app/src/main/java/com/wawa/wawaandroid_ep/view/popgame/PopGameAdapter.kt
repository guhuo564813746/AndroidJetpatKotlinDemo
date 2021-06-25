package com.coinhouse777.wawa.widget.popgame

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.robotwar.app.R
import com.wawa.wawaandroid_ep.view.popgame.PopGameItemBean

/**
 *作者：create by 张金 on 2021/4/26 17:43
 *邮箱：564813746@qq.com
 */
class PopGameAdapter(private val context: Context,private var list: MutableList<PopGameItemBean>) : RecyclerView.Adapter<PopGameAdapter.PopGameVH>(){

    private var onItemClickListener: OnItemClickListener? = null
    fun setDatas(mList: MutableList<PopGameItemBean>){
        this.list=mList
        notifyDataSetChanged()
    }

     inner class PopGameVH(itemView: View) : RecyclerView.ViewHolder(itemView){
         lateinit var btnGameset: ImageView
         init {
             btnGameset=itemView.findViewById(R.id.btn_gameset)
         }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopGameVH {
        val view=LayoutInflater.from(context).inflate(R.layout.popgame_item_lay,parent,false)
        return PopGameVH(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: PopGameVH, position: Int) {
        holder.itemView.isEnabled=list.get(position).enableTab
        holder.itemView.isClickable=list.get(position).enableTab
        holder.btnGameset.setImageResource(list.get(position).imgRes)
        holder.itemView.setOnClickListener {
            if (onItemClickListener != null){
                onItemClickListener!!.onItemClick(it,position)
            }
        }
    }
    fun setOnItemClickListener(listener: OnItemClickListener){
        this.onItemClickListener=listener
    }
    interface OnItemClickListener{
        fun onItemClick(view: View,pos: Int)
    }
}