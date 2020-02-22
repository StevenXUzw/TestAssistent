package com.steven.testassistant.common.Utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DeviceUtils {
    private final TelephonyManager telephonyManager;
    private final WifiManager wifiManager;
    DisplayMetrics metrics;
    Context mContext;

    public DeviceUtils(Context context) {
        mContext = context;
        telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        metrics=mContext.getResources().getDisplayMetrics();
    }

    /**
     * 获取CPU型号
     * @return cpu name
     */
    public static String getCpuName() {
        String str1 = "/proc/cpuinfo";
        String str2 = "";
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr);
            while ((str2 = localBufferedReader.readLine()) != null) {
                if (str2.contains("Hardware")) {
                    return str2.split(":")[1];
                }
            }
            localBufferedReader.close();
        } catch (IOException e) {
        }
        return null;
    }

    /**
     * 读取设备imei号
     * @return
     */
    public String getImei() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return "";
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return telephonyManager.getImei();
        } else {
            return telephonyManager.getDeviceId();
        }
    }

    /**
     *
     * @return
     */
    public String getDeviceSoftwareVersion() {
        return Build.VERSION.RELEASE;
    }

    public String getLine1Number() {

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return "";
        }
        return telephonyManager.getLine1Number();
    }

    /**
     *
     * @return
     */
    public String getSimOperatorName(){
        String tt =  telephonyManager.getSimOperatorName();
        return tt;
    }

    public int getNetWorkType(){

        return telephonyManager.getNetworkType();
    }

    /**
     *
     * @return
     */
    public int getWifiState(){
        return wifiManager.getWifiState();
    }

    public boolean enableWifi(){
        return wifiManager.setWifiEnabled(true);
    }

    public String getMacAddress(){
        return wifiManager.getConnectionInfo().getMacAddress();
    }
    public int getIpAddress(){
        return wifiManager.getConnectionInfo().getIpAddress();
    }
    public String getDeviceBrand(){
        return Build.BRAND;
    }
    public String getDeviceName(){
        return Build.DEVICE;
    }

    public int[] getScreenPix(){
        //获取屏幕宽度的物理像素格式
        int widthPix=metrics.widthPixels;
        //获取屏幕高度的物理像素格式
        int heightPix=metrics.heightPixels;
        int[] pix = {widthPix, heightPix};
        return pix;
    }
    public int getDensityDpi(){
        //获取屏幕密度
        int density=metrics.densityDpi;
        return density;
    }

    public float getDensity(){
        //获取当前屏幕密度与标准屏幕密度的比值
        float denstiyScale=metrics.density;
        return denstiyScale;
    }

    public float getScaledDensity(){
        //获取当前文字的比值
        float fontDensity=metrics.scaledDensity;
        return fontDensity;
    }

}
