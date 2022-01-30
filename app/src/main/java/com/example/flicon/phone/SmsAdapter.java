package com.example.flicon.phone;

import android.content.Context;
import android.media.audiofx.LoudnessEnhancer;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.flicon.R;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.util.Date;

public class SmsAdapter extends ArrayAdapter<Sms> {
    private Context context;
    private int resource;
    private TreeMap<String, String> contactMap;

    public SmsAdapter(Context context, int resource, LinkedList<Sms> smsList, TreeMap contactMap) {
        super(context, resource, smsList);
        this.context = context;
        this.resource = resource;
        this.contactMap = contactMap;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String id = getItem(position).getId();
        String content = getItem(position).getContent();
        String address = getItem(position).getAddress();
        String folder = getItem(position).getFolder();
        String time = getItem(position).getTime();
        String state = getItem(position).getState();

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView = layoutInflater.inflate(resource, parent, false);

        TextView smsItemAddress = convertView.findViewById(R.id.smsItemAddress);
        TextView smsItemDate = convertView.findViewById(R.id.smsItemDate);
        TextView smsItemContent = convertView.findViewById(R.id.smsItemContent);
        ImageView smsItemDestination = convertView.findViewById(R.id.smsDestinationIcon);
        boolean found = false;

        for(Map.Entry<String, String> entry: contactMap.entrySet()) {
            if(PhoneNumberUtils.compare(entry.getKey(), address)) {
                smsItemAddress.setText(entry.getValue());
                found = true;
                break;
            }
        }
        if(!found) {
            smsItemAddress.setText(address);
        }

        Date date = new Date(Long.valueOf(time));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM HH:mm");

        smsItemDate.setText(dateFormat.format(date));
        smsItemContent.setText(content);
        if (folder.equals("inbox")) {
            smsItemDestination.setImageResource(R.drawable.call_received_icon);
        }
        else {
            smsItemDestination.setImageResource(R.drawable.made_call_icon);
        }
        return convertView;
    }


}
