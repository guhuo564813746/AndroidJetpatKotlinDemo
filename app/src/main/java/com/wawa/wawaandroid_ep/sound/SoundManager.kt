package com.wawa.wawaandroid_ep.sound

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.util.Log
import android.util.SparseIntArray
import com.robotwar.app.R


/**
 *作者：create by 张金 on 2021/7/16 10:58
 *邮箱：564813746@qq.com
 */
class SoundManager(private val mContext: Context) {
    val TAG="SoundManager"
    companion object{
        val GAME_START=1
        val FISH_ONHOOK=2
        val FISH_PRIZE=3
    }
    private var mSoundPool: SoundPool?=null
    private val sparseIntArray= SparseIntArray()
    init {
        val attr =
            AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME) // 设置音效使用场景
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build() // 设置音效的类型
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            mSoundPool = SoundPool.Builder().setAudioAttributes(attr) // 设置音效池的属性
                .setMaxStreams(4) // 设置最多可容纳10个音频流
                .build() //
        }else{
            mSoundPool= SoundPool(4,AudioManager.STREAM_MUSIC, 0)
        }
        sparseIntArray.put(GAME_START,mSoundPool!!.load(mContext, R.raw.game_start,1))
        sparseIntArray.put(FISH_ONHOOK,mSoundPool!!.load(mContext, R.raw.onhook,1))
        sparseIntArray.put(FISH_PRIZE,mSoundPool!!.load(mContext, R.raw.fish_prize,1))
    }

    fun playSound(key: Int){
        if (mSoundPool == null){
            return
        }
        val soundId=sparseIntArray.get(key)
        Log.d(TAG,"playSound--"+soundId)
        if (soundId > 0){
            when(key){
                FISH_ONHOOK ->{
                    mSoundPool!!.play(soundId,1f, 1f, 0, -1, 1f)
                }
                else ->{
                    mSoundPool!!.play(soundId,1f, 1f, 0, 0, 1f)
                }
            }
        }
    }

    fun stopSound(){
        if (mSoundPool != null){
            mSoundPool!!.autoPause()
        }
    }

    fun releaseSound(){
        if (mSoundPool != null){
            mSoundPool!!.release()
        }
    }
}