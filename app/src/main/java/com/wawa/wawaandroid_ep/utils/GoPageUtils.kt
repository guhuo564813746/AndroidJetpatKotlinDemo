package com.wawa.wawaandroid_ep.utils

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.robotwar.app.R
import com.wawa.wawaandroid_ep.MainActivity
import com.wawa.wawaandroid_ep.activity.web.WebActivity
import com.wawa.wawaandroid_ep.activity.web.WebActivity.Companion.WEB_TITLE
import com.wawa.wawaandroid_ep.activity.web.WebActivity.Companion.WEB_URL

/**
 *作者：create by 张金 on 2021/7/2 09:44
 *邮箱：564813746@qq.com
 */
object  GoPageUtils {

    fun goPage( mContext: AppCompatActivity, action: String?,  url: String?,title: String?){
        action?.toLowerCase()
        when(action){
            "link"->{
                val intent = Intent()
                intent.setClass(mContext, WebActivity::class.java)
                intent.putExtra(WEB_TITLE, title)
                intent.putExtra(WEB_URL, url)
                mContext.startActivity(intent)
            }
            "bill"->{
                // 跳转账单
                (mContext as MainActivity).navBottom.visibility= View.GONE
                (mContext as MainActivity).navControlor.navigate(R.id.orderFragmentAction)
            }
            "address"->{
                //收货地址
            }
            "daily_mission"->{
                //每日任务
            }
            "rank" ->{
                //榜单
            }
            "vip"->{
                //VIP

            }
            "mission"->{
                //任务中心
            }
            "invited"->{
                //接受邀请
            }
            "invite"->{
                //邀请好友
            }
            "feedback"->{
                (mContext as MainActivity).navBottom.visibility= View.GONE
                (mContext as MainActivity).navControlor.navigate(R.id.feedbackFragment)
            }
            "feedback_detail" ->{
                (mContext as MainActivity).navBottom.visibility= View.GONE
                (mContext as MainActivity).navControlor.navigate(R.id.feedbackDetailFragment)
            }
            "settings"->{
                //个性设置
                (mContext as MainActivity).navBottom.visibility= View.GONE
                (mContext as MainActivity).navControlor.navigate(R.id.settingAction)
            }
            "custom_service"->{
                //在线客服
            }
            "about"->{
                //关于我们
            }
            "gift"->{

            }
            "profile"->{
                (mContext as MainActivity).navBottom.visibility=View.GONE
                (mContext as MainActivity).navControlor.navigate(R.id.goProfileFragmentAction)
            }
        }
    }
}