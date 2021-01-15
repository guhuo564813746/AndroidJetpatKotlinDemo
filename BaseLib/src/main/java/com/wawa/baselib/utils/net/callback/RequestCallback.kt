package com.wawa.baselib.utils.net.callback


/**
 *作者：create by 张金 on 2021/1/15 11:47
 *邮箱：564813746@qq.com
 */
class RequestCallback<Data>(internal var onSuccess:((Data)->Unit)?= null,
                            internal var onSuccessIO: (suspend (Data)->Unit)? =null) : BaseRequestCallback() {
     /**
      * 当网络请求成功时会调用此方法，随后会先后调用 onSuccessIO、onFinally 方法
      */
     fun onSuccess(block: (Data)-> Unit){
          this.onSuccess=block
     }

     /**
      * 在 onSuccess 方法之后，onFinally 方法之前执行
      * 考虑到网络请求成功后有需要将数据保存到数据库之类的耗时需求，所以提供了此方法用于在 IO 线程进行执行
      * 注意外部不要在此处另开子线程，此方法会等到耗时任务完成后再执行 onFinally 方法
      */
     fun onSuccessIO(block: suspend (Data) -> Unit){
          this.onSuccessIO=block
     }

}

class RequestPairCallback<Data1,Data2>(internal var onSuccess: ((data1: Data1,data2: Data2) -> Unit)?=null,
                                        internal var onSuccessIO: (suspend (data1: Data1,data2: Data2)-> Unit)?=null) :BaseRequestCallback() {
     fun onSuccess(block: (data1: Data1,data2: Data2)-> Unit){
          this.onSuccess=block
     }

     fun onSuccessIO(block: suspend (data1: Data1, data2: Data2) -> Unit){
          this.onSuccessIO=block
     }
}