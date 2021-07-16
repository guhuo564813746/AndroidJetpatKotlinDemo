package com.wawa.wawaandroid_ep.adapter.viewmodel

import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.apollographql.apollo.ChargeItemListQuery
import com.blankj.utilcode.util.ToastUtils
import com.robotwar.app.R
import com.wawa.baselib.utils.baseadapter.imp.ArrayListAdapter
import com.wawa.baselib.utils.baseadapter.imp.ArrayListViewModel
import com.wawa.wawaandroid_ep.MainActivity
import com.wawa.wawaandroid_ep.WawaApp
import com.wawa.wawaandroid_ep.dialog.game.PayPortDialog_V2
import com.wawa.wawaandroid_ep.pay.PayManager

/**
 *作者：create by 张金 on 2021/7/15 18:09
 *邮箱：564813746@qq.com
 */
class PayItemViewModel : ArrayListViewModel<ChargeItemListQuery.ChargeItemList>() {

    override fun onBindAdapter(adapter: ArrayListAdapter<ChargeItemListQuery.ChargeItemList>) {
        val imPayTag=viewHolder!!.view.findViewById<ImageView>(R.id.im_payTag)
        val rlTopup=viewHolder!!.view.findViewById<RelativeLayout>(R.id.rl_topup)
        val tvTopupTitle=viewHolder!!.view.findViewById<TextView>(R.id.tv_topup_title)
        val tvTopupPrice=viewHolder!!.view.findViewById<TextView>(R.id.tv_topup_price)
        model?.let {
            tvTopupTitle.setText(it.name())
            tvTopupPrice.setText("￥"+it.goods()?.fragments()?.chargeGoodsFields()?.price())
            it.chargeItemId()?.let {
                viewHolder!!.view.setOnClickListener {
                    PayPortDialog_V2.payManager?.showPayTypeDialog(
                            model?.goods()!!, model?.chargeItemId()!!,
                            object: PayManager.PayCallback{
                                override fun paySuccess(payType: Int) {
                                    (WawaApp.app)!!.setUpDataSource()
                                }

                                override fun payErr(msg: String) {
                                    ToastUtils.showShort(msg)
                                }
                            })
                    PayPortDialog_V2.shutDownDialog.value=true
                }
            }
        }
    }

    override fun getLayoutRes(): Int {
        return R.layout.paylist_item_lay
    }
}