package com.wawa.baselib.utils.net.viewmodule

import kotlinx.coroutines.Job

/**
 *作者：create by 张金 on 2021/1/15 16:18
 *邮箱：564813746@qq.com
 */
open class BaseActionEvent

class ShowLoadingEvent(val job: Job?) : BaseActionEvent()
object DismissLoadingEvent : BaseActionEvent()
object FinishViewEvent : BaseActionEvent()
class ShowToastEvent(val message: String) : BaseActionEvent()