package com.example.flicon.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Toast.makeText(context, "SMS received", Toast.LENGTH_LONG).show();
        }
    }
}
