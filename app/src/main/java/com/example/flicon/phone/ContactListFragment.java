package com.example.flicon.phone;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.flicon.MainActivity;
import com.example.flicon.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class ContactListFragment extends Fragment {
    private static final int REQUEST_CALL = 1;
    private static final int PICK_CONTACT = 2;
    private static final int WRITE_CONTACTS = 4;
    private static String dial;
    private ListView listView;
    private LinkedList<Contact> contacts;
    private FloatingActionButton addContactButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_list_fragment, container, false);
        listView = (ListView) view.findViewById(R.id.contactListView);
        addContactButton = (FloatingActionButton) view.findViewById(R.id.addContactButton);
        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewContact(-1);
            }
        });

        registerForContextMenu(listView);

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, PICK_CONTACT);
        } else {
            getAllContacts();
        }
        return view;
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.contact_menu, menu);
//        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
//        Toast.makeText(getContext(), Long.toString(contacts.get(info.position).getId()), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
         final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.editContact:
                addNewContact(contacts.get(info.position).getId());
                return true;
            case R.id.deleteContact:
                new AlertDialog.Builder(getContext())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Delete contact")
                        .setMessage("Are you sure you want to delete "+contacts.get(info.position).getName()+"?")
                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ContentResolver contentResolver = getContext().getContentResolver();
                        ArrayList<ContentProviderOperation> ops = new ArrayList<>();
                        ops.add(ContentProviderOperation.newDelete(ContactsContract.RawContacts.CONTENT_URI)
                                .withSelection(ContactsContract.Data.CONTACT_ID + " = ?", new String[] {Long.toString(contacts.get(info.position).getId())}).build());
                        try {
                            contentResolver.applyBatch(ContactsContract.AUTHORITY, ops);
                        } catch (OperationApplicationException e) {
                            e.printStackTrace();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void getAllContacts() {
        contacts = new LinkedList<>();

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
                Contact contact = new Contact(name, number, Long.parseLong(id));
                contacts.add(contact);
            }

        }

        Collections.sort(contacts);
        ContactListAdapter contactListAdapter = new ContactListAdapter(getContext(), R.layout.contact_item, contacts);
        listView.setAdapter(contactListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contact contact = (Contact) parent.getItemAtPosition(position);
                dial = "tel:" + contact.getPhoneNumber();
                makePhoneCall();
            }
        });
    }

    public void makePhoneCall() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        } else {
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CALL:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makePhoneCall();
                } else {
                    Toast.makeText(getActivity(), "Call Phone Permission DENIED", Toast.LENGTH_SHORT).show();
                }
                break;
            case PICK_CONTACT:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getAllContacts();
                } else {
                    Toast.makeText(getActivity(), "Pick Contact Permission DENIED", Toast.LENGTH_SHORT).show();
                }
                break;
            case WRITE_CONTACTS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    addNewContact(-1);
                } else {
                    Toast.makeText(getActivity(), "Write Contact Permission DENIED", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void addNewContact(long id) {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_CONTACTS}, WRITE_CONTACTS);
        } else {
            Intent intent = new Intent(getActivity(), AddNewPhoneContactActivity.class);
                intent.putExtra("contactId", Long.toString(id));
                startActivityForResult(intent, 0);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
                if(resultCode == RESULT_OK) {
                    onDestroy();
                    getAllContacts();

        }
    }
}
