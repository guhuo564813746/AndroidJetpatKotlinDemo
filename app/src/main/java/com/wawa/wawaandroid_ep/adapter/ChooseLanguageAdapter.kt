package com.wawa.wawaandroid_ep.adapter

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wawa.baselib.utils.SharePreferenceUtils
import com.wawa.wawaandroid_ep.R

/**
 *作者：create by 张金 on 2021/3/3 15:24
 *邮箱：564813746@qq.com
 */
class ChooseLanguageAdapter(
    private val context: Context,
    private val lans: List<String>,
    private val lansType: List<String>,
    var curLan: String?=SharePreferenceUtils.getStr(SharePreferenceUtils.LOCALE_LAN)
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.lans_item_lay, parent, false)
        return ChooseLanguageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return lans.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ChooseLanguageViewHolder).tvLanTips.setText(
            lans.get(
                position
            )
        )

        if (!TextUtils.isEmpty(curLan) && curLan.equals(
                lansType.get(position),
                ignoreCase = true
            )
        ) {
            (holder as ChooseLanguageViewHolder).checkedLans.isSelected = true
        } else {
            (holder as ChooseLanguageViewHolder).checkedLans.isSelected = false
        }
        holder.itemView.setOnClickListener {
            if (position == 0) {
                curLan = "1"
            } else {
                curLan = lansType.get(position)
            }
            notifyDataSetChanged()
        }
    }

    private inner class ChooseLanguageViewHolder(view: View) : RecyclerView.ViewHolder(view){
        lateinit var tvLanTips: TextView
        lateinit var checkedLans: ImageView
        init {
            tvLanTips=itemView.findViewById(R.id.tv_lan_tips)
            checkedLans=itemView.findViewById(R.id.checked_lans)
        }
    }

}