package com.wawa.baselib.utils.dialog

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.wawa.baselib.R

/**
 *作者：create by 张金 on 2021/3/8 16:37
 *邮箱：564813746@qq.com
 */
abstract class BaseDialogFragment : DialogFragment(){
    var dialogWidth=0
    var dialogHeight=0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view =inflater.inflate(getLayoutId(), container, false)
        initView(view)
        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = activity?.let { Dialog(it, R.style.dialog) }
        dialog?.setCanceledOnTouchOutside(true)
        dialog?.setCancelable(true)
        val window = dialog?.window
        val params = window?.attributes
        params?.gravity = Gravity.CENTER
        params?.width= dialogWidth
        params?.height= dialogHeight
        window?.attributes = params
        window?.setWindowAnimations(R.style.bottomToTopAnim)
        return dialog!!
    }


    abstract fun initDialogParams()
    abstract fun getLayoutId(): Int

    abstract fun initView(view: View)


    fun showDialog(manager: FragmentManager,tag: String){

//        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//        transaction.add(this, tag).addToBackStack(null);
//        return transaction.commitAllowingStateLoss();
        if(!isAdded){
            show(manager,tag)
        }
    }

    override fun show(
        manager: FragmentManager,
        tag: String?
    ) {
        /*if (manager.findFragmentByTag(tag) != null && manager.findFragmentByTag(tag).isAdded() && manager.findFragmentByTag(tag).isVisible()){
            return;
        }
        manager.executePendingTransactions();
        super.show(manager, tag);*/
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            if (manager.isDestroyed) return
        }
        try {
            //在每个add事务前增加一个remove事务，防止连续的add
            manager.beginTransaction().remove(this).commit()
            super.show(manager, tag)
        } catch (e: Exception) {
            //同一实例使用不同的tag会异常，这里捕获一下
            e.printStackTrace()
        }
    }
}