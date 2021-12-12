package com.example.flicon.phone;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.flicon.R;

import java.util.LinkedList;
import java.util.TreeMap;

public class CallLogFragment extends Fragment {
    private static String dial;
    private static final int REQUEST_CALL = 1;
    private static final int PICK_CONTACT = 2;
    private static final int READ_LOG = 3;
    private ListView listView;
    private TreeMap<String, String> contactMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.call_log_fragment, container, false);
        listView = (ListView) view.findViewById(R.id.callLogListView);

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, PICK_CONTACT);
        } else {
            contactMap = getContactsMap();
        }
        getCallLogs();

        return view;
    }

    private TreeMap<String, String> getContactsMap() {
        TreeMap<String, String> map = new TreeMap<>();

        ContentResolver contentResolver = getContext().getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
            Cursor phoneCursor = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
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

    @Override
    public void onResume() {
        super.onResume();
        getCallLogs();
    }

    private void getCallLogs() {
        LinkedList<Call> callList = new LinkedList<>();
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CALL_LOG}, READ_LOG);
        } else {
            Cursor cursor = getActivity().getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, null);
            while (cursor.moveToNext()) {
                Call call = new Call();
                call.setPhoneNumber(cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER)));
                call.setCallDate(cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE)));
                call.setCallDuration(cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION)));
                call.setCallType(cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE)));
                call.setName(cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)));
                callList.addFirst(call);
            }

            CallLogAdapter callLogAdapter = new CallLogAdapter(getContext(), R.layout.call_log_item, callList, contactMap);
            listView.setAdapter(callLogAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Call call = (Call) parent.getItemAtPosition(position);
                    dial = "tel:" + call.getPhoneNumber();
                    makePhoneCall();
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CALL:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makePhoneCall();
                } else {
                    Toast.makeText(getContext(), "Call Phone Permission DENIED", Toast.LENGTH_SHORT).show();
                }
                break;
            case PICK_CONTACT:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    contactMap = getContactsMap();
                } else {
                    Toast.makeText(getContext(), "Pick Contact Permission DENIED", Toast.LENGTH_SHORT).show();
                }
                break;
            case READ_LOG:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCallLogs();
                } else {
                    Toast.makeText(getContext(), "Call log Permission DENIED", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    public void makePhoneCall() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        } else {
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }


}
