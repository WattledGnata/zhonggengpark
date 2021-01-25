package cn.zhonggeng.park

import java.io.Serializable

data class WeChatParkBean(val name: String, val url: String, var time: String? = null) :Serializable
