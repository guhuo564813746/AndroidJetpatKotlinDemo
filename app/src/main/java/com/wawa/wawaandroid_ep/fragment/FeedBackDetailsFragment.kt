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
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.constant.ImageProvider
import com.github.dhaval2404.imagepicker.listener.DismissListener
import com.robotwar.app.BR
import com.robotwar.app.R
import com.robotwar.app.databinding.FeedbackDetailsFmLayBinding
import com.wawa.baselib.utils.apollonet.MutationCallback
import com.wawa.baselib.utils.baseadapter.imp.ArrayListAdapter
import com.wawa.wawaandroid_ep.WawaApp
import com.wawa.wawaandroid_ep.adapter.viewmodel.FeedbackCommentItemVM
import com.wawa.wawaandroid_ep.base.fragment.BaseFragment
import com.wawa.wawaandroid_ep.fragment.viewmodule.FeedBackDetailsFmVm
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File

/**
 *作者：create by 张金 on 2021/7/2 14:42
 *邮箱：564813746@qq.com
 */
class FeedBackDetailsFragment : BaseFragment<FeedbackDetailsFmLayBinding,FeedBackDetailsFmVm>(){
    val TAG="FeedBackDetailsFragment"
    var feedbackId=0
    val CHOOSE_IMG_CODE=100
    var imgFile: File?=null
    var fbCommentAdapter= ArrayListAdapter<FeedbackCommentQuery.List>()
    companion object{
        val FEEDBACK_ID="FEEDBACK_ID"
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): FeedBackDetailsFmVm {
        val feedBackDetailsFmVm: FeedBackDetailsFmVm by viewModels()
        return feedBackDetailsFmVm
    }

    override fun getLayoutId(): Int {
        return R.layout.feedback_details_fm_lay
    }

    override fun initFragmentView() {
        var imBack=binding.viewTitle.findViewById<ImageView>(R.id.im_back)
        imBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.viewTitle.findViewById<TextView>(R.id.title).setText(getString(R.string.tx_feedback_detail_tips))
        arguments?.let {
            feedbackId=it.getInt(FEEDBACK_ID)

        }
        viewModel.clicks.observe(this, Observer {
            when(it.id){
                R.id.rl_addphoto ->{
                    chooseImg()
                }
                R.id.tv_commitfeedback ->{
                    if (imgFile == null){
                        addFbCommentWithNoImg()
                    }else{
                        upLoadFbImg()
                    }
                }
            }
        })
        val manager=LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
        binding.lvFeedbackDetail.bindAdapter(fbCommentAdapter,manager)
        getFbDetailBean()
        getDetailFbCommentList()
    }

    fun getFbDetailBean(){
        val fbDetailBeanSuccessDp=apolloDataSource.feedbackListWithId
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleFbDetailBean)
        fragmentDisposible.add(fbDetailBeanSuccessDp)
        apolloDataSource.getFeedbackWithId(1,feedbackId)
    }

    fun handleFbDetailBean(data: FeedBackListWithIdQuery.Feedback){
        data?.list()?.let {
            if (it.size >0){
                binding.tvFeedbackTitle.setText(it.get(0).fragments()?.feedback()?.content())
                when (it.get(0).fragments()?.feedback()?.status()) {
                    0 -> {                          //未处理
                        binding.tvStatus.setTextColor(resources.getColor(R.color.base_blue))
                        binding.tvStatus.setText("未处理")
                    }
                    1 -> {                            //已处理
                        binding.tvStatus.setTextColor(resources.getColor(R.color.normal_green))
                        binding.tvStatus.setText("已处理")
                    }
                    2 -> {                            //处理中
                        binding.tvStatus.setTextColor(resources.getColor(R.color.yellow))
                        binding.tvStatus.setText("处理中")
                    }
                }
            }
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

    fun upLoadFbImg(){
        val upLoadFbImg= UploadFeedbackPictureMutation(FileUpload("image/*", imgFile?.absolutePath))
        WawaApp.apolloClient.mutate(upLoadFbImg)
            .enqueue(object: MutationCallback<UploadFeedbackPictureMutation.Data>(){
                override fun onResponse(response: Response<UploadFeedbackPictureMutation.Data>) {
                    super.onResponse(response)
                    response?.data?.uploadFeedbackPicture()?.url()?.let {
                        addFbComment(it)
                    }
                }
            })
    }

    fun addFbCommentWithNoImg(){
        val fbCommentAddWithNoImg= FeedbackCommentAddNoImgMutation(feedbackId,binding.etFeedbackcontent.text.toString().trim())
        WawaApp.apolloClient.mutate(fbCommentAddWithNoImg)
            .enqueue(object: MutationCallback<FeedbackCommentAddNoImgMutation.Data>(){
                override fun onResponse(response: Response<FeedbackCommentAddNoImgMutation.Data>) {
                    super.onResponse(response)
                    apolloDataSource.getFbCommentList(feedbackId)
                }
            })

    }
    fun addFbComment(url: String){
        val fbCommentAddMutation= FeedbackCommentAddMutation(url,feedbackId,binding.etFeedbackcontent.text.toString().trim())
        WawaApp.apolloClient.mutate(fbCommentAddMutation)
            .enqueue(object: MutationCallback<FeedbackCommentAddMutation.Data>(){
                override fun onResponse(response: Response<FeedbackCommentAddMutation.Data>) {
                    super.onResponse(response)
                    apolloDataSource.getFbCommentList(feedbackId)
                }
            })
    }

    fun  getDetailFbCommentList(){
        val fbDetails=apolloDataSource.feedbackCommentListData
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handlerFeedBackCommentList)
        fragmentDisposible.add(fbDetails)
        apolloDataSource.getFbCommentList(feedbackId)
    }

    fun handlerFeedBackCommentList(data: FeedbackCommentQuery.Data){
        Log.d(TAG,"handlerFeedBackCommentList--")
        data?.feedbackComment()?.list()?.let {
            if (it.size > 0){
                for (i in it){
                    val vm=FeedbackCommentItemVM()
                    vm.model=i
                    fbCommentAdapter.add(vm)
                }
            }
        }

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
}