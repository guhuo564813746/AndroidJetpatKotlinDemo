package com.wawa.baselib.utils.dialog

import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.blankj.utilcode.util.SizeUtils
import com.google.android.material.tabs.TabLayout
import com.wawa.baselib.R

/**
 *作者：create by 张金 on 2021/3/9 18:17
 *邮箱：564813746@qq.com
 */
class GameSetDialog : BaseDialogFragment(){
    private var titles= listOf(getString(R.string.VOICE_SET_TIPS),getString(R.string.VIDEO_SET_TIPS),getString(R.string.fapao_set_tips))
    private var fragmets= listOf<Fragment>()
    override fun initDialogParams() {
        dialogWidth=SizeUtils.dp2px(420f)
        dialogHeight=SizeUtils.dp2px(278f)
    }

    override fun getLayoutId(): Int {
        return R.layout.game_dialog_set_lay
    }

    override fun initView(view: View) {
        var imGamesetCancel=view.findViewById<ImageView>(R.id.im_gameset_cancel)
        var lansgameSettab=view.findViewById<TabLayout>(R.id.lansgame_settab)
        var lansgamePager=view.findViewById<ViewPager>(R.id.lansgame_pager)

    }

    fun initFragments(){

    }
}