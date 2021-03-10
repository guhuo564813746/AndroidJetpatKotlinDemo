package com.wawa.wawaandroid_ep.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.UploadUserAvatarMutation
import com.apollographql.apollo.UserEditMutation
import com.apollographql.apollo.api.FileUpload
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.blankj.utilcode.util.ToastUtils
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.ImagePicker.Companion.getFile
import com.github.dhaval2404.imagepicker.constant.ImageProvider
import com.github.dhaval2404.imagepicker.listener.DismissListener
import com.robotwar.app.BR
import com.robotwar.app.R
import com.robotwar.app.databinding.ProfileFmLayBinding
import com.wawa.baselib.utils.glide.loader.ImageLoader
import com.wawa.baselib.utils.glide.utils.ImageUtil
import com.wawa.baselib.utils.logutils.LogUtils
import com.wawa.wawaandroid_ep.MainViewModule
import com.wawa.wawaandroid_ep.WawaApp
import com.wawa.wawaandroid_ep.base.fragment.BaseFragment
import com.wawa.wawaandroid_ep.fragment.viewmodule.ProfileFragmentViewModel
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import java.io.File

/**
 *作者：create by 张金 on 2021/3/2 16:57
 *邮箱：564813746@qq.com
 */
class ProfileFragment : BaseFragment<ProfileFmLayBinding,ProfileFragmentViewModel>(){
    val TAG="ProfileFragment"
    val CHOOSE_IMG_CODE=100
    var imgFile: File?=null
    var uploadImg=false
    var avatarUrl: String?=null
    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): ProfileFragmentViewModel {
        val profileFragmentViewModel: ProfileFragmentViewModel by viewModels()
        return profileFragmentViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.profile_fm_lay
    }

    override fun initFragmentView() {
        binding.tvNickname.setOnClickListener {
            tvNameChange()
        }
        var tvTitle=binding.titleView.findViewById<TextView>(R.id.title)
        var imBack=binding.titleView.findViewById<ImageView>(R.id.im_back)
        imBack.setOnClickListener {
            findNavController().popBackStack()
        }
        tvTitle.setText(getString(R.string.profile_edit))
        binding.etName.setText(MainViewModule.userData?.fragments()?.userFragment()?.nickName())
        binding.tvNickname.setText(MainViewModule.userData?.fragments()?.userFragment()?.nickName())
        avatarUrl=MainViewModule.userData?.fragments()?.userFragment()?.avatarThumb()
        ImageLoader.with(activity)
            .url(avatarUrl)
//                .placeHolder(R.mipmap.ic_launcher)
            .rectRoundCorner(ImageUtil.dip2px(30f), RoundedCornersTransformation.CornerType.ALL)
            .into(binding.imUserHead)
        binding.rlHeadIm.setOnClickListener {
            chooseImg()
        }
        binding.tvSave.setOnClickListener {
            clickSave()
        }
    }

    fun tvNameChange() {
        binding.tvNickname.setVisibility(View.GONE)
        binding.etName.setVisibility(View.VISIBLE)
        binding.etName.setFocusable(true)
        binding.etName.setFocusableInTouchMode(true)
        binding.etName.requestFocus()
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
    }

    fun upLoadImg(){
       var  upLoadImgMutation = UploadUserAvatarMutation(FileUpload("image/*", imgFile?.absolutePath))
        WawaApp.apolloClient.mutate(upLoadImgMutation)
            .enqueue(object: ApolloCall.Callback<UploadUserAvatarMutation.Data>(){
                override fun onFailure(e: ApolloException) {
                    activity?.runOnUiThread {
                        ToastUtils.showShort(e?.message)
                    }
                }

                override fun onResponse(response: Response<UploadUserAvatarMutation.Data>) {
                    LogUtils.d(TAG,"upLoadImg--${response.data?.toString()+response?.errors.toString()}")
                    avatarUrl=response?.data?.uploadUserAvatar()?.url()
                    saveUserData()
                }
            })
    }

    fun saveUserData(){
        var nickName=binding.etName.text.toString().trim()
        var saveUserMutation=UserEditMutation(MainViewModule.userData?.fragments()?.userFragment()?.email().toString(),MainViewModule.userData?.fragments()?.userFragment()?.phoneNo().toString(),nickName,avatarUrl.toString())
        WawaApp.apolloClient.mutate(saveUserMutation)
            .enqueue(object: ApolloCall.Callback<UserEditMutation.Data>(){
                override fun onFailure(e: ApolloException) {
                    activity?.runOnUiThread {
                        ToastUtils.showShort(e?.message)
                    }
                }

                override fun onResponse(response: Response<UserEditMutation.Data>) {
                    activity?.runOnUiThread {
                        binding.tvNickname.setVisibility(View.VISIBLE)
                        binding.tvNickname.setText(response?.data?.userEdit()?.nickName())
                        binding.etName.setVisibility(View.GONE)
                        ToastUtils.showShort(getString(R.string.userinfo_change_success_tips))
                    }
                }
            } )
    }

    fun clickSave(){
        if (uploadImg){
            upLoadImg()
        }else{
            saveUserData()
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
        if (requestCode== CHOOSE_IMG_CODE && resultCode== RESULT_OK){
            uploadImg=true
            if (data != null) {
                val result = data?.data
                imgFile = getFile(data)
                binding.imUserHead.setImageBitmap(null)
                binding.imUserHead.setImageDrawable(null)
                binding.imUserHead.setImageURI(null)
                ImageLoader.with(activity)
                    .file(imgFile)
                    .asCircle()
                    .into(binding.imUserHead)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}