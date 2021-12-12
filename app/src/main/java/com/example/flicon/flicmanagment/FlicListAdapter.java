package com.example.flicon.flicmanagment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.flicon.R;
import com.example.flicon.database.Flic;
import com.example.flicon.phone.Contact;

import java.util.ArrayList;

public class FlicListAdapter extends ArrayAdapter<Flic> {
    private Context context;
    private int resource;

    public FlicListAdapter(@NonNull Context context, int resource, ArrayList<Flic> flicList) {
        super(context, resource, flicList);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String name = getItem(position).getName();
        String mac = getItem(position).getMac();

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView = layoutInflater.inflate(resource, parent, false);

        TextView flicName = (TextView) convertView.findViewById(R.id.flicName);

        flicName.setText(name);

        return convertView;
    }
}
