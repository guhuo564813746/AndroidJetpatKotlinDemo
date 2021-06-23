package com.wawa.wawaandroid_ep.adapter.charge

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.apollographql.apollo.ChargeItemListQuery
import com.blankj.utilcode.util.ToastUtils
import com.robotwar.app.R
import com.wawa.baselib.utils.pay.PayManager
import com.wawa.wawaandroid_ep.MainActivity
import com.wawa.wawaandroid_ep.fragment.ChargeFragment

/**
 *作者：create by 张金 on 2021/6/22 10:22
 *邮箱：564813746@qq.com
 */
class ChargeDialogV2Adapter(context: Context, private val payManager: PayManager) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private var chargeList: List<ChargeItemListQuery.ChargeItemList> = mutableListOf()
    private val layoutInflater: LayoutInflater
    private val mContext: Context
    //item 的显示类型，1为熊猫币，2为砖石
    var listType: Int = ChargeFragment.GOODS_TYPE_COIN
    init {
        mContext = context
        layoutInflater = LayoutInflater.from(mContext)
    }

    fun setData(list: List<ChargeItemListQuery.ChargeItemList>) {
        chargeList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val contentView: View = layoutInflater.inflate(R.layout.charge_v2_item_lay, parent , false)
        return ChargeViewHolder(contentView)
    }

    override fun getItemCount(): Int {
        return chargeList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val chargeHolder: ChargeViewHolder=holder as ChargeViewHolder
        bindViewHolder(chargeHolder,position)
    }

    private fun bindViewHolder(holder: ChargeViewHolder, position: Int){
        holder.txChargeTitle.setText(chargeList[position].goods()?.fragments()?.chargeGoodsFields()?.name())
        holder.txChargePrice.setText("￥${chargeList[position].goods()?.fragments()?.chargeGoodsFields()?.price().toString()}")
        holder.txChargeSubTitle.setText(chargeList[position].goods()?.fragments()?.chargeGoodsFields()?.shortDesc())
    }

    private inner class ChargeViewHolder(view: View) : RecyclerView.ViewHolder(view){
        lateinit var txChargeTag: TextView
        lateinit var txChargeTitle: TextView
        lateinit var txChargeSubTitle: TextView
        lateinit var txChargePrice: TextView
        init {
            txChargeTag.visibility=View.GONE
            view.setOnClickListener {
                chargeList.get(adapterPosition).goods()?.let {
                    chargeList.get(adapterPosition).chargeItemId()?.let { it1 ->
                        payManager.showPayTypeDialog(
                            it, it1,
                            object:PayManager.PayCallback{
                                override fun paySuccess(payType: Int) {
                                    (mContext as MainActivity).setUpDataSource()
                                }

                                override fun payErr(msg: String) {
                                    ToastUtils.showShort(msg)
                                }
                            })
                    }
                }
            }
        }
    }

}