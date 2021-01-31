package com.wawa.wawaandroid_ep.fragment

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.wawa.wawaandroid_ep.MainActivity
import com.wawa.wawaandroid_ep.MainViewModule
import com.wawa.wawaandroid_ep.R
import com.wawa.wawaandroid_ep.base.fragment.BaseFragment
import com.wawa.wawaandroid_ep.databinding.FragmentMainLayBinding
import com.wawa.wawaandroid_ep.fragment.viewmodule.MainFragmentViewModel

/**
 *作者：create by 张金 on 2021/1/13 18:08
 *邮箱：564813746@qq.com
 */
class MainFragment : BaseFragment<FragmentMainLayBinding>() {
    companion object{
        val TAG="MainFragment"
    }
    val mainFragmentViewModel: MainFragmentViewModel by viewModels()
    override fun getLayoutId(): Int {
        return R.layout.fragment_main_lay
    }

    override fun initFragmentView() {
        MainViewModule.userData?.observe(this, Observer {
            Log.d(TAG,"userData")
            binding.tvMainUsername.text=it.user()?.nickName()

        })
    }

}