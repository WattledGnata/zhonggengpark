package cn.zhonggeng.park.kt

import StatusBarUtil
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import cn.zhonggeng.park.BaseActivity
import cn.zhonggeng.park.R
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient
import kotlinx.android.synthetic.main.activity_main2.*

class MainActivity2 : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        StatusBarUtil.setColor(this, Color.parseColor("#399D07"))
        // 根据状态栏高度设置
        if (intent.getStringExtra("url").isNullOrBlank())
            finish()


        webView.addJavascriptInterface(MyJavaScript(),"HTMLOUT")
        webView.webViewClient  = object :WebViewClient(){
            override fun onPageFinished(p0: WebView?, p1: String?) {
                super.onPageFinished(p0, p1)
                p0?.loadUrl("javascript:window.HTMLOUT.showHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');")
            }
        }
        webView.loadUrl(intent.getStringExtra("url"))
        webView.goBack()
        btn_back.setOnClickListener { finish() }
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (webView.canGoBack()){
            webView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }


     class MyJavaScript {
        fun showHTML(html: String?) {
            Log.d("zhanghao HTML", html)
        }
    }
}