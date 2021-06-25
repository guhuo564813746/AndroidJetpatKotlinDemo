package com.coinhouse777.wawa.widget.popgame

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.robotwar.app.R
import com.wawa.baselib.utils.AppUtils
import com.wawa.wawaandroid_ep.view.popgame.PopGameItemBean

/**
 *作者：create by 张金 on 2021/4/26 16:38
 *邮箱：564813746@qq.com
 */
class GameSetGroupViewControlor(private val mContext: Context,
                                private val dataList: MutableList<PopGameItemBean>,
                                private  val gameSetClickCallback: GameViewClickCallback?) : PopGameAdapter.OnItemClickListener{
    private val TAG="GameSetControlor"
    private lateinit var popGameSetWindow: View
    private lateinit var lvPopGame: RecyclerView
    private var popGameAdapter: PopGameAdapter?=null
    private var popGameSetListData: MutableList<PopGameItemBean> = mutableListOf()
    init {
        popGameSetListData=dataList
        popGameSetWindow=LayoutInflater.from(mContext).inflate(R.layout.popup_gameset_lay,null)
        val gd = GradientDrawable()
        val cornors = floatArrayOf(0f, 0f, 0f, 0f, AppUtils.dp2px(mContext,12f).toFloat(), AppUtils.dp2px(mContext,12f).toFloat(), AppUtils.dp2px(mContext,12f).toFloat(), AppUtils.dp2px(mContext,12f).toFloat())
        gd.cornerRadii = cornors
        gd.setColor(Color.parseColor("#B2000000"))
        popGameSetWindow.background=gd
        lvPopGame=popGameSetWindow.findViewById(R.id.lv_popgame)
        lvPopGame.layoutManager=LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false)
        popGameAdapter=PopGameAdapter(mContext,getPopGameSetListData())
        popGameAdapter?.setOnItemClickListener(this)
        lvPopGame.adapter=popGameAdapter
        popGameSetWindow.post(Runnable {
            Log.d(TAG,"viewheight--"+popGameSetWindow.height+" "+popGameSetWindow.measuredHeight)
        })
    }

    fun setPopGameSetListData(data: MutableList<PopGameItemBean>){
        this.popGameSetListData=data
        popGameAdapter?.setDatas(popGameSetListData)
    }

    fun getPopViewHeight(): Int{
        var viewHeight=0
        popGameSetWindow.post(Runnable {
            viewHeight= popGameSetWindow.measuredHeight
        })
        return viewHeight
    }

    fun getPopGameSetListData(): MutableList<PopGameItemBean>{
        return popGameSetListData
    }

    fun getPopDialog(): View{
        return popGameSetWindow
    }

    interface GameViewClickCallback{
        fun gameSetClick(pos: Int)
    }

    override fun onItemClick(view: View, pos: Int) {
        gameSetClickCallback?.let {
            it.gameSetClick(pos)
        }
    }
}