package com.example.flicon.phone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.CellSignalStrength;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import com.example.flicon.MainActivity;
import com.example.flicon.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LockScreenActivity extends AppCompatActivity {
    private TextClock textClock;
    private ImageView unlockButton;
    private TextView dateTextView;
    private ImageView signalStrengthIcon;
    private ImageView batteryStatusIcon;
    private PhoneStateIconChanger phoneStateIconChanger;
    private TelephonyManager telephonyManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideButtons();
        setContentView(R.layout.activity_lock_screen);

        textClock = (TextClock) findViewById(R.id.clockLockScreen);
        unlockButton = (ImageView) findViewById(R.id.lockButtonLockScreen);
        dateTextView = (TextView) findViewById(R.id.dateLockScreen);
        signalStrengthIcon = (ImageView) findViewById(R.id.signalStrengthIconLockScreen);
        batteryStatusIcon = (ImageView) findViewById(R.id.batteryStatusIconLockScreen);

        textClock.setFormat12Hour("hh:mm");
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE dd/MM/yyyy");

        dateTextView.setText(dateFormat.format(date));

        unlockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        phoneStateIconChanger = new PhoneStateIconChanger();
        telephonyManager = (TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneStateIconChanger, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

        registerReceiver(batteryBroadcastReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideButtons();
    }

    private void hideButtons() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
    }

    private class PhoneStateIconChanger extends PhoneStateListener {
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (signalStrength.getLevel() == CellSignalStrength.SIGNAL_STRENGTH_NONE_OR_UNKNOWN) {
                    signalStrengthIcon.setImageResource(R.drawable.signal_0_bar_icon);
                } else if (signalStrength.getLevel() >= CellSignalStrength.SIGNAL_STRENGTH_GREAT) {
                    signalStrengthIcon.setImageResource(R.drawable.signal_4_bar_icon);
                } else if (signalStrength.getLevel() >= CellSignalStrength.SIGNAL_STRENGTH_GOOD) {
                    signalStrengthIcon.setImageResource(R.drawable.signal_3_bar_icon);
                } else if (signalStrength.getLevel() >= CellSignalStrength.SIGNAL_STRENGTH_MODERATE) {
                    signalStrengthIcon.setImageResource(R.drawable.signal_2_bar_icon);
                } else if (signalStrength.getLevel() >= CellSignalStrength.SIGNAL_STRENGTH_POOR) {
                    signalStrengthIcon.setImageResource(R.drawable.signal_1_bar_icon);
                }
            }
        }
    }

    private void batteryChangeIcon(int level, int scale, boolean plugged) {
        float batteryPower = level/(float)scale;
        if (!plugged) {
            if (batteryPower >= 0.95) {
                batteryStatusIcon.setImageResource(R.drawable.battery_full_icon);
            } else if (batteryPower >= 0.85) {
                batteryStatusIcon.setImageResource(R.drawable.battery_90_icon);
            } else if (batteryPower >= 0.75) {
                batteryStatusIcon.setImageResource(R.drawable.battery_80_icon);
            } else if (batteryPower >= 0.55) {
                batteryStatusIcon.setImageResource(R.drawable.battery_60_icon);
            } else if (batteryPower >= 0.45) {
                batteryStatusIcon.setImageResource(R.drawable.battery_50_icon);
            } else if (batteryPower >= 0.25) {
                batteryStatusIcon.setImageResource(R.drawable.battery_30_icon);
            } else if (batteryPower >= 0.15) {
                batteryStatusIcon.setImageResource(R.drawable.battery_20_icon);
            } else {
                batteryStatusIcon.setImageResource(R.drawable.battery_low_icon);
            }
        } else {
            if (batteryPower >= 0.95) {
                batteryStatusIcon.setImageResource(R.drawable.battery_charging_full_icon);
            } else if (batteryPower >= 0.85) {
                batteryStatusIcon.setImageResource(R.drawable.battery_charging_90_icon);
            } else if (batteryPower >= 0.75) {
                batteryStatusIcon.setImageResource(R.drawable.battery_charging_80_icon);
            } else if (batteryPower >= 0.55) {
                batteryStatusIcon.setImageResource(R.drawable.battery_charging_60_icon);
            } else if (batteryPower >= 0.45) {
                batteryStatusIcon.setImageResource(R.drawable.battery_charging_50_icon);
            } else if (batteryPower >= 0.25) {
                batteryStatusIcon.setImageResource(R.drawable.battery_charging_30_icon);
            } else {
                batteryStatusIcon.setImageResource(R.drawable.battery_charging_20_icon);
            }
        }
    }

    private BroadcastReceiver batteryBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING;
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            batteryChangeIcon(level, scale, isCharging);
        }
    };
}