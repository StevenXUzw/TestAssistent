package com.steven.testassistant.common.Utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts.Data;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;

import com.steven.testassistant.common.Content;
import com.steven.testassistant.R;
import com.steven.testassistant.database.ContactsData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ContactsUtil {
    private String TAG = ContactsUtil.class.getSimpleName();
    private Context mContext;
    public final String TEST_CONTACT_NUMBER = "13812013333";
    public final String TEST_CONTACT = "测试_%s_contact";
    public static final String[] NAME = {"张老师", "李师傅", "隔壁老王", "赵主任", "钱大妈"};

    public ContactsUtil(Context mContext){
        this.mContext = mContext;
    }

    /**
     *
     * @param mHandler message handler
     * @param times contacts sum
     */
    public void generateContacts(Handler mHandler, int times, String name, String number){

        mHandler.sendMessage(mHandler.obtainMessage(Content.MSG_TYPE_UPDATE_PROGRESSDIALOG, mContext.getString(R.string.importing_contacts)));

        ContentResolver mContentResolver = mContext.getContentResolver();
        int max_value = getMaxContactValue(mContentResolver , "CONTACT") ;
        for (int i = 1; i <= times; i++) {
            int tag = i + max_value ;
            String displayName = name==null || name.equals("")? String.format(TEST_CONTACT , tag ) : name;
            String phoneNum = number==null || number.equals("")? TEST_CONTACT_NUMBER + tag : number ;
            writeContact(mContentResolver,displayName, phoneNum) ;
            //pd.setProgress(i) ;
            mHandler.sendMessage(mHandler.obtainMessage(Content.MSG_TYPE_UPDATE_PROGRESSDIALOG_PROGRESS, i));
        }
        //dismissDialog() ;
        mHandler.sendMessage(mHandler.obtainMessage(Content.MSG_TYPE_DISMISS_PROGRESSDIALOG, "dimiss progressdialog"));
    }

    /**
     * 获取填充的联系人的最大数量（按规则生成的名字中数字比对）
     * @param mContentResolver
     * @param containString
     * @return
     */
    public int getMaxContactValue(ContentResolver mContentResolver , String containString){
        Cursor cursor = mContentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null) ;
        cursor.moveToLast() ;
        int displayColumn = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME) ;
        int max_value = 0 ;
        int test = 0 ;
        for(int i = 0 ; i < cursor.getCount() ; i++){
            String displayName = cursor.getString(displayColumn) ;
            if(displayName.startsWith(containString)){
                test = Integer.parseInt(displayName.split("_")[1].split("_")[0]) ;
                if(test > max_value){
                    max_value = test ;
                }
            }
            cursor.moveToPrevious() ;
        }
        cursor.close() ;
        return max_value  ;
    }

    /**
     *
     * @param mContentResolver
     * @param str
     * @return
     */
    public ContentValues writeContact(ContentResolver mContentResolver, String... str) {
        // insert and get rawContactsId
        Util mUtil = new Util();
        String display_name =  mUtil.randomName(50);;
        String phone_number = "1" + mUtil.randomNumber(10);;
        if(str != null){
            if(str.length == 2){
                display_name = str[0];
                phone_number = str[1];
            }
        }

        long rawContactsId = writePhoneContact(mContentResolver,display_name,phone_number, null, null, false);

        ContentValues values = new ContentValues();
        values.put(ContactsData.CONTACTS_ID, rawContactsId);
        values.put(ContactsData.DISPLAY_NAME, display_name);
        values.put(ContactsData.PHONE_NUMBER, phone_number);
        return values;
    }

    /**
     * insert a contact data into database
     * @param mContentResolver
     * @param display_name
     * @param phone_number
     * @param mBitmap
     * @param ringtone
     * @param block
     * @return
     */
    public long writePhoneContact(ContentResolver mContentResolver, String display_name, String phone_number ,
                                  Bitmap mBitmap , Uri ringtone , boolean block) {
        // insert null to get rawContactsId
        ContentValues values = new ContentValues();
        Uri rawContactUri = mContentResolver.insert(ContactsContract.RawContacts.CONTENT_URI,values);
        long rawContactsId = ContentUris.parseId(rawContactUri);
        // insert display_name;
        values.clear();
        values.put(Data.RAW_CONTACT_ID, rawContactsId);
        values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
        values.put(StructuredName.DISPLAY_NAME, display_name);
        mContentResolver.insert(ContactsContract.Data.CONTENT_URI, values);
        // insert phone_number
        values.clear();
        values.put(Phone.RAW_CONTACT_ID,rawContactsId);
        values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
        values.put(Phone.NUMBER, phone_number);
        values.put(Phone.TYPE, Phone.TYPE_MOBILE);
        mContentResolver.insert(ContactsContract.Data.CONTENT_URI, values);

/*		//insert Photo
		values.clear() ;
		values.put(Phone.RAW_CONTACT_ID, rawContactsId);
		values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
		values.put(Photo.PHOTO, phone_number);
		values.put(Photo.IS_PRIMARY, 1);
		mContentResolver.insert(ContactsContract.Data.CONTENT_URI, values);
		*/
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if(mBitmap!=null){	// If an image is selected successfully
            mBitmap.compress(Bitmap.CompressFormat.PNG , 75, stream);
            values.clear() ;
            values.put(Phone.RAW_CONTACT_ID, rawContactsId);
            values.put(ContactsContract.Data.IS_SUPER_PRIMARY, 1);
            values.put(ContactsContract.Data.MIMETYPE, Photo.CONTENT_ITEM_TYPE);
            values.put(ContactsContract.CommonDataKinds.Photo.PHOTO, stream.toByteArray());
            mContentResolver.insert(ContactsContract.Data.CONTENT_URI, values);

            try {
                stream.flush();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(ringtone != null){
            final Uri lookupUri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, phone_number) ;
            final String[] projection = new String[]{
                    Contacts._ID,Contacts.LOOKUP_KEY
            } ;
            final Cursor data = mContentResolver.query(lookupUri, projection, null, null, null);
            data.moveToFirst() ;
            try {
                final long contactId = data.getLong(0) ;
                final String lookupKey = data.getString(1) ;
                final Uri contactUri =Contacts.getLookupUri(contactId, lookupKey) ;
                if(contactUri != null){
                    System.out.println("viking flag ------------------ringtone : "+ringtone);
                    setRingtone(mContentResolver, contactUri, ringtone) ;
                    if(block)
                        setSendToVoicemail(mContentResolver, contactUri, true) ;
                }
            } catch (Exception e) {
                e.printStackTrace() ;
            }finally{
                if(data!= null && !data.isClosed())
                    data.close() ;
            }
        }
        return rawContactsId;
    }

    /**
     * set ringtone of contact
     * @param resolver
     * @param contactUri
     * @param ringtone
     */
    public void setRingtone(ContentResolver resolver , Uri contactUri ,Uri ringtone) {
        ContentValues values = new ContentValues(1);
        values.put(Contacts.CUSTOM_RINGTONE, ringtone.toString());
        resolver.update(contactUri, values, null, null);
    }

    /**
     *
     * @param resolver
     * @param contactUri
     * @param value
     */
    public void setSendToVoicemail(ContentResolver resolver , Uri contactUri ,boolean value) {
        final ContentValues values = new ContentValues(1);
        values.put(Contacts.SEND_TO_VOICEMAIL, value);
        resolver.update(contactUri, values, null, null);
    }
}
