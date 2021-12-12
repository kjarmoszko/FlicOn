package com.example.flicon.phone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentProviderOperation;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.flicon.R;

import java.util.ArrayList;

public class AddNewPhoneContactActivity extends AppCompatActivity {
    private ImageView saveContactButton;
    private EditText newContactName;
    private EditText newContactNumber;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_phone_contact);

        saveContactButton = (ImageView) findViewById(R.id.saveContactButton);
        newContactName = (EditText) findViewById(R.id.newContactName);
        newContactNumber = (EditText) findViewById(R.id.newContactNumber);
        id = new String();
        Intent intent = getIntent();
        id = intent.getStringExtra("contactId");

        if (!id.equals("-1")) {
            Cursor phoneCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                    new String[]{id},
                    null);
            if (phoneCursor.moveToNext()) {
                String number = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String name = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                newContactNumber.setText(number);
                newContactName.setText(name);
            }
            saveContactButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        updateContact();
                    finish();
                }
            });
        } else {

            saveContactButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveNewContact();
                    finish();
                }
            });
        }
    }

    private void saveNewContact() {
        String name = newContactName.getText().toString();
        String number = newContactNumber.getText().toString();

        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        ops.add(ContentProviderOperation.newInsert(
                ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        if (name != null) {
            ops.add(ContentProviderOperation.newInsert(
                    ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(
                            ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                            name).build());
        }

        if (number != null) {
            ops.add(ContentProviderOperation.
                    newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, number)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .build());
        }

        try {
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            setResult(RESULT_OK);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateContact() {
        String name = newContactName.getText().toString();
        String number = newContactNumber.getText().toString();

        ArrayList<ContentProviderOperation> ops = new ArrayList<>();
        ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                .withSelection(ContactsContract.Data.CONTACT_ID + " = ? AND " +
                        ContactsContract.Data.MIMETYPE + "='" +
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE + "'", new String[]{id})
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name).build());

        ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
        .withSelection(ContactsContract.Data.CONTACT_ID + " = ? AND " +
                ContactsContract.Data.MIMETYPE + "='" +
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "'", new String[]{id})
        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, number).build());

        try {
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            setResult(RESULT_OK);
            finish();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

}
