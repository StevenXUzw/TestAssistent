package com.steven.testassistant.common.Utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat

private var permissionCode = 0
data class _Permission(val granted: (Boolean) -> Unit)
object PermissionManager {
    val permissionList = HashMap<Int, _Permission>()
    fun onRequestPermissionsResult(activity: Activity, requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (permissionList.containsKey(requestCode)) {
            val granted = permissionList.remove(requestCode) ?: return
            val isTip = ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[0])
            val isDenied = grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED
            if (isDenied) {
                if (isTip) {
                    val code = ++permissionCode
                    PermissionManager.permissionList[requestCode] = granted
                    ActivityCompat.requestPermissions(activity, permissions, code)
                } else {
                    granted.granted.invoke(false)
                }
            } else
                granted.granted.invoke(true)
        }
    }
}
fun Activity.locationPermission(granted: (Boolean) -> Unit) {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission_group.LOCATION) == PackageManager.PERMISSION_GRANTED)
        granted(true)
    else {
        val requestCode = ++permissionCode
        PermissionManager.permissionList[requestCode] = _Permission(granted)
        ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION), requestCode)
    }
}
fun Activity.storagePermission(granted: (Boolean) -> Unit) {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission_group.STORAGE) == PackageManager.PERMISSION_GRANTED)
        granted(true)
    else {
        val requestCode = ++permissionCode
        PermissionManager.permissionList[requestCode] = _Permission(granted)
        ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE), requestCode)
    }
}
fun Activity.contactsPermission(granted: (Boolean) -> Unit) {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission_group.CONTACTS) == PackageManager.PERMISSION_GRANTED)
        granted(true)
    else {
        val requestCode = ++permissionCode
        PermissionManager.permissionList[requestCode] = _Permission(granted)
        ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.GET_ACCOUNTS,
                Manifest.permission.WRITE_CONTACTS), requestCode)
    }
}
fun Activity.phonePermission(granted: (Boolean) -> Unit) {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission_group.PHONE) == PackageManager.PERMISSION_GRANTED)
        granted(true)
    else {
        val requestCode = ++permissionCode
        PermissionManager.permissionList[requestCode] = _Permission(granted)
        ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.READ_CALL_LOG,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.WRITE_CALL_LOG
//                Manifest.permission.USE_SIP,
//                Manifest.permission.PROCESS_OUTGOING_CALLS,
//                Manifest.permission.ADD_VOICEMAIL
            ), requestCode)
    }
}
fun Activity.deviceInfoPermission(granted: (Boolean) -> Unit) {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission_group.PHONE) == PackageManager.PERMISSION_GRANTED)
        granted(true)
    else {
        val requestCode = ++permissionCode
        PermissionManager.permissionList[requestCode] = _Permission(granted)
        ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_SMS,
                Manifest.permission.READ_PHONE_NUMBERS

        ), requestCode)
    }
}
fun Activity.calendarPermission(granted: (Boolean) -> Unit) {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission_group.CALENDAR) == PackageManager.PERMISSION_GRANTED)
        granted(true)
    else {
        val requestCode = ++permissionCode
        PermissionManager.permissionList[requestCode] = _Permission(granted)
        ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.READ_CALENDAR,
                Manifest.permission.WRITE_CALENDAR), requestCode)
    }
}
fun Activity.cameraPermission(granted: (Boolean) -> Unit) {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission_group.CAMERA) == PackageManager.PERMISSION_GRANTED)
        granted(true)
    else {
        val requestCode = ++permissionCode
        PermissionManager.permissionList[requestCode] = _Permission(granted)
        ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.CAMERA), requestCode)
    }
}
fun Activity.sensorsPermission(granted: (Boolean) -> Unit) {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission_group.SENSORS) == PackageManager.PERMISSION_GRANTED)
        granted(true)
    else {
        val requestCode = ++permissionCode
        PermissionManager.permissionList[requestCode] = _Permission(granted)
        ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.BODY_SENSORS), requestCode)
    }
}
fun Activity.microphonePermission(granted: (Boolean) -> Unit) {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission_group.MICROPHONE) == PackageManager.PERMISSION_GRANTED)
        granted(true)
    else {
        val requestCode = ++permissionCode
        PermissionManager.permissionList[requestCode] = _Permission(granted)
        ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.RECORD_AUDIO), requestCode)
    }
}
fun Activity.smsPermission(granted: (Boolean) -> Unit) {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission_group.SMS) == PackageManager.PERMISSION_GRANTED)
        granted(true)
    else {
        val requestCode = ++permissionCode
        PermissionManager.permissionList[requestCode] = _Permission(granted)
        ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.READ_SMS,
                Manifest.permission.RECEIVE_WAP_PUSH,
                Manifest.permission.RECEIVE_MMS,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.SEND_SMS), requestCode)
    }
}