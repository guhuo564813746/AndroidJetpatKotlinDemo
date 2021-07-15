package com.wawa.wawaandroid_ep.dialog.game

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.apollographql.apollo.ChargeItemListQuery
import com.robotwar.app.BR
import com.robotwar.app.R
import com.robotwar.app.databinding.PayDialogLayBinding
import com.wawa.baselib.utils.AppUtils
import com.wawa.baselib.utils.apollonet.BaseDataSource
import com.wawa.baselib.utils.dialog.BaseVMDialogFragment
import com.wawa.wawaandroid_ep.pay.PayManager
import com.wawa.wawaandroid_ep.WawaApp
import com.wawa.wawaandroid_ep.adapter.charge.ChargeDialogAdapter
import com.wawa.wawaandroid_ep.dialog.viewmodel.PayDialogViewModel
import com.wawa.wawaandroid_ep.fragment.ChargeFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 *作者：create by 张金 on 2021/7/9 15:49
 *邮箱：564813746@qq.com
 */
class PayPortDialog : BaseVMDialogFragment<PayDialogLayBinding, PayDialogViewModel>() {
    companion object{
        val TAG="PayPortDialog"
    }
    private var chargeDialogAdapter: ChargeDialogAdapter?= null
    lateinit var payManager: PayManager
    val apolloDataSource: BaseDataSource by lazy {
        (activity?.application as WawaApp).getDataSource(WawaApp.ServiceTypes.COROUTINES)
    }

    override fun initDialogParams() {
        dialogWidth=AppUtils.dp2px(activity,290f)
        dialogHeight=AppUtils.dp2px(activity,451f)
    }

    override fun getLayoutId(): Int {
        return R.layout.pay_dialog_lay
    }

    override fun initView(view: View) {
        binding.tvGamepay.isSelected=true
        viewModel.clicks.observe(this, Observer {
            when(it.id){
                R.id.im_cancelpay ->{
                    dismissAllowingStateLoss()
                }

            }
        })
        activity?.let { payManager=
            PayManager(it, WawaApp.apolloClient)
        }
        lifecycle.addObserver(payManager)
        binding.lvCoinvsdiamenpay.setHasFixedSize(true)
        binding.lvCoinvsdiamenpay.layoutManager=
            GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        activity?.let {
            chargeDialogAdapter=ChargeDialogAdapter(it,payManager)
            binding.lvCoinvsdiamenpay.adapter=chargeDialogAdapter
        }
        initChargeData()
    }

    fun initChargeData(){
        val successChargeItemDispose= apolloDataSource.chargeItemList
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleSuccessChargeItem)
        fragmentDisposible.add(successChargeItemDispose)
        apolloDataSource.getChargeItemList()
    }

    fun handleSuccessChargeItem(chargeItemList: List<ChargeItemListQuery.ChargeItemList>){
        chargeDialogAdapter?.listType= ChargeFragment.GOODS_TYPE_COIN
        chargeDialogAdapter?.setData(chargeItemList)

    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): PayDialogViewModel {
        val payDialogVM: PayDialogViewModel by viewModels()
        return payDialogVM
    }
}