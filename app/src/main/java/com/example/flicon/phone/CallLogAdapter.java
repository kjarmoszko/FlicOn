package com.example.flicon.phone;

import android.content.Context;
import android.provider.CallLog;
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
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

public class CallLogAdapter extends ArrayAdapter<Call> {
    private Context context;
    private int resource;
    private TreeMap<String, String> contactMap;

    public CallLogAdapter(Context context, int resource, LinkedList<Call> callList, TreeMap contactMap) {
        super(context, resource, callList);
        this.context = context;
        this.resource = resource;
        this.contactMap = contactMap;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String name = getItem(position).getName();
        String phoneNumber = getItem(position).getPhoneNumber();
        String callDate = getItem(position).getCallDate();
        String callDuration = getItem(position).getCallDuration();
        String callType = getItem(position).getCallType();

        Call call = new Call(name, phoneNumber, callType, callDate, callDuration);

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView = layoutInflater.inflate(resource, parent, false);

        TextView callName = (TextView) convertView.findViewById(R.id.callName);
        TextView callDetail = (TextView) convertView.findViewById(R.id.callDetail);
        ImageView callIcon = (ImageView) convertView.findViewById(R.id.callIcon);
        boolean found = false;

        for(Map.Entry<String, String > entry: contactMap.entrySet()) {
            if(PhoneNumberUtils.compare(entry.getKey(), phoneNumber)) {
                callName.setText(entry.getValue());
                found = true;
                break;
            }
        }
        if(!found) {
            callName.setText(phoneNumber);
        }

        Date date = new Date(Long.valueOf(callDate));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM HH:mm");

        callDetail.setText(dateFormat.format(date) + ", " + timeString(Long.parseLong(callDuration)));
        if (callType.equals(Integer.toString(CallLog.Calls.INCOMING_TYPE))) {
            callIcon.setImageResource(R.drawable.call_received_icon);
        } else if (callType.equals(Integer.toString(CallLog.Calls.OUTGOING_TYPE))) {
            callIcon.setImageResource(R.drawable.made_call_icon);
        } else {
            callIcon.setImageResource(R.drawable.call_missed_icon);
        }
        return convertView;
    }

    private String timeString(long seconds) {
        StringBuilder sb = new StringBuilder();
        long houres = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;
        if(houres > 0) {
            sb.append(houres+ " h ");
        }
        if(minutes > 0 || houres > 0) {
            sb.append(minutes+ " min ");
        }
        sb.append(seconds + " sec");
        return sb.toString();
    }


}
