package com.wawa.wawaandroid_ep.adapter.records

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.apollographql.apollo.ChargeOrderListQuery
import com.robotwar.app.R

/**
 *作者：create by 张金 on 2021/3/4 10:33
 *邮箱：564813746@qq.com
 */
class ChargesRecordsAdapter(
    private val context:Context
) :RecyclerView.Adapter<ChargesRecordsAdapter.ChargeRecordsViewHolder>(){
    var list: List<ChargeOrderListQuery.List>?=null
    inner class ChargeRecordsViewHolder(view: View):RecyclerView.ViewHolder(view){
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChargeRecordsViewHolder {
        val view=LayoutInflater.from(context).inflate(R.layout.recordlist_item_lay,parent,false)
        return ChargeRecordsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    override fun onBindViewHolder(holder: ChargeRecordsViewHolder, position: Int) {
        holder.tvRecordtitle.setText(list?.get(position)?.name())
        holder.tvRecordtime.setText(list?.get(position)?.order()?.addtimeFormat())
        var virtualGoodsType=list?.get(position)?.goods()?.virtualGoodsType()
        var virtualGoodsAmount=list?.get(position)?.goods()?.virtualGoodsAmount()
        var virtualGoodsGiveAmount=list?.get(position)?.goods()?.virtualGoodsGiveAmount()
        var goodsTips=""
        virtualGoodsType?.let {
            if (virtualGoodsType == 1){
                goodsTips="+ ${virtualGoodsAmount?.let {
                    if (virtualGoodsGiveAmount != null){
                        it+virtualGoodsGiveAmount
                    }else{
                        it
                    }
                }} Coins"
            }else if (virtualGoodsType == 2){
                goodsTips="+${virtualGoodsAmount?.let {
                    if (virtualGoodsGiveAmount != null){
                        it+virtualGoodsGiveAmount
                    }else{
                        it
                    }
                }} Points"
            }
        }
        holder.tvRecordNum.setText(goodsTips)
        holder.tvRecordRighttips.setText(list?.get(position)?.order()?.fee()?.toString())
    }
}