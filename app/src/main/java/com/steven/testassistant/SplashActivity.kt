package com.steven.testassistant

import android.content.Intent
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent

/**
 * 启动闪屏页，用于数据初始化及开屏广告
 */
class SplashActivity : AppCompatActivity() {
    private var mJumpMainHandler: Handler? = null
    private var mJumpMainTask: Runnable? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        initData()
    }

    public override fun onResume() {
        super.onResume()
        mJumpMainHandler!!.postDelayed(mJumpMainTask, 2500)
    }

    public override fun onPause() {
        super.onPause()
        mJumpMainHandler!!.removeCallbacks(mJumpMainTask)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (event.keyCode == KeyEvent.KEYCODE_BACK) {
            true//屏蔽返回键
        } else super.onKeyDown(keyCode, event)
    }

    private fun initData() {
        mJumpMainHandler = Handler()
        mJumpMainTask = Runnable { jumpMain() }
    }

    private fun jumpMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()//finish掉，在mainactivity返回就不会回到这了
    }
}
