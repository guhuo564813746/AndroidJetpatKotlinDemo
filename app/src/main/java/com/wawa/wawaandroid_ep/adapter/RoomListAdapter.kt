package com.wawa.wawaandroid_ep.adapter

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.apollographql.apollo.RoomListQuery
import com.apollographql.apollo.RoomListQuery.RoomList
import com.robotwar.app.R
import com.wawa.baselib.utils.glide.loader.ImageLoader
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

/**
 * 作者：create by 张金 on 2021/2/2 10:52
 * 邮箱：564813746@qq.com
 */
class RoomListAdapter(
    private val mContext: Context,
    private val roomLists: List<RoomListQuery.List>?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TAG="RoomListAdapter"
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(mContext)
            .inflate(R.layout.roomlist_item_lay, parent, false)
        return RoomListViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val roomListViewHolder: RoomListViewHolder= holder as RoomListViewHolder
        roomListViewHolder.name.text=roomLists?.get(position)?.fragments()?.roomFragment()?.title().toString()
        roomListViewHolder.shortDesc.text=roomLists?.get(position)?.fragments()?.roomFragment()?.shortDesc()
//        roomListViewHolder.coin.text=roomLists?.get(position)?.
        ImageLoader.with(mContext)
            .url(roomLists?.get(position)?.fragments()?.roomFragment()?.thumb())
//            .placeHolder(R.mipmap.ic_launcher)
            .rectRoundCorner(15, RoundedCornersTransformation.CornerType.TOP)
            .into(roomListViewHolder.img)
        roomListViewHolder.itemView.setOnClickListener {
            var bundle=Bundle()
            val roomItemInfo=roomLists?.get(position)
            Log.d(TAG,"ROOMINFO--"+roomItemInfo.toString())
            bundle.putSerializable("ROOM_ID",roomItemInfo?.fragments()?.roomFragment()?.roomId())
            it.findNavController().navigate(R.id.robotActivity,bundle)
        }

    }

    override fun getItemCount(): Int {
        return roomLists?.size ?: 0
    }

    private inner class RoomListViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var img: ImageView
        var iconCoin: ImageView
        var imGamelistTag: ImageView
        var coin: TextView
        var imPkgame: ImageView
        var status: TextView
        var name: TextView
        var shortDesc: TextView

        init {
            img = itemView.findViewById(R.id.img)
            iconCoin =
                itemView.findViewById(R.id.icon_coin)
            imGamelistTag =
                itemView.findViewById(R.id.im_gamelist_tag)
            coin = itemView.findViewById(R.id.coin)
            imPkgame =
                itemView.findViewById(R.id.im_pkgame)
            status =
                itemView.findViewById(R.id.status)
            name = itemView.findViewById(R.id.name)
            shortDesc =
                itemView.findViewById(R.id.short_desc)
        }
    }

}