package com.wawa.baselib.utils.dialog.adapter

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
class PayTypeListAdapter(private val context: Context,private val list: List<Int>) : RecyclerView.Adapter<PayTypeListAdapter.PayTypeListViewHolder>() {
    companion object{
        val PAYTYPE_ZFB_H5=5
    }
    var curPos=0
    inner class PayTypeListViewHolder(view: View) : RecyclerView.ViewHolder(view){
        lateinit var imPaytypeChoice: ImageView
        lateinit var imPaytypetips: ImageView
        lateinit var tvPaytypetips: TextView
        init {
            imPaytypeChoice=itemView.findViewById(R.id.im_paytype_choice)
            imPaytypetips=itemView.findViewById(R.id.im_paytypetips)
            tvPaytypetips=itemView.findViewById(R.id.tv_paytypetips)
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
            PAYTYPE_ZFB_H5 ->{
                holder.imPaytypetips.setImageResource(R.mipmap.im_payzfbtag)
                holder.tvPaytypetips.setText(context.getString(R.string.ali_pay))
            }
        }

    }
}