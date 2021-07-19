package com.wawa.wawaandroid_ep.dialog.game

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.robotwar.app.BR
import com.robotwar.app.R
import com.robotwar.app.databinding.GameSettingDialogLayBinding
import com.wawa.baselib.utils.AppUtils
import com.wawa.baselib.utils.dialog.BaseVMDialogFragment
import com.wawa.wawaandroid_ep.adapter.viewpager.BaseViewPagerAdapter
import com.wawa.wawaandroid_ep.dialog.game.fragment.AudioSetFragment
import com.wawa.wawaandroid_ep.dialog.game.fragment.GameOpSetFragment
import com.wawa.wawaandroid_ep.dialog.game.fragment.VideoSetFragment
import com.wawa.wawaandroid_ep.dialog.viewmodel.GameSettingVM

/**
 *作者：create by 张金 on 2021/7/13 15:12
 *邮箱：564813746@qq.com
 */
class GameSettingDialog : BaseVMDialogFragment<GameSettingDialogLayBinding,GameSettingVM>(){
    companion object{
        val confirmSubmit=MutableLiveData<Boolean>()
    }
    override fun initDialogParams() {
        dialogWidth=AppUtils.dp2px(activity,290f)
        dialogHeight=AppUtils.dp2px(activity,440f)
    }

    override fun getLayoutId(): Int {
        return R.layout.game_setting_dialog_lay
    }


    override fun initView(view: View) {
        val gameSetViewPagerAdapter= BaseViewPagerAdapter(childFragmentManager,initVpFragments(),getViewPagerTitles())
        binding.setViewpager.adapter=gameSetViewPagerAdapter
        binding.setTab.setupWithViewPager(binding.setViewpager)
        viewModel.clicks.observe(this, Observer {
            when(it.id){
                R.id.bt_set ->{
                    confirmSubmit.value=true
                }
            }
        })
    }

    fun initVpFragments(): List<Fragment>{
        val fragments=ArrayList<Fragment>()
        fragments.add(AudioSetFragment())
        fragments.add(VideoSetFragment())
        fragments.add(GameOpSetFragment())
        return fragments
    }

    fun getViewPagerTitles(): List<String>{
        val titles= mutableListOf<String>()
        titles.add(getString(R.string.VOICE_SET_TIPS))
        titles.add(getString(R.string.VIDEO_SET_TIPS))
        titles.add(getString(R.string.tx_operation))
        return titles
    }


    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): GameSettingVM {
        val gameSettingVM: GameSettingVM by viewModels()
        return gameSettingVM
    }
}