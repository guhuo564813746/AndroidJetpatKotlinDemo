package com.wawa.wawaandroid_ep.adapter.records

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.apollographql.apollo.GameRecordListQuery
import com.apollographql.apollo.UserCoinLogListQuery
import com.robotwar.app.R

/**
 *作者：create by 张金 on 2021/3/4 10:32
 *邮箱：564813746@qq.com
 */
class GamesRecordsAdapter(
    private val context: Context
) : RecyclerView.Adapter<GamesRecordsAdapter.GameRecordsViewHolder>(){
    var list: List<GameRecordListQuery.List>?= listOf()
    inner class GameRecordsViewHolder(view: View): RecyclerView.ViewHolder(view){
        lateinit var tvRecordtitle: TextView
        lateinit var tvRecordtime: TextView
        lateinit var tvRecordNum: TextView
        lateinit var tvRecordRighttips: TextView
        init {
            tvRecordtitle=itemView.findViewById(R.id.tv_recordtitle)
            tvRecordtime=itemView.findViewById(R.id.tv_recordtime)
            tvRecordNum=itemView.findViewById(R.id.tv_record_num)
            tvRecordRighttips=itemView.findViewById(R.id.tv_record_righttips)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameRecordsViewHolder {
        val view= LayoutInflater.from(context).inflate(R.layout.recordlist_item_lay,parent,false)
        return GameRecordsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    override fun onBindViewHolder(holder: GameRecordsViewHolder, position: Int) {
        holder.tvRecordtitle.setText(list?.get(position)?.fragments()?.gameRecordFragment()?.roomTitle())
        holder.tvRecordtime.setText(list?.get(position)?.fragments()?.gameRecordFragment()?.timeFormat())
        val consumedCoin=list?.get(position)?.fragments()?.gameRecordFragment()?.consumedCoin()
        val consumedPoint=list?.get(position)?.fragments()?.gameRecordFragment()?.consumedPoint()
        val earnCoin =list?.get(position)?.fragments()?.gameRecordFragment()?.earnCoin()
        val earnHardcoin=list?.get(position)?.fragments()?.gameRecordFragment()?.earnHardcoin()
        val earnPoint=list?.get(position)?.fragments()?.gameRecordFragment()?.earnPoint()
        var recordNum=""
        if (consumedCoin != null) {
            if (consumedCoin > 0){
                recordNum="-$consumedCoin Coins"
            }
        }
        if (consumedPoint != null){
            if (consumedPoint >0){
                recordNum="-$consumedPoint Points"
            }
        }
        holder.tvRecordNum.setText(recordNum)
        var resultTips=""
        if (earnCoin != null){
            if (earnCoin >0){
                resultTips="+$earnCoin Coins"
            }
        }
        if (earnHardcoin != null){
            if (earnHardcoin >0){
                resultTips="+$earnHardcoin Hardcoins"
            }
        }
        earnPoint?.let {
            if (earnPoint > 0){
                resultTips="+$earnPoint Points"
            }
        }
        holder.tvRecordRighttips.setText(resultTips)
    }
}