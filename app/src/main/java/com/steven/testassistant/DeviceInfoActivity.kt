package com.steven.testassistant

import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.steven.testassistant.Generator.GeneratorActivity
import com.steven.testassistant.common.Utils.DeviceUtils
import com.steven.testassistant.common.Utils.deviceInfoPermission
import kotlinx.android.synthetic.main.activity_device_info.*

class DeviceInfoActivity : AppCompatActivity() {
    companion object {
        fun startDeviceInfoActivity(context: Context, type: Int) {
            var intent = Intent()
            intent.putExtra("type", type)
            intent.setClass(context, DeviceInfoActivity::class.java)
            context.startActivity(intent)
        }
        var TAG = this.javaClass.name
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_info)
        deviceInfoPermission { onPermissionGranted(it) }
        var info = getDeviceInfo()
        device_info.text = info
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
                Log.d(GeneratorActivity.TAG, String.format("客官不给我%s的权限", permission))
//                finish()
            }
        }
    }

    private fun getDeviceInfo(): String {
        var deviceUtil = DeviceUtils(this)
        var info = String.format("品牌：%s\n型号：%s\nCPU：%s\n屏幕：%s\nIMEI：%s\n软件版本：%s\nIP地址：%s\nmac地址：%s\n手机号：%s\n网络类型：%s\n运营商：%s\n",
                Build.MANUFACTURER,
                Build.MODEL,
                DeviceUtils.getCpuName(),
                Build.DISPLAY,
                deviceUtil.imei,
                deviceUtil.deviceSoftwareVersion,
                deviceUtil.ipAddress,
                deviceUtil.macAddress,
                deviceUtil.line1Number,
                deviceUtil.netWorkType,
                deviceUtil.simOperatorName

                )
        Log.i("debug", info)
        return info
    }
}
