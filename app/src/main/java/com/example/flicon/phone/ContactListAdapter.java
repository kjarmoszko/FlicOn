package com.example.flicon.phone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.flicon.R;

import java.util.LinkedList;
import java.util.TreeMap;

public class ContactListAdapter extends ArrayAdapter<Contact> {
    private Context context;
    private int resource;

    public ContactListAdapter(Context context, int resource, LinkedList<Contact> contactList){
        super(context, resource, contactList);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String name = getItem(position).getName();
        String number = getItem(position).getPhoneNumber();
        long id = getItem(position).getId();

        Contact contact = new Contact(name, number, id);

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView = layoutInflater.inflate(resource, parent, false);

        TextView contactName = (TextView) convertView.findViewById(R.id.contactItem);
        TextView contactNumber = (TextView) convertView.findViewById(R.id.contectNumber);

        contactName.setText(name);
        contactNumber.setText(number);

        return convertView;
    }
}
