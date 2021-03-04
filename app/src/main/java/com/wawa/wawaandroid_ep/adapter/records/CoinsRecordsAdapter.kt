package com.wawa.wawaandroid_ep.adapter.records

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.apollographql.apollo.ChargeOrderListQuery
import com.apollographql.apollo.UserCoinLogListQuery
import com.wawa.wawaandroid_ep.R

/**
 *作者：create by 张金 on 2021/3/4 10:33
 *邮箱：564813746@qq.com
 */
class CoinsRecordsAdapter(
    private val context: Context
) : RecyclerView.Adapter<CoinsRecordsAdapter.CoinRecordsViewHolder>(){
    var list: List<UserCoinLogListQuery.List>?= null
    inner class CoinRecordsViewHolder(view: View): RecyclerView.ViewHolder(view){
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinRecordsViewHolder {
        val view= LayoutInflater.from(context).inflate(R.layout.recordlist_item_lay,parent,false)
        return CoinRecordsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    override fun onBindViewHolder(holder: CoinRecordsViewHolder, position: Int) {
        holder.tvRecordRighttips.setText("Coins")
        holder.tvRecordtitle.setText(list?.get(position)?.fragments()?.userCoinLogFragment()?.title())
        holder.tvRecordtime.setText(list?.get(position)?.fragments()?.userCoinLogFragment()?.timeFormat())
        var action=list?.get(position)?.fragments()?.userCoinLogFragment()?.action()
        var amount=list?.get(position)?.fragments()?.userCoinLogFragment()?.amount()
        action?.let {
            if (action==0){
                holder.tvRecordNum.setText("-$amount")
            }else{
                holder.tvRecordNum.setText("+$amount")
            }
        }
    }
}