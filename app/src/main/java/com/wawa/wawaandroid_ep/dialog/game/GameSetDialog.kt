package com.wawa.wawaandroid_ep.dialog.game

import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.blankj.utilcode.util.SizeUtils
import com.google.android.material.tabs.TabLayout
import com.wawa.baselib.R
import com.wawa.baselib.utils.dialog.BaseDialogFragment
import com.wawa.wawaandroid_ep.fragment.game.GameAudioSetFragment

/**
 *作者：create by 张金 on 2021/3/9 18:17
 *邮箱：564813746@qq.com
 */
class GameSetDialog : BaseDialogFragment(){
    private var titles= listOf("")/*,getString(R.string.VIDEO_SET_TIPS),getString(R.string.fapao_set_tips)*/
    private var fragmets= listOf<Fragment>()
    override fun initDialogParams() {
        dialogWidth=SizeUtils.dp2px(420f)
        dialogHeight=SizeUtils.dp2px(278f)
    }

    override fun getLayoutId(): Int {
        return R.layout.game_dialog_set_lay
    }

    override fun initView(view: View) {
        titles= listOf(getString(R.string.VOICE_SET_TIPS))
        initFragments()
        var imGamesetCancel=view.findViewById<ImageView>(R.id.im_gameset_cancel)
        var lansgameSettab=view.findViewById<TabLayout>(R.id.lansgame_settab)
        var lansgamePager=view.findViewById<ViewPager>(R.id.lansgame_pager)
        lansgamePager.adapter=GameSetPageAdapter(childFragmentManager)
        lansgameSettab.setupWithViewPager(lansgamePager)
        imGamesetCancel.setOnClickListener {
            dismissAllowingStateLoss()
        }
    }

    fun initFragments(){
        var gameAudioSetFragment=GameAudioSetFragment()
        fragmets+=gameAudioSetFragment
    }

    inner class GameSetPageAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager){
        override fun getItem(position: Int): Fragment {
            return fragmets.get(position)
        }

        override fun getCount(): Int {
            return fragmets.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titles.get(position)
        }

    }
}