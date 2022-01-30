package com.example.flicon.phone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.widget.ListView;

import com.example.flicon.R;

import java.util.LinkedList;
import java.util.TreeMap;

public class SmsActivity extends AppCompatActivity {
    private static final int READ_SMS = 1;
    private ListView smsListView;
    private LinkedList<Sms> smsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        smsListView = findViewById(R.id.smsListView);

        if (ContextCompat.checkSelfPermission(SmsActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SmsActivity.this, new String[]{Manifest.permission.READ_SMS}, READ_SMS);
        } else {
            getSmsList();
        }
    }

    private void getSmsList() {
        smsList = new LinkedList<>();
        if (ContextCompat.checkSelfPermission(SmsActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SmsActivity.this, new String[]{Manifest.permission.READ_SMS}, READ_SMS);
        } else {
                ContentResolver contentResolver = getContentResolver();
                Cursor cursor = contentResolver.query(Telephony.Sms.CONTENT_URI,null,null,null, null);

                int allSms = cursor.getCount();
                while(cursor.moveToNext()) {
                    Sms sms = new Sms();
                    sms.setId(cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms._ID)));
                    sms.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS)));
                    sms.setContent(cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.BODY)));
                    sms.setTime(cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.DATE)));
                    sms.setState(cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.READ)));
                    if (cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.TYPE)).contains(String.valueOf(Telephony.Sms.MESSAGE_TYPE_INBOX))) {
                        sms.setFolder("inbox");
                    }
                    else {
                        sms.setFolder("outbox");
                    }
                    smsList.add(sms);
                }

                SmsAdapter smsAdapter = new SmsAdapter(this, R.layout.sms_item, smsList, getContactsMap());
                smsListView.setAdapter(smsAdapter);
        }
    }
    private TreeMap<String, String> getContactsMap() {
        TreeMap<String, String> map = new TreeMap<>();

        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
            Cursor phoneCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                    new String[]{id},
                    null);
            if (phoneCursor.moveToNext()) {
                final String number = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                map.put(number, name);
            }
        }
        return map;
    }
}