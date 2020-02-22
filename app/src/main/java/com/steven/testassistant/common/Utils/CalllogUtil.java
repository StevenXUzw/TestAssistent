package com.steven.testassistant.common.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.steven.testassistant.R;
import com.steven.testassistant.common.Content;
import com.steven.testassistant.database.CallLogData;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.telecom.Call;
import android.util.Log;


public class CalllogUtil {
	public static String TAG = CalllogUtil.class.getSimpleName();
	private Context mContext;
	public final String TEST_NUMBER_CALL = "662317";
	public static final String[] TYPE = {"呼入", "呼出", "未接"};
	public static final List<String> CALLLOG_TYPE = Arrays.asList(TYPE);

	public CalllogUtil(Context mContext) {
		this.mContext = mContext;
	}

	public int[] getCalllogCount() {
		int[] call_sort = new int[3];
		ContentResolver contentResolver = mContext.getContentResolver();
		if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			return null;
		}
		Cursor cursor = contentResolver.query(CallLog.Calls.CONTENT_URI, null, null, null, null);
		cursor.moveToFirst();
		int call_out = 0, call_in = 0, call_miss = 0;
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			String type = cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE));
			switch (Integer.valueOf(type)) {
				case CallLog.Calls.INCOMING_TYPE:
					call_in++;
					break;
				case CallLog.Calls.OUTGOING_TYPE:
					call_out++;
					break;
				case CallLog.Calls.MISSED_TYPE:
					call_miss++;
					break;

				default:
					break;
			}
		}
		cursor.close();
		call_sort[0] = call_out;
		call_sort[1] = call_in;
		call_sort[2] = call_miss;
		return call_sort;
	}

	public ContentValues insertCallLog(String number, int callType) {
		long start = new Date().getTime() - 1000 * 60;
		int duration = 1000 * 60;

		ContentValues values = new ContentValues();
		values.put(CallLog.Calls.NUMBER, number);
		values.put(CallLog.Calls.TYPE, Integer.valueOf(callType));
		values.put(CallLog.Calls.DATE, Long.valueOf(start));
		values.put(CallLog.Calls.DURATION, Long.valueOf(duration));
		values.put(CallLog.Calls.NEW, Integer.valueOf(1));
		if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			return null;
		}
		Uri calllogUri = mContext.getContentResolver().insert(CallLog.Calls.CONTENT_URI, values);
        values.put(CallLogData.CALLLOG_URI, calllogUri.toString());
		Log.i(TAG, "genarate one call log");
        return values;
	}

	/**
	 *
	 * @param mHandler
	 * @param times
	 */
	public void calllogFill(Handler mHandler, int times, String phoneNum, String type){
		mHandler.sendMessage(mHandler.obtainMessage(Content.MSG_TYPE_UPDATE_PROGRESSDIALOG, mContext.getString(R.string.importing_callog)));
		for (int k = 0; k < times; k++) {
			phoneNum = phoneNum == null || phoneNum.equals("")? Util.randomNumber(11) : phoneNum;
			int ty = CALLLOG_TYPE.indexOf(type);
			ty = ty >=0? ty : (int)(Math.random()*3);//没有输入默认是随机
			Log.i(TAG, "type:"+type+"ty"+ty);
			insertCallLog(phoneNum, ty+1);
			//pd.setProgress(k) ;
			mHandler.sendMessage(mHandler.obtainMessage(Content.MSG_TYPE_UPDATE_PROGRESSDIALOG_PROGRESS, k));
		}
		//dismissDialog();
		mHandler.sendMessage(mHandler.obtainMessage(Content.MSG_TYPE_DISMISS_PROGRESSDIALOG, "dimiss progressdialog"));
	}

}
