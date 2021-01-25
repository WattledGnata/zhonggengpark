package cn.zhonggeng.park

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity :AppCompatActivity() {


    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier(
            "status_bar_height",
            "dimen", "android"
        )
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }
}