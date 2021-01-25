package com.wawa.wawaandroid_ep.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

/**
 *作者：create by 张金 on 2021/1/13 18:15
 *邮箱：564813746@qq.com
 */
abstract class BaseFragment<V : ViewDataBinding> : Fragment(){
    protected lateinit var binding: V
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
        initFragmentView()
        return binding.root
    }

    abstract fun getLayoutId() : Int
    abstract fun initFragmentView()
}