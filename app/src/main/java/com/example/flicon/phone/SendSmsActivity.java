package com.example.flicon.phone;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.flicon.MainActivity;
import com.example.flicon.R;
import com.example.flicon.flicmanagment.Functionalities;

public class SendSmsActivity extends AppCompatActivity {

    private static final int SEND_SMS = 7;
    private EditText telephoneNumber;
    private EditText smsContent;
    private Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms);

        telephoneNumber = (EditText) findViewById(R.id.smsNumber);
        smsContent = (EditText) findViewById(R.id.smsContent);
        sendButton = (Button) findViewById(R.id.sendSMS);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = telephoneNumber.getText().toString();
                String message = smsContent.getText().toString();
                if (number.length() > 0 && message.length() > 0)
                    if (ContextCompat.checkSelfPermission(SendSmsActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(SendSmsActivity.this, new String[]{Manifest.permission.SEND_SMS}, SEND_SMS);
                    } else if (ContextCompat.checkSelfPermission(SendSmsActivity.this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(SendSmsActivity.this, new String[]{Manifest.permission.RECEIVE_SMS}, SEND_SMS); }

                else
                        sendSMS(number, message);
                else
                    Toast.makeText(getApplicationContext(), "Enter phone number and message", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void sendSMS(String number, String message) {
        PendingIntent sentPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT"), 0);
        PendingIntent deliveredPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_DELIVERED"), 0);

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getApplicationContext(), "SMS sent", Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getApplicationContext(), "No service", Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getApplicationContext(), "Radio OFF", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "Unknown error", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        }, new IntentFilter("SMS_SENT"));
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getApplicationContext(), "SMS delivered", Toast.LENGTH_LONG).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getApplicationContext(), "SMS not delivered", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        }, new IntentFilter("SMS_DELIVERED"));

        SmsManager smsManager = SmsManager.getDefault();

        smsManager.sendTextMessage(number, null, message, sentPendingIntent, deliveredPendingIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode) {
            case SEND_SMS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this, "SMS_SEND Permission DENIED", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}