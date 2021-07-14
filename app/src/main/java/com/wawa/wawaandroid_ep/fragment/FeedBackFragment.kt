package com.wawa.wawaandroid_ep.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollographql.apollo.*
import com.apollographql.apollo.api.FileUpload
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.fragment.Feedback
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.DeviceUtils
import com.blankj.utilcode.util.ToastUtils
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.constant.ImageProvider
import com.github.dhaval2404.imagepicker.listener.DismissListener
import com.robotwar.app.BR
import com.robotwar.app.R
import com.robotwar.app.databinding.FeedbackActivityLayBinding
import com.wawa.baselib.utils.apollonet.MutationCallback
import com.wawa.baselib.utils.baseadapter.imp.ArrayListAdapter
import com.wawa.baselib.utils.glide.loader.ImageLoader
import com.wawa.wawaandroid_ep.WawaApp
import com.wawa.wawaandroid_ep.adapter.viewmodel.FeedbackRecordItemCommitVM
import com.wawa.wawaandroid_ep.adapter.viewmodel.FeedbackRecordItemVM
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
    val fbRecordListAdapter = ArrayListAdapter<FeedBackListQuery.List>()
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
        var imBack=binding.viewTitle.findViewById<ImageView>(R.id.im_back)
        imBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.viewTitle.findViewById<TextView>(R.id.title).setText(getString(R.string.tx_feedbacktitle))
        viewModel.clicks.observe(this, Observer {
            when(it.id){
                R.id.rl_addphoto ->{
                    chooseImg()
                }
                R.id.tv_commitfeedback ->{
                    if (binding.etFeedbackcontent.text.toString().trim().isNullOrEmpty()){
                        ToastUtils.showShort("请输入反馈内容")
                    }else{
                        if (imgFile == null){
                            commitFbWithNoImg()
                        }else{
                            upLoadFbImg()
                        }
                    }
                }
            }
        })
        initMalfunctionList()
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
        apolloDataSource.getMalfunctionList("")
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
                        mal.model?.fragments()?.malfunction()?.malfunctionId()?.let {

                        }
                        mal.isSelect=true
                    }else{
                        mal.isSelect=false
                    }
                    malfunctionListAdapter.add(mal)
                }
            }
        }
        initFeedBackRecordList()
    }

    fun initFeedBackRecordList(){
        val layoutManager= LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
        binding.lvFeedbackRecord.bindAdapter(fbRecordListAdapter,layoutManager)
        val feedBackRecordList= apolloDataSource.feedbackList
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handlerFeedBackRecordList)
        fragmentDisposible.add(feedBackRecordList)
        apolloDataSource.getFeedbackList(1,0)
    }
    fun handlerFeedBackRecordList(feedBackList: FeedBackListQuery.Feedback){
        Log.d(TAG,"handlerFeedBackRecordList--"+feedBackList.toString())
        fbRecordListAdapter.clearData()
        feedBackList?.list()?.let {
            if (it.size > 0){
                for (i in 0..it.size-1){
                    val fbRecordItemVM=FeedbackRecordItemVM()
                    fbRecordItemVM.model=it.get(i)
                    fbRecordListAdapter.add(i,fbRecordItemVM)
                }
            }
        }
    }

    override fun onItemSelected(content: String?,fbId: String?) {
        fbContent=content
        fbId?.let {

        }
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

    fun upLoadFbImg(){
        val upLoadFbImg= UploadFeedbackPictureMutation(FileUpload("image/*", imgFile?.absolutePath))
        WawaApp.apolloClient.mutate(upLoadFbImg)
            .enqueue(object: MutationCallback<UploadFeedbackPictureMutation.Data>(){
                override fun onResponse(response: Response<UploadFeedbackPictureMutation.Data>) {
                    super.onResponse(response)
                    response?.data?.uploadFeedbackPicture()?.url()?.let {
                        commitFeedBack(it)
                    }
                }
            })
    }

    fun commitFbWithNoImg(){
        val commitFbWithNoImgMutation=FeedbackAddWithNoImgMutation(fbContent!!,AppUtils.getAppVersionName(activity?.packageName),DeviceUtils.getModel(),binding.etFeedbackcontent.text.toString().trim())
        WawaApp.apolloClient.mutate(commitFbWithNoImgMutation)
            .enqueue(object : MutationCallback<FeedbackAddWithNoImgMutation.Data>(){

                override fun onResponse(response: Response<FeedbackAddWithNoImgMutation.Data>) {
                    super.onResponse(response)
                    response?.data?.feedbackAdd()?.feedbackId()?.let {
                        apolloDataSource.getFeedbackList(1,it.toInt())
                        ToastUtils.showShort(getString(R.string.tx_feedback_success_tips))
                    }
                }
            })
    }

    fun commitFeedBack(url: String){
        val commitFeedbackMutation=FeedbackAddMutation(fbContent!!,AppUtils.getAppVersionName(activity?.packageName),DeviceUtils.getModel(),binding.etFeedbackcontent.text.toString().trim(),url)
        WawaApp.apolloClient.mutate(commitFeedbackMutation)
            .enqueue(object : MutationCallback<FeedbackAddMutation.Data>(){

                override fun onResponse(response: Response<FeedbackAddMutation.Data>) {
                    super.onResponse(response)
                    response?.data?.feedbackAdd()?.feedbackId()?.let {
                        apolloDataSource.getFeedbackList(1,it.toInt())
                        ToastUtils.showShort(getString(R.string.tx_feedback_success_tips))
                    }
                }
            })
    }
}