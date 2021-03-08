package com.wawa.wawaandroid_ep.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wawa.wawaandroid_ep.R
import com.wawa.wawaandroid_ep.adapter.LiveChatListAdapter.Vh
import com.wawa.wawaandroid_ep.bean.game.GameRoomChatDataBean
import com.wawa.wawaandroid_ep.utils.stringutils.TextRender
import java.util.*

class LiveChatListAdapter(private val mContext: Context) :
    RecyclerView.Adapter<Vh>() {
    private var mList: List<GameRoomChatDataBean.MsgListBean>
    private val mInflater: LayoutInflater
    private var mRecyclerView: RecyclerView? = null

    fun clear() {
        mList= listOf()
        notifyDataSetChanged()
    }

    fun insertItem(bean: List<GameRoomChatDataBean.MsgListBean>?) {
        if (bean != null) {
            mList+=bean
        }
        if (mList.size > 30) {
            mList = mList.subList(10, mList.size)
            notifyItemRangeRemoved(0, 10)
            notifyItemRangeChanged(0, mList.size)
        } else {
            val position: Int = mList.size - 1
            notifyItemInserted(position)
            notifyItemRangeChanged(position, mList.size)
        }
        mRecyclerView!!.smoothScrollToPosition(mList.size - 1)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
    }



    override fun onBindViewHolder(vh: Vh, position: Int) {
        vh.setData(mList[position], position)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class Vh(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv: TextView
        var mBean: GameRoomChatDataBean.MsgListBean? = null
        var mPosition = 0
        fun setData(bean: GameRoomChatDataBean.MsgListBean?, position: Int) {
            mBean = bean
            mPosition = position
            tv.setText(TextRender.createChat(mContext,bean))
        }

        init {
            tv = itemView as TextView
        }
    }

    init {
        mList = listOf()
        mInflater = LayoutInflater.from(mContext)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(mInflater.inflate(R.layout.item_list_live_chat, parent, false))
    }
}