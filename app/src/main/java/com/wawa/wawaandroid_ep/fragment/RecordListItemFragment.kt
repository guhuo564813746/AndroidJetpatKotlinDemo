package com.wawa.wawaandroid_ep.fragment

import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollographql.apollo.ChargeOrderListQuery
import com.apollographql.apollo.GameRecordListQuery
import com.apollographql.apollo.UserCoinLogListQuery
import com.blankj.utilcode.util.SizeUtils
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.wawa.baselib.utils.apollonet.BaseDataSource
import com.wawa.baselib.utils.logutils.LogUtils
import com.wawa.wawaandroid_ep.BR
import com.wawa.wawaandroid_ep.R
import com.wawa.wawaandroid_ep.WawaApp
import com.wawa.wawaandroid_ep.adapter.records.ChargesRecordsAdapter
import com.wawa.wawaandroid_ep.adapter.records.CoinsRecordsAdapter
import com.wawa.wawaandroid_ep.adapter.records.GamesRecordsAdapter
import com.wawa.wawaandroid_ep.base.fragment.BaseFragment
import com.wawa.wawaandroid_ep.databinding.RecordlistitemFmLayBinding
import com.wawa.wawaandroid_ep.fragment.viewmodule.RecordListItemFragmentVM
import com.wawa.wawaandroid_ep.view.recycleview.SpacesItemDecoration
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 *作者：create by 张金 on 2021/3/3 17:53
 *邮箱：564813746@qq.com
 */
class RecordListItemFragment : BaseFragment<RecordlistitemFmLayBinding,RecordListItemFragmentVM>(),
    OnRefreshListener, OnLoadMoreListener {
    companion object{
        val TAG="RecordListItemFragment"
        val GAME_RECORD="game_record"
        val COINS_RECORD="coins_record"
        val DIAMON_RECORD="diamon_record"
        val CHARGE_RECORD="charge_record"
    }
    var recordItemType: String=GAME_RECORD
    var errDp: Disposable?= null
    var mPage=1
    var gameRecordsAdapter: GamesRecordsAdapter?=null
    var coinRecordsAdapter: CoinsRecordsAdapter?=null
    var chargeRecordsAdapter: ChargesRecordsAdapter?= null
    private val compositeDisposable = CompositeDisposable()
    private val dataSource: BaseDataSource by lazy {
        (activity?.application as WawaApp).getDataSource(WawaApp.ServiceTypes.COROUTINES)
    }
    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): RecordListItemFragmentVM {
        val recordListItemFragmentVM: RecordListItemFragmentVM by viewModels()
        return recordListItemFragmentVM
    }

    override fun getLayoutId(): Int {
        return R.layout.recordlistitem_fm_lay
    }

    override fun initFragmentView() {
        arguments?.let { recordItemType= requireArguments().getString(TAG,GAME_RECORD) }
        binding.refreshLayout.setOnRefreshListener(this)
        binding.refreshLayout.setOnLoadMoreListener(this)
        binding.lvRecord.layoutManager=LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        binding.lvRecord.addItemDecoration(SpacesItemDecoration(SizeUtils.dp2px(20f)))
        binding.lvRecord.setHasFixedSize(true)
        gameRecordsAdapter= activity?.let { GamesRecordsAdapter(it) }
        coinRecordsAdapter=activity?.let { CoinsRecordsAdapter(it) }
        chargeRecordsAdapter=activity?.let { ChargesRecordsAdapter(it) }
        when(recordItemType){
            GAME_RECORD ->{
                binding.lvRecord.adapter=gameRecordsAdapter
            }
            COINS_RECORD ->{
                binding.lvRecord.adapter=coinRecordsAdapter
            }
            CHARGE_RECORD ->{
                binding.lvRecord.adapter=chargeRecordsAdapter
            }
        }
        errDp=dataSource.error
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleError)
        initRecordListData()
    }

    fun handleError(e: Throwable?){
        LogUtils.d(TAG,e?.message.toString())
    }

    fun initRecordListData(){
        when(recordItemType){
            GAME_RECORD ->{ getGameRecordList() }
            COINS_RECORD ->{ getCoinsRecordList() }
            DIAMON_RECORD ->{ getDiamondRecordList() }
            CHARGE_RECORD ->{ getChargeRecordList() }
        }
    }

    fun getGameRecordList(){
        val gameRecordListDp=dataSource.gameRecordList
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleSuccessGameRecords)
        compositeDisposable.add(gameRecordListDp)
        errDp?.let { compositeDisposable.add(it) }
        dataSource.getGameRecordList(mPage)
    }

    fun getCoinsRecordList(){
        val coinsListDp=dataSource.userCoinLogList
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleSuccessCoinsRecords)
        compositeDisposable.add(coinsListDp)
        errDp?.let { compositeDisposable.add(it) }
        dataSource.getUserCoinLogList(mPage)

    }

    fun getDiamondRecordList(){

    }

    fun getChargeRecordList(){
        val chargeRecordListDp=dataSource.chargeOrderList
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleSuccessChargeRecords)
        compositeDisposable.add(chargeRecordListDp)
        errDp?.let { compositeDisposable.add(it) }
        dataSource.getChargeOrderList(mPage)
    }

    fun handleSuccessGameRecords(list: List<GameRecordListQuery.List>){
        LogUtils.d(TAG,"handleSuccessGameRecords--${list.size}")
        if (mPage==1){
            if (binding.refreshLayout.isRefreshing){
                binding.refreshLayout.finishRefresh()
            }
            list?.let {
                binding.noData.visibility=View.GONE
                gameRecordsAdapter?.list=it
                gameRecordsAdapter?.notifyDataSetChanged()
            }
            if (list== null || list?.size==0){
                binding.noData.visibility=View.VISIBLE
                gameRecordsAdapter?.notifyDataSetChanged()
            }
        }else{
            if (binding.refreshLayout.isLoading){
                binding.refreshLayout.finishLoadMore()
            }
            list?.let{
                gameRecordsAdapter?.list = gameRecordsAdapter?.list?.plus(it)
                LogUtils.d(TAG,"addSize--${gameRecordsAdapter?.list?.size}")
                gameRecordsAdapter?.notifyItemRangeInserted(gameRecordsAdapter?.list?.size!! - it.size,it.size)
//                gameRecordsAdapter?.notifyDataSetChanged()
            }
            if (list==null || list?.size==0){
                Toast.makeText(activity,"no more data",Toast.LENGTH_SHORT).show()
                binding.refreshLayout.setNoMoreData(true)
            }
        }
    }

    fun handleSuccessCoinsRecords(list: List<UserCoinLogListQuery.List>){
        if (mPage==1){
            if (binding.refreshLayout.isRefreshing){
                binding.refreshLayout.finishRefresh()
            }
            list?.let {
                binding.noData.visibility=View.GONE
                coinRecordsAdapter?.list=it
                coinRecordsAdapter?.notifyDataSetChanged()
            }
            if (list== null || list?.size==0){
                binding.noData.visibility=View.VISIBLE
                coinRecordsAdapter?.notifyDataSetChanged()
            }
        }else{
            if (binding.refreshLayout.isLoading){
                binding.refreshLayout.finishLoadMore()
            }
            list?.let{
                coinRecordsAdapter?.list=coinRecordsAdapter?.list?.plus(it)
                coinRecordsAdapter?.notifyItemRangeInserted(coinRecordsAdapter?.list?.size!! -it.size,it.size)
            }
            if (list==null || list?.size==0){
                Toast.makeText(activity,"no more data",Toast.LENGTH_SHORT).show()
                binding.refreshLayout.finishLoadMoreWithNoMoreData()
            }
        }
    }

    fun handleSuccessDiamondRecords(){

    }

    fun handleSuccessChargeRecords(list: List<ChargeOrderListQuery.List>){
        if (mPage==1){
            if (binding.refreshLayout.isRefreshing){
                binding.refreshLayout.finishRefresh()
            }
            list?.let {
                binding.noData.visibility=View.GONE
                chargeRecordsAdapter?.list=it
                chargeRecordsAdapter?.notifyDataSetChanged()
            }
            if (list== null || list?.size==0){
                binding.noData.visibility=View.VISIBLE
                chargeRecordsAdapter?.notifyDataSetChanged()
            }
        }else{
            if (binding.refreshLayout.isLoading){
                binding.refreshLayout.finishLoadMore()
            }
            list?.let{
                chargeRecordsAdapter?.list=chargeRecordsAdapter?.list?.plus(it)
                chargeRecordsAdapter?.notifyItemRangeInserted(chargeRecordsAdapter?.list?.size!! -it.size,it.size)
            }
            if (list==null || list?.size==0){
                Toast.makeText(activity,"no more data",Toast.LENGTH_SHORT).show()
                binding.refreshLayout.finishLoadMoreWithNoMoreData()
            }
        }
    }

    fun updataData(){
        when(recordItemType){
            GAME_RECORD ->{ dataSource.getGameRecordList(mPage) }
            COINS_RECORD ->{ dataSource.getUserCoinLogList(mPage) }
            DIAMON_RECORD ->{  }
            CHARGE_RECORD ->{ dataSource.getChargeOrderList(mPage) }
        }
    }
    override fun onRefresh(refreshLayout: RefreshLayout) {
        LogUtils.d(TAG,"onRefresh--")
        mPage=1
        updataData()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        LogUtils.d(TAG,"onLoadMore--")
        mPage+=1
        updataData()
    }

}