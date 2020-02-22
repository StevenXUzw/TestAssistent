package com.steven.testassistant

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.steven.testassistant.common.Utils.DeviceUtils
import kotlinx.android.synthetic.main.activity_screen.*

class ScreenActivity : AppCompatActivity() {
    companion object {
        fun startScreenActivity(context: Context, type: Int) {
            var intent = Intent()
            intent.putExtra("type", type)
            intent.setClass(context, ScreenActivity::class.java)
            context.startActivity(intent)
        }
        var TAG = this.javaClass.name
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen)
        var deviceUtil = DeviceUtils(this)
        var info = String.format("屏幕宽高【像素】：%s * %s\n",  deviceUtil.screenPix[0], deviceUtil.screenPix[1])+ String.format("屏幕密度：%s\n", deviceUtil.densityDpi)+String.format("屏幕密度与标准屏幕密度的比值：%s\n", deviceUtil.density)+String.format("获取当前文字的比值：%s\n", deviceUtil.scaledDensity)
        screen_info.text = info
    }
}
