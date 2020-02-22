package com.steven.testassistant.Generator

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.steven.testassistant.common.Content
import com.steven.testassistant.R
import com.steven.testassistant.common.Utils.*

import kotlinx.android.synthetic.main.activity_generator.*
import kotlinx.android.synthetic.main.content_generator.*
import java.lang.ref.WeakReference
import android.widget.ArrayAdapter



class GeneratorActivity : AppCompatActivity() {

    var permisson_list = arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS)
    companion object {
        fun startGeneratorActivity(context: Context, type: Int) {
            var intent = Intent()
            intent.putExtra("type", type)
            intent.setClass(context, GeneratorActivity::class.java)
            context.startActivity(intent)
        }
        var TAG = this.javaClass.name
    }

    private var mContactUtil: ContactsUtil?= null
    private var mCallogUtil: CalllogUtil ?= null
    private var mHandler: MyHandler?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generator)
        setSupportActionBar(toolbar)
        var TITLES = arrayOf(R.string.contact_tool, R.string.call_log_tool, R.string.message_tool)
        mContactUtil = ContactsUtil(this)
        mCallogUtil = CalllogUtil(this)
        mHandler = MyHandler(this)
        var type = intent.getIntExtra("type", 1)
        toolbar.setTitle(TITLES[type])
        handlerPermission(type)
        Toast.makeText(this, "$type", Toast.LENGTH_SHORT).show()
        initView(type)
        fab.setOnClickListener { view ->
            when(type){
                Content.TYPE_CONTACT->generatContactRun()
                Content.TYPE_MESSAGE->{}
                Content.TYPE_CALL_LOG->generatCallLog()
            }
            Snackbar.make(view, "开始生成", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }
    override fun onDestroy() {
        // Remove all Runnable and Message.
        MyHandler(this).removeCallbacksAndMessages(null)
        super.onDestroy()
    }

    /**
     * 向系统申请权限之后的回调接口
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (permission in permissions){
            var result = grantResults[permissions.indexOf(permission)]
            if (result==0){
                //result:0,用户授权了
                continue
            }else{
                Log.d(TAG, String.format("客官不给我%s的权限", permission))
//                finish()
            }
        }
    }
    private fun initView(type: Int){
        when(type){
            Content.TYPE_CONTACT->{
                tv_content.setText("姓名：")
                var adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ContactsUtil.NAME)
                et_text.setAdapter(adapter)
            }
            Content.TYPE_MESSAGE->tv_content.setText("内容：")
            Content.TYPE_CALL_LOG->{
                tv_content.setText("类型：")
                var adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, CalllogUtil.TYPE)
                et_text.setAdapter(adapter)
            }
        }
        cb_text_random.setOnCheckedChangeListener{button, isChecked ->
            et_text.isEnabled = !isChecked
        }
        cb_phone_random.setOnCheckedChangeListener{buttonView, isChecked ->
            et_phone.isEnabled = !isChecked
        }

    }

    /**
     * 处理权限
     */
    private fun handlerPermission(type: Int) {
        when(type){
            Content.TYPE_CONTACT->contactsPermission { onPermissionGranted(it) }
            Content.TYPE_MESSAGE->smsPermission { onPermissionGranted(it) }
            Content.TYPE_CALL_LOG->phonePermission { onPermissionGranted(it) }
        }
    }

    /**
     * 动态申请权限的回调接口
     * it：true：已经拥有权限
     */
    private fun onPermissionGranted(it: Boolean) {
        if(it){

        }else{
        }
    }

    /**
     * 生成联系人
     */
    protected fun generatContactRun() {
        var repeat_num = et_repeat_num.text.toString()
        if (repeat_num != "") {
            var times = Integer.parseInt(repeat_num)
            if(times>1 && et_text.isEnabled && et_phone.isEnabled){
                Toast.makeText(this, getString(R.string.same_contact), Toast.LENGTH_SHORT).show()
            }
            var name = et_text.text.toString()
            if (!et_text.isEnabled) name = ""
            else if(name == null || name.equals("")){
                Toast.makeText(this, getString(R.string.input_calllog_type), Toast.LENGTH_SHORT).show()
                return
            }
            var phoneNum = et_phone.text.toString()
            if (!et_phone.isEnabled) phoneNum = ""
            else if(phoneNum == null || phoneNum.equals("")){
                Toast.makeText(this, getString(R.string.input_mobile), Toast.LENGTH_SHORT).show()
                return
            }
            Thread(Runnable {
                // TODO Auto-generated method stub
                mContactUtil?.generateContacts(mHandler, times, name, phoneNum)
            }).start()
        } else {
//            mUtil.toastShow()
        }
    }

    /**
     * 生成通话记录
     */
    protected fun generatCallLog(){
        var repeat_num = et_repeat_num.text.toString()
        if (repeat_num!= "") {
            var times = Integer.parseInt(repeat_num)
            var type = et_text.text.toString()
            if (!et_text.isEnabled) type = ""
            else if(type == null || type.equals("")){
                Toast.makeText(this, getString(R.string.input_calllog_type), Toast.LENGTH_SHORT).show()
                return
            }
            var phoneNum = et_phone.text.toString()
            if(phoneNum == null || phoneNum.equals("")){
                Toast.makeText(this, getString(R.string.input_mobile), Toast.LENGTH_SHORT).show()
                return
            }
            Thread(Runnable {
                // TODO Auto-generated method stub
                mCallogUtil?.calllogFill(mHandler, times, phoneNum, type)

            }).start()
        } else {
//            mUtil.toastShow()
        }
    }


    private class MyHandler(activity: GeneratorActivity) : Handler() {
        private val mActivity: WeakReference<GeneratorActivity> = WeakReference(activity)
        override fun handleMessage(msg: Message) {
            if (mActivity.get() == null) {
                return
            }
//            val activity = mActivity.get()
            when (msg.what) {
                Content.MSG_TYPE_UPDATE_PROGRESSDIALOG->{

                }
                else -> {
                }
            }
        }
    }

}


