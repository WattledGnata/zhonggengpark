package cn.zhonggeng.park

import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Adapter(var clickListener: OnClickListener) : RecyclerView.Adapter<Adapter.ViewHolder>() {
    private var data: ArrayList<WeChatParkBean> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(Button(parent.context))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }


    fun setAccountList(accountList:ArrayList<WeChatParkBean>){
        data.clear()
        data.addAll(accountList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(var textView: TextView) : RecyclerView.ViewHolder(textView) {
        init {
            textView.id = R.id.item_key
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,12f)
            var layoutParams:ViewGroup.LayoutParams = ViewGroup.LayoutParams(-1,Utils.dp2px(50f))
            textView.gravity = Gravity.CENTER
            textView.layoutParams = layoutParams

        }

        fun bindData(parkInfo: WeChatParkBean) {
            textView.text = parkInfo.name + "      " + parkInfo.time
            textView.setOnClickListener{
                clickListener.onItemClick(parkInfo.url,parkInfo.name)
            }

        }
    }
}


interface OnClickListener {
    fun onItemClick(url: String, name: String)
}