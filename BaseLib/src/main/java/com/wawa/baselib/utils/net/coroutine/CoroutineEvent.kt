package com.wawa.baselib.utils.net.coroutine

import kotlinx.coroutines.*

/**
 *作者：create by 张金 on 2021/1/15 16:25
 *邮箱：564813746@qq.com
 */
interface CoroutineEvent{
    /**
     * 此字段用于声明在 BaseViewModel，BaseRemoteDataSource，BaseView 下和生命周期绑定的协程作用域
     * 推荐的做法是：
     * 1.BaseView 单独声明自己和 View 相关联的作用域
     * 2.BaseViewModel 单独声明自己和 ViewModel 相关联的作用域，因为一个 BaseViewModel 可能和多个 BaseView 相关联，所以不要把 BaseView 的 CoroutineScope 传给 BaseViewModel
     * 3.BaseRemoteDataSource 首选使用 BaseViewModel 传过来的 lifecycleCoroutineScope，因为 BaseRemoteDataSource 和 BaseViewModel 是一对一的关系
     */
    val lifecycleSuportScope: CoroutineScope

    /**
     * 此字段用于声明在全局范围下的协程作用域，不和生命周期绑定
     */
    val globalScope: CoroutineScope
        get() = GlobalScope

    val mainDispacher: CoroutineDispatcher
        get() = Dispatchers.Main

    val ioDispacher: CoroutineDispatcher
        get() = Dispatchers.IO

    val cpuDispacher: CoroutineDispatcher
        get() = Dispatchers.Default

    suspend fun <T> withNonCancelable(block: suspend CoroutineScope.() -> T) : T{
        return withContext(NonCancellable,block)
    }

    suspend fun <T> withMain(block: suspend CoroutineScope.() -> T) : T{
        return withContext(mainDispacher,block)
    }

    suspend fun <T> withIO(block: suspend CoroutineScope.() -> T) : T{
        return withContext(ioDispacher,block)
    }

    suspend fun <T> withCpu(block: suspend CoroutineScope.() -> T) : T{
        return withContext(cpuDispacher,block)
    }

    /******************************不和生命周期绑定的方法******************************/
    fun launchMainG(block: suspend CoroutineScope.()-> Unit) : Job{
        return globalScope.launch(context = mainDispacher,block = block)
    }

    fun launchIOG(block: suspend CoroutineScope.() -> Unit) : Job{
        return globalScope.launch(context = ioDispacher,block = block)
    }

    fun launchCpuG(block: suspend CoroutineScope.() -> Unit) : Job{
        return globalScope.launch(context = cpuDispacher,block = block)
    }

    fun <T> asynMainG(block: suspend CoroutineScope.() -> T) :Deferred<T>{
        return globalScope.async(context = mainDispacher,block = block)
    }

    fun <T> asynIOG(block: suspend CoroutineScope.() -> T) : Deferred<T>{
        return globalScope.async(context = ioDispacher,block = block)
    }

    fun <T> asynCpuG(block: suspend CoroutineScope.() -> T) : Deferred<T>{
        return globalScope.async(context = cpuDispacher,block = block)
    }

    /******************************和生命周期绑定的方法******************************/
    fun launchMain(block:suspend CoroutineScope.() -> Unit) : Job{
        return lifecycleSuportScope.launch(context = mainDispacher,block = block)
    }

    fun launchIO(block: suspend CoroutineScope.() -> Unit) : Job{
        return lifecycleSuportScope.launch(context = ioDispacher,block = block)
    }

    fun launchCpu(block: suspend CoroutineScope.() -> Unit) : Job{
        return lifecycleSuportScope.launch(context = cpuDispacher,block = block)
    }

    fun <T> asynMain(block: suspend CoroutineScope.() -> T) : Deferred<T>{
        return lifecycleSuportScope.async(context = mainDispacher,block = block)
    }

    fun <T> asynIO(block: suspend CoroutineScope.() -> T) : Deferred<T>{
        return lifecycleSuportScope.async(context = ioDispacher,block = block)
    }

    fun <T> asynCpu(block: suspend CoroutineScope.() -> T) : Deferred<T>{
        return lifecycleSuportScope.async(context = cpuDispacher,block = block)
    }

    /******************************扩展方法，外部不可调用******************************/
    private fun CoroutineScope.launchMain(block: suspend CoroutineScope.() -> Unit): Job {
        return launch(context = mainDispacher, block = block)
    }

    private fun CoroutineScope.launchIO(block: suspend CoroutineScope.() -> Unit): Job {
        return launch(context = ioDispacher, block = block)
    }

    private fun CoroutineScope.launchCPU(block: suspend CoroutineScope.() -> Unit): Job {
        return launch(context = cpuDispacher, block = block)
    }

    private fun <T> CoroutineScope.asyncMain(block: suspend CoroutineScope.() -> T): Deferred<T> {
        return async(context = mainDispacher, block = block)
    }

    private fun <T> CoroutineScope.asyncIO(block: suspend CoroutineScope.() -> T): Deferred<T> {
        return async(context = ioDispacher, block = block)
    }

    private fun <T> CoroutineScope.asyncCPU(block: suspend CoroutineScope.() -> T): Deferred<T> {
        return async(context = cpuDispacher, block = block)
    }

}