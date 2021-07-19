package com.wawa.wawaandroid_ep.activity.viewmodule

import android.util.Log
import android.view.View
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import com.apollographql.apollo.RoomInfoQuery
import com.wawa.baselib.utils.logutils.LogUtils
import com.wawa.baselib.utils.viewmodel.BaseVM
import com.wawa.wawaandroid_ep.sound.SoundManager

/**
 *作者：create by 张金 on 2021/2/3 14:41
 *邮箱：564813746@qq.com
 */
open class BaseGameViewModel : BaseVM(){
    private val TAG="BaseGameViewModel"
    var topUpTipsDialogShow= MutableLiveData<Boolean>()
    var userDataGroupVisibility = ObservableInt(View.VISIBLE)
    var fee = ObservableField("")
    var coin=ObservableField("")
    var points=ObservableField("")
    var roomUserAmountText = ObservableField<String>("")
    var guestPanelVisibility= ObservableInt(View.VISIBLE)
    var queueCount = ObservableField<String>("")
    var gamePanelVisibility =ObservableInt(View.GONE)
    var countdownText = ObservableField<String>("")
    var countdownVisibility= ObservableInt(View.GONE)
    var roomInfoData=MutableLiveData<RoomInfoQuery.List>()
    var startGameBtnRes = ObservableField<Int>()
    var playerName=ObservableField("尊贵的用户666")
    var playerHonnorName= ObservableField("")
    var playerGameViewVisibility= ObservableInt(View.GONE)
    var soundManager: SoundManager?= null
    init {
        startGameBtnRes.addOnPropertyChangedCallback(object: Observable.OnPropertyChangedCallback(){
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                LogUtils.d(TAG,"startGameBtnRes--")
            }
        })
    }


    override fun onCleared() {
        super.onCleared()
        Log.d(TAG,"onCleared--")
        if (soundManager != null){
            soundManager?.releaseSound()
        }
    }

}