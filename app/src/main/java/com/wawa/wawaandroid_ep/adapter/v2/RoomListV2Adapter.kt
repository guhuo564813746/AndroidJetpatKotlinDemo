package com.wawa.wawaandroid_ep.adapter.v2

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.os.Build
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.TextAppearanceSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.DrawableUtils
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.apollographql.apollo.RoomListQuery
import com.blankj.utilcode.util.SpanUtils
import com.robotwar.app.R
import com.wawa.baselib.utils.AppUtils
import com.wawa.baselib.utils.glide.loader.ImageLoader
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException

/**
 *作者：create by 张金 on 2021/6/23 15:10
 *邮箱：564813746@qq.com
 */
class RoomListV2Adapter(private val mContext: Context) : RecyclerView.Adapter<RoomListV2Adapter.RoomListV2ViewHolder>(){
    var roomLists: List<RoomListQuery.List>?= mutableListOf()
    val TAG="RoomListV2Adapter"
    inner class RoomListV2ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        lateinit var img: ImageView
        lateinit var tvRoomStatus: TextView
        lateinit var name: TextView
        lateinit var shortDesc: TextView
        init {
            img=itemView.findViewById(R.id.img)
            tvRoomStatus=itemView.findViewById(R.id.tv_room_status)
            name=itemView.findViewById(R.id.name)
            shortDesc=itemView.findViewById(R.id.short_desc)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomListV2ViewHolder {
        val view = LayoutInflater.from(mContext)
            .inflate(R.layout.room_list_v2_item_lay, parent, false)
        return RoomListV2ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return roomLists?.size ?: 0
    }

    override fun onBindViewHolder(holder: RoomListV2ViewHolder, position: Int) {
        bindVH(holder,position)
    }

    private fun bindVH(holder: RoomListV2ViewHolder, position: Int){
        ImageLoader.with(mContext)
            .url(roomLists?.get(position)?.fragments()?.roomFragment()?.thumb())
//            .placeHolder(R.mipmap.ic_launcher)
            .rectRoundCorner(5, RoundedCornersTransformation.CornerType.TOP)
            .into(holder.img)
        holder.name.text=roomLists?.get(position)?.fragments()?.roomFragment()?.title().toString()
        var roomStatus=0
        var gameStatus=0
        roomLists?.get(position)?.fragments()?.roomFragment()?.status()?.let {
            roomStatus=it
        }
        roomLists?.get(position)?.fragments()?.roomFragment()?.gameStatus()?.let {
            gameStatus=it
        }
        var statusBg: GradientDrawable=GradientDrawable()
        val leftTopCornor=0
        val rightTopCornor= AppUtils.dp2px(mContext,10f)
        val leftBottomCornor=0
        val rightBottomCornor= AppUtils.dp2px(mContext,10f)
        val cornors= floatArrayOf(leftTopCornor.toFloat(),leftTopCornor.toFloat(),rightTopCornor.toFloat(),rightTopCornor.toFloat()
            ,rightBottomCornor.toFloat(),rightBottomCornor.toFloat(),leftBottomCornor.toFloat(),leftBottomCornor.toFloat())
        statusBg.cornerRadii=cornors
        when(roomStatus){
            //下架
            0 ->{
                statusBg.setColor(Color.parseColor("#999999"))
                holder.tvRoomStatus.text="已下架"
            }
            //上架
            1 ->{
                when(gameStatus){
                    //空闲中
                    0 ->{
                        statusBg.setColor(Color.parseColor("#A680FF"))
                        holder.tvRoomStatus.text="空闲中"
                    }
                    //热玩中
                    1 ->{
                        statusBg.setColor(Color.parseColor("#FB6A6A"))
                        holder.tvRoomStatus.text="热玩中"
                    }
                }
            }
            //维修中
            2 ->{
                statusBg.setColor(Color.parseColor("#FB6A6A"))
                holder.tvRoomStatus.text="维修中"
            }
        }
        holder.tvRoomStatus.background=statusBg
        val coinsPer=roomLists?.get(position)?.fragments()?.roomFragment()?.roomGameOption()?.fragments()?.roomGameOptionFragment()?.coin2hardRatio()?.toInt()
        val shortDescTips="$coinsPer 游戏币/次"
        val coinPerSS= SpannableStringBuilder()
        coinPerSS.append(shortDescTips)
        var colorList: ColorStateList?=null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            colorList=mContext.getColorStateList(R.color.coins_colors)
        }
        coinPerSS.setSpan(TextAppearanceSpan("serif", Typeface.NORMAL,AppUtils.dp2px(mContext,16f),colorList,colorList),
            0,coinsPer?.toString()?.length ?: 0, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        holder.shortDesc.text=coinPerSS
        holder.itemView.setOnClickListener {
            var bundle= Bundle()
            val roomItemInfo=roomLists?.get(position)
            Log.d(TAG,"ROOMINFO--"+roomItemInfo.toString())
            bundle.putSerializable("ROOM_ID",roomItemInfo?.fragments()?.roomFragment()?.roomId())
            roomLists?.get(position)?.fragments()?.roomFragment()?.machine()?.rawValue()
//            it.findNavController().navigate(R.id.robotActivity,bundle)
            it.findNavController().navigate(R.id.fishGameRoomActivity,bundle)
        }
    }
}