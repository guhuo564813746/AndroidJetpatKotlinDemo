package com.wawa.wawaandroid_ep

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.apollographql.apollo.ConfigDataQuery
import com.apollographql.apollo.UserQuery
import com.blankj.utilcode.util.ToastUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wawa.baselib.utils.SharePreferenceUtils
import com.wawa.baselib.utils.apollonet.BaseDataSource
import com.wawa.wawaandroid_ep.base.activity.BaseActivity
import com.wawa.wawaandroid_ep.databinding.ActivityMainBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainActivity : BaseActivity<ActivityMainBinding,MainViewModule>() {
    lateinit var navBottom: BottomNavigationView
    private val compositeDisposable = CompositeDisposable()
    val dataSource: BaseDataSource by lazy {
        (application as WawaApp).getDataSource(WawaApp.ServiceTypes.COROUTINES)
    }
    companion object{
        private val TAG: String="MainActivity"
    }
    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        window.setBackgroundDrawableResource(R.color.white)
        navBottom=binding.navMainBottom
        navBottom.itemIconTintList=null
        val navHostFragment=supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        val navControlor=navHostFragment.navController
        navBottom.setupWithNavController(navControlor)
        navControlor.addOnDestinationChangedListener{
            controller, destination, arguments ->
            Log.d(TAG,"addOnDestinationChangedListener--")
            when(destination.id){
                R.id.mainFragment -> Log.d(TAG,"main")
                R.id.chargeFragment -> Log.d(TAG,"charge")
                R.id.mineFragment -> Log.d(TAG,"mine")
            }
        }
        viewModel.isShowBottom.observe(this, Observer {
            if (it){
                navBottom.visibility=View.VISIBLE
            }else{
                navBottom.visibility=View.GONE
            }
        })
        viewModel.isUserLogined.observe(this, Observer {
            Log.d(TAG,"isUserLogined"+it.toString())
            if (it){
//                GraphqlRemoteDataSource().initTokenAndUid(Utils.readToken(),Utils.readUid())
                setUpDataSource()
                navControlor.navigate(R.id.mainFragment)
            }else{
                SharePreferenceUtils.saveToken("")
                SharePreferenceUtils.saveUid("")
                //跳转登陆逻辑
                navControlor.navigate(R.id.loginFragment)
            }
        })
        if (!TextUtils.isEmpty(SharePreferenceUtils.readUid()) && !TextUtils.isEmpty(SharePreferenceUtils.readToken())){
            viewModel.isShowBottom.postValue(true)
            viewModel.isUserLogined.postValue(true)
        }else{
            viewModel.isShowBottom.postValue(false)
            viewModel.isUserLogined.postValue(false)
        }
        initConfigData()
    }

    fun initConfigData(){
        val successConfigDataDp=dataSource.configData
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleSuccessConfigData)
        val errorConfigDataDp=dataSource.error
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleErrorData)
        compositeDisposable.add(successConfigDataDp)
        compositeDisposable.add(errorConfigDataDp)
        dataSource.getConfigData()
    }

    fun handleSuccessConfigData(data: ConfigDataQuery.Config){
        data?.let {
            MainViewModule.configData=data
        }
    }

     fun setUpDataSource(){
        val successUserDisposable=dataSource.userData
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleSuccessUserData)

        val errorUserDisposable=dataSource.error
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleErrorData)
        compositeDisposable.add(successUserDisposable)
        compositeDisposable.add(errorUserDisposable)
        dataSource.getUserData()
    }

    private fun handleSuccessUserData(userData: UserQuery.User){
        MainViewModule.mutableLiveuserData.value=userData
        MainViewModule.userData=userData
    }

    private fun handleErrorData(error: Throwable?){
        Log.d(TAG,"handleErrorData--")
        runOnUiThread {
            ToastUtils.showShort(error?.message)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()

    }

    override fun initVariableId(): Int {
       return BR.viewModel
    }

    override fun initViewModel(): MainViewModule {
        val mainViewModel: MainViewModule by viewModels()
        return mainViewModel
    }

}