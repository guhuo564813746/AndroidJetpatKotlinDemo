package com.wawa.wawaandroid_ep.dialog.viewmodel

import androidx.databinding.ObservableField
import com.wawa.baselib.utils.viewmodel.BaseVM

/**
 *作者：create by 张金 on 2021/7/19 18:57
 *邮箱：564813746@qq.com
 */
class GameFishBuyTimeDialogVM : BaseVM(){
    var fishBuyTime=ObservableField<String>()
    var buyTimeTips= ObservableField<String>()
}