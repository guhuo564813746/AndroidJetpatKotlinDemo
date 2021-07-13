package com.wawa.baselib.utils.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apollographql.apollo.ChargeItemListQuery
import com.blankj.utilcode.util.SizeUtils
import com.wawa.baselib.R
import com.wawa.baselib.utils.dialog.adapter.PayTypeListAdapter
import com.wawa.baselib.utils.logutils.LogUtils
import com.wawa.baselib.utils.pay.PayManager

/**
 *作者：create by 张金 on 2021/3/4 17:46
 *邮箱：564813746@qq.com
 */
class PayTypeDialog(
                    private var callback:PayTypeCallback,private var payGoods: ChargeItemListQuery.Goods
    ) : DialogFragment(){
    val TAG="PayTypeDialog"
    lateinit var adapter: PayTypeListAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        var view =inflater.inflate(R.layout.paytype_port_dialog_lay, container, false)
        initView(view)
        return view
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = activity?.let { Dialog(it, R.style.dialog) }
        dialog?.setCanceledOnTouchOutside(true)
        dialog?.setCancelable(true)
        val window = dialog?.window
        val params = window?.attributes
        params?.gravity = Gravity.CENTER
        params?.width=SizeUtils.dp2px(290f)
        params?.height=SizeUtils.dp2px(400f)
        window?.attributes = params
        window?.setWindowAnimations(R.style.bottomToTopAnim)
        return dialog!!
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        LogUtils.d(TAG,"onCancel--")
        if (callback != null){
            callback.payTypeCancel()
        }
    }
    fun initView(view: View){
        val lvPaytypeitem=view.findViewById<RecyclerView>(R.id.lv_paytypeitem)
        val tvPaytips=view.findViewById<TextView>(R.id.tv_paytips)
        val imCancelpay=view.findViewById<ImageView>(R.id.im_cancelpay)
        val tvPaybuynow=view.findViewById<TextView>(R.id.tv_paybuynow)
        val tvPaytypeTips=view.findViewById<TextView>(R.id.tv_paytype_tips)
        lvPaytypeitem.setHasFixedSize(true)
        lvPaytypeitem.layoutManager=LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)

        activity?.let {
            adapter=PayTypeListAdapter(it, listOf(PayManager.PAYTYPE_ALIPAY,PayManager.PAYTYPE_WX,PayManager.PAYTYPE_CLOUD_FLASH_PAY))
            lvPaytypeitem.adapter=adapter }
        tvPaytips.setText(payGoods.fragments()?.chargeGoodsFields()?.name())
        tvPaytypeTips.setText(payGoods.fragments()?.chargeGoodsFields()?.detailDesc())
        imCancelpay.setOnClickListener {
            if (callback != null){
                callback.payTypeCancel()
            }
            dismissAllowingStateLoss()
        }
        tvPaybuynow.setOnClickListener {
            if (callback != null){
                adapter?.list?.let {
                    callback.payTypeConfirm(it.get(adapter.curPos))
                }
            }
            dismissAllowingStateLoss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    interface PayTypeCallback{
        fun payTypeConfirm(payType: Int)
        fun payTypeCancel()
    }
}