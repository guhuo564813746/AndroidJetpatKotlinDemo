package com.wawa.wawaandroid_ep.pay

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wawa.baseresourcelib.R


/**
 *作者：create by 张金 on 2021/3/4 18:21
 *邮箱：564813746@qq.com
 */
class PayTypeListAdapter(private val context: Context,var  list: List<Int>) : RecyclerView.Adapter<PayTypeListAdapter.PayTypeListViewHolder>() {
    var curPos=0
    inner class PayTypeListViewHolder(view: View) : RecyclerView.ViewHolder(view){
        lateinit var imPaytypeChoice: ImageView
        lateinit var imPaytypetips: ImageView
        lateinit var tvPaytypetips: TextView
        init {
            imPaytypeChoice=itemView.findViewById(R.id.im_paytype_choice)
            imPaytypetips=itemView.findViewById(R.id.im_paytypetips)
            tvPaytypetips=itemView.findViewById(R.id.tv_paytypetips)
            itemView.setOnClickListener {
                curPos=adapterPosition
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PayTypeListViewHolder {
       val view=LayoutInflater.from(context).inflate(R.layout.paytype_dialog_item,parent,false)
        return  PayTypeListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: PayTypeListViewHolder, position: Int) {
        if (position==curPos){
            holder.imPaytypeChoice.visibility=View.VISIBLE
        }else{
            holder.imPaytypeChoice.visibility=View.GONE
        }
        when(list.get(position)){
            PayManager.PAYTYPE_ZFB_H5 ->{
                holder.imPaytypetips.setImageResource(R.mipmap.im_payzfbtag)
                holder.tvPaytypetips.setText(context.getString(R.string.ali_pay)+"_H5")
            }

            PayManager.PAYTYPE_ALIPAY ->{
                holder.imPaytypetips.setImageResource(R.mipmap.im_payzfbtag)
                holder.tvPaytypetips.setText(context.getString(R.string.ali_pay))
            }
            PayManager.PAYTYPE_WX ->{
                holder.imPaytypetips.setImageResource(R.mipmap.im_wx_paytag)
                holder.tvPaytypetips.setText(context.getString(R.string.wx_pay))
            }
            PayManager.PAYTYPE_CLOUD_FLASH_PAY ->{
                holder.imPaytypetips.setImageResource(R.mipmap.im_cloudflashpay_tag)
                holder.tvPaytypetips.setText(context.getString(R.string.tx_cloud_flash_pay))
            }

        }

    }
}