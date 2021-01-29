package com.wawa.wawaandroid_ep

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wawa.baselib.utils.Utils
import com.wawa.baselib.utils.net.datasource.GraphqlRemoteDataSource
import com.wawa.wawaandroid_ep.base.activity.BaseActivity
import com.wawa.wawaandroid_ep.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {
    private lateinit var navBottom: BottomNavigationView
    val mainViewModel: MainViewModule by viewModels()
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
        mainViewModel.isShowBottom.observe(this, Observer {
            if (it){
                navBottom.visibility=View.VISIBLE
            }else{
                navBottom.visibility=View.GONE
            }
        })
        mainViewModel.isUserLogined.observe(this, Observer {
            Log.d(TAG,"isUserLogined"+it.toString())
            if (it){
//                GraphqlRemoteDataSource().initTokenAndUid(Utils.readToken(),Utils.readUid())
                mainViewModel.getUserData()
                navControlor.navigate(R.id.mainFragment)
            }else{
                Utils.saveToken("")
                Utils.saveUid("")
                //跳转登陆逻辑
                navControlor.navigate(R.id.loginFragment)
            }
        })
        if (!TextUtils.isEmpty(Utils.readUid()) && !TextUtils.isEmpty(Utils.readToken())){
            mainViewModel.isShowBottom.postValue(true)
            mainViewModel.isUserLogined.postValue(true)
        }else{
            mainViewModel.isShowBottom.postValue(false)
            mainViewModel.isUserLogined.postValue(false)
        }
    }

}