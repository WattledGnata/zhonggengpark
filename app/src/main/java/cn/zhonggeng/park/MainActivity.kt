package cn.zhonggeng.park

import StatusBarUtil
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.AlarmClock
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import cn.zhonggeng.park.kt.MainActivity2
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tencent.mmkv.MMKV
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_dialog_input_name.*
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : BaseActivity(), View.OnClickListener {
    companion object {
        val xzf = "http://zgg.xiaooo.club/popc/index.html?openid=ozkTyws0C7vBEoEbxUdlJKoqlGyI"
        val ccx = "http://zgg.xiaooo.club/popc/index.html?openid=ozkTywi2LNA8hKgT-suUpiowrHKA"
        val zxr = "http://zgg.xiaooo.club/popc/index.html?openid=ozkTywgBP097_FCBLnX60oAZmsCw"
        val xyx = "http://zgg.xiaooo.club/popc/index.html?openid=ozkTywgP3WGayMBYQFLUP7fjiJHo"
        val zz = "http://zgg.xiaooo.club/popc/index.html?openid=ozkTywqjdqAjxDkQWbgtbGG0BXho"
        val sqf = "http://zgg.xiaooo.club/popc/index.html?openid=ozkTywm9inUwfoIE5p4zN7uICJwM"
        val zq = "http://zgg.xiaooo.club/popc/index.html?openid=ozkTywlxVpOrAYmaVorapGj5YL44"
        val hy = "http://zgg.xiaooo.club/popc/index.html?openid=ozkTywoq8QLyHXB-vAL_0U7KSVKw"
        val hy2 = "http://zgg.xiaooo.club/popc/index.html?openid=ozkTywgvnvijnqnpkqpD0HaFRUFE"
        val lzq = "http://zgg.xiaooo.club/popc/index.html?openid=ozkTywqFX9MrylHOwKSfrkj74Kds"
    }

    private var list: ArrayList<WeChatParkBean> = ArrayList()
    private lateinit var adapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MMKV.initialize(applicationContext)
        setContentView(R.layout.activity_main)
        StatusBarUtil.setColor(this, Color.parseColor("#399D07"))

        initData()
        initView()
    }

    override fun onClick(v: View?) {

    }

    private fun initView() {
        adapter = Adapter(object : OnClickListener {
            override fun onItemClick(url: String, name: String) {
                jump2Park(url)
                list.forEach() {
                    if (it.name == name) {
                        it.time = getFormatTimeStamp2HM()
                    }
                }
                adapter.notifyDataSetChanged()
            }
        })
        account_list.adapter = adapter
        account_list.layoutManager = LinearLayoutManager(this)
        adapter.setAccountList(list)
        btn_save.setOnClickListener {
            if (input.text.startsWith("http")) {
                showDialog(input.text.toString())
            } else {
                showToast("请输入有效的链接")
            }
        }
        btn_reset.setOnClickListener{
            list.forEach(){it.time = ""}
            adapter.notifyDataSetChanged()
        }
    }

    fun initData() {
        var listArray: String = PreferenceUtil.getString(
            this,
            "key",
            "",
            PreferenceUtil.SP_KEY.TH_TABLE
        ).orEmpty()
        var listPreference = Gson().fromJson<List<WeChatParkBean>>(
            listArray,
            object : TypeToken<List<WeChatParkBean>>() {}.type
        ).orEmpty()
        list.addAll(listPreference)
    }

    fun jump2Park(url: String) {
        var intent = Intent();
        intent.putExtra("url", url)
        intent.setClass(this@MainActivity, MainActivity2::class.java)
        startActivity(intent)
    }

    private fun showDialog(url: String) {
        var dialog = CommonAlertDialog.Builder(this)
            .setOnLeftCancelListener { it.dismiss() }
            .setOnRightConfirmListener { content, dialog ->
                if (nameContains(content)) {
                    showToast("已存在的名称")
                    return@setOnRightConfirmListener
                }
                list.add(WeChatParkBean(content, url, ""))
                adapter.setAccountList(list)
                dialog.dismiss()
            }
            .create()
        dialog.show()
    }

    private fun nameContains(name: String): Boolean {
        for (wechat in list) {
            if (wechat.name == name) {
                return true
            }
        }
        return false
    }

    private fun showToast(str: String) {
        Toast.makeText(this@MainActivity, str, Toast.LENGTH_SHORT).show()
    }

    override fun onPause() {
        super.onPause()
        PreferenceUtil.setString(this, "key", Gson().toJson(list), PreferenceUtil.SP_KEY.TH_TABLE)
    }

    /**
     * 时间戳格式转换成 HH:mm
     * @param time
     * @return
     */
    fun getFormatTimeStamp2HM(): String? {
        try {
            val dateFormat = SimpleDateFormat("HH:mm  dd日")
            dateFormat.dateFormatSymbols = DateFormatSymbols
                .getInstance(Locale.CHINA)
            val date = Calendar.getInstance().time
            return dateFormat.format(date)
        } catch (e: Exception) {
        }
        return ""
    }

    /**
     * 设置闹钟
     * */

    fun setAlert(){
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.SECOND, 10)
//setAlarm(cale.
        val alarmIntent = Intent(AlarmClock.ACTION_SET_ALARM)
        alarmIntent.putExtra(AlarmClock.EXTRA_MESSAGE, "停车提醒")
        alarmIntent.putExtra(AlarmClock.EXTRA_HOUR, calendar[Calendar.HOUR_OF_DAY])
        alarmIntent.putExtra(AlarmClock.EXTRA_MINUTES, calendar[Calendar.MINUTE] + 1)
        alarmIntent.putExtra(AlarmClock.EXTRA_SKIP_UI, true)
        alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(alarmIntent)
    }
}