package com.wawa.wawaandroid_ep.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.wawa.baselib.utils.apollonet.BaseDataSource
import com.wawa.baselib.utils.baseadapter.BaseRecyclerViewAdapter
import com.wawa.baselib.utils.baseadapter.BaseRecyclerViewModel
import com.wawa.baselib.utils.baseadapter.BaseViewHolder
import com.wawa.wawaandroid_ep.WawaApp
import com.wawa.wawaandroid_ep.base.viewmodel.BaseVM
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlin.properties.Delegates

/**
 *作者：create by 张金 on 2021/1/13 18:15
 *邮箱：564813746@qq.com
 */
abstract class BaseFragment<V : ViewDataBinding, VM : BaseVM> : Fragment(){
    protected lateinit var binding: V
    protected lateinit var viewModel: VM
    val apolloDataSource: BaseDataSource by lazy {
        (activity?.application as WawaApp).getDataSource(WawaApp.ServiceTypes.COROUTINES)
    }
    val fragmentDisposible: CompositeDisposable by lazy {
        CompositeDisposable()
    }
    protected var viewModelId by Delegates.notNull<Int>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewRoot=inflater.inflate(getLayoutId(),container,false)
        binding= DataBindingUtil.bind<V>(viewRoot)!!
        viewModelId=initVariableId()
        viewModel=initViewModel()
        binding.setVariable(viewModelId,viewModel)
        binding.lifecycleOwner=this
        initFragmentView()
        dealNetError()
        return binding.root
    }
    abstract fun initVariableId(): Int
    abstract fun initViewModel(): VM
    abstract fun getLayoutId() : Int
    abstract fun initFragmentView()
    fun dealNetError(){
        val errorHandler=apolloDataSource.error
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleErrorInfo)
        fragmentDisposible.add(errorHandler)
    }

    fun handleErrorInfo(e: Throwable?){
        Toast.makeText(activity,e?.message,Toast.LENGTH_SHORT).show()
    }
    fun <VM : BaseRecyclerViewModel<*, BaseViewHolder>> RecyclerView.bindAdapter(listAdapter: BaseRecyclerViewAdapter<VM>
                                                                                 , layoutManager: RecyclerView.LayoutManager?= null){
        this.layoutManager=layoutManager
        this.adapter=listAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentDisposible?.clear()
    }
}