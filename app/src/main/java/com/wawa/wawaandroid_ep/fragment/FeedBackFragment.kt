package com.wawa.wawaandroid_ep.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollographql.apollo.FeedBackListQuery
import com.apollographql.apollo.MalfunctionListQuery
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.constant.ImageProvider
import com.github.dhaval2404.imagepicker.listener.DismissListener
import com.robotwar.app.BR
import com.robotwar.app.R
import com.robotwar.app.databinding.FeedbackActivityLayBinding
import com.wawa.baselib.utils.baseadapter.imp.ArrayListAdapter
import com.wawa.baselib.utils.glide.loader.ImageLoader
import com.wawa.wawaandroid_ep.adapter.viewmodel.dialog.FeedbackListVM
import com.wawa.wawaandroid_ep.base.fragment.BaseFragment
import com.wawa.wawaandroid_ep.fragment.viewmodule.FeedBackFragmentVM
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File

/**
 *作者：create by 张金 on 2021/7/2 14:27
 *邮箱：564813746@qq.com
 */
class FeedBackFragment : BaseFragment<FeedbackActivityLayBinding,FeedBackFragmentVM>(),FeedbackListVM.OnItemSelectListener{
    val TAG="FeedBackFragment"
    val CHOOSE_IMG_CODE=100
    var imgFile: File?=null
    var fbContent: String?=null
    val malfunctionListAdapter= ArrayListAdapter<MalfunctionListQuery.List>()
    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): FeedBackFragmentVM {
        val feedBackFragmentVm: FeedBackFragmentVM by viewModels()
        return feedBackFragmentVm
    }

    override fun getLayoutId(): Int {
        return R.layout.feedback_activity_lay
    }

    override fun initFragmentView() {
        binding.viewTitle.findViewById<TextView>(R.id.title).setText(getString(R.string.tx_feedbacktitle))
        viewModel.clicks.observe(this, Observer {
            when(it.id){
                R.id.rl_addphoto ->{
                    chooseImg()
                }
                R.id.tv_commitfeedback ->{

                }
            }
        })
        initMalfunctionList()
        initFeedBackRecordList()
    }

    fun initMalfunctionList(){
        malfunctionListAdapter.clearData()
        val layoutManager=LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
        binding.lvFeedback.bindAdapter(malfunctionListAdapter,layoutManager)
        val malfunctionsSuccessDp=apolloDataSource.malfunctionList
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handlerMalfunctionList)
        fragmentDisposible.add(malfunctionsSuccessDp)
        apolloDataSource.getMalfunctionList("dji_ep")
    }

    fun handlerMalfunctionList(feedbackList: MalfunctionListQuery.MalfunctionList){
        Log.d(TAG,"handlerMalfunctionList--"+feedbackList.toString())
        feedbackList?.list()?.let {
            if (it.size > 0){
                for (i in 0..it.size-1){
                    val mal= FeedbackListVM()
                    mal.model=it.get(i)
                    mal.pos=i
                    mal.listener=this
                    if (i==0){
                        fbContent=mal.model?.fragments()?.malfunction()?.title()
                        mal.isSelect=true
                    }else{
                        mal.isSelect=false
                    }
                    malfunctionListAdapter.add(mal)
                }
            }
        }
    }

    fun initFeedBackRecordList(){
        val feedBackRecordList= apolloDataSource.feedbackList
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handlerFeedBackRecordList)
        fragmentDisposible.add(feedBackRecordList)
        apolloDataSource.getFeedbackList(1)
    }
    fun handlerFeedBackRecordList(feedBackList: FeedBackListQuery.Feedback){
        Log.d(TAG,"handlerFeedBackRecordList--"+feedBackList.toString())
    }

    override fun onItemSelected(content: String?) {
        fbContent=content
    }

    fun chooseImg(){
        ImagePicker.with(this) // Crop Square image
            .cropSquare()
            .compress(200)
            .setImageProviderInterceptor { imageProvider: ImageProvider ->
                Log.d("intercepter", "6666" + imageProvider.name)
            }
            .setDismissListener(object : DismissListener {
                override fun onDismiss() {
                    Log.d("ImagePicker", "Dialog Dismiss")
                }
            }) // Image resolution will be less than 512 x 512
            //            .maxResultSize(512, 512)
            .maxResultSize(512, 512)
            .start(CHOOSE_IMG_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode== CHOOSE_IMG_CODE && resultCode== Activity.RESULT_OK){
            if (data != null) {
                val result = data?.data
                imgFile = ImagePicker.getFile(data)
                imgFile?.getAbsolutePath()?.let {
                    binding.rlAddphoto.setBackground(
                        BitmapDrawable(
                            resources,
                            BitmapFactory.decodeFile(it)
                        )
                    )
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun commitFeedBack(){

    }
}