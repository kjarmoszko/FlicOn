package com.example.flicon.flicmanagment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.telecom.TelecomManager;
import android.telephony.SmsManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.flicon.MainActivity;
import com.example.flicon.R;

import java.util.ArrayList;

public class Functionalities {
    private Context context;

    private boolean flashlightOn = false;

    private static Functionalities instance;

    private Functionalities(Context context) {
        this.context = context;
    }

    public static synchronized Functionalities getInstance(Context context) {
        if (instance == null)
            instance = new Functionalities(context);
        return instance;
    }

//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    protected void flashLightOn() {
//        CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
//
//        try {
//            String cameraId = cameraManager.getCameraIdList()[0];
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                cameraManager.setTorchMode(cameraId, true);
//            }
//        } catch (CameraAccessException e) {
//
//        }
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    protected void flashLightOff() {
//        CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
//        try {
//            String cameraId = cameraManager.getCameraIdList()[0];
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                cameraManager.setTorchMode(cameraId, false);
//            }
//        } catch (CameraAccessException e) {
//
//        }
//    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void blinkFlash() {
        CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        String myString = "0101010101";
        long blinkDelay = 50; //Delay in ms
        for (int i = 0; i < myString.length(); i++) {
            if (myString.charAt(i) == '0') {
                try {
                    String cameraId = cameraManager.getCameraIdList()[0];
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        cameraManager.setTorchMode(cameraId, true);
                    }
                } catch (CameraAccessException e) {

                }
            } else {
                try {
                    String cameraId = cameraManager.getCameraIdList()[0];
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        cameraManager.setTorchMode(cameraId, false);
                    }
                } catch (CameraAccessException e) {

                }
            }
            try {
                Thread.sleep(blinkDelay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected void soundAlarm() {
        MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.car_alarm);
        mediaPlayer.start();
    }

    protected void setMaxVolume() {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume/10, AudioManager.FLAG_SHOW_UI); //10% for test only
        MainActivity.muteButton.setImageResource(R.drawable.volume_on_icon);
    }


    protected void vibrate() {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
        }
    }

    public void flashlightService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
            try {
                String cameraId = cameraManager.getCameraIdList()[0];
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (flashlightOn) {
                        cameraManager.setTorchMode(cameraId, false);
                        flashlightOn = false;
                        MainActivity.flashlightButton.setImageResource(R.drawable.flashlight_off_icon);
                    } else {
                        cameraManager.setTorchMode(cameraId, true);
                        flashlightOn = true;
                        MainActivity.flashlightButton.setImageResource(R.drawable.flashlight_on_icon);
                    }
                }

            } catch (CameraAccessException e) {

            }
        }
    }

    protected void findPhone() {
        Intent intent = new Intent(context, AlarmActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void panic() {
        MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.car_alarm);
        int delay = 500;
        int alarmDuration = mediaPlayer.getDuration();
        setMaxVolume();
        mediaPlayer.start();
        for (int i = 0; i < alarmDuration / delay; i++) {
            flashlightService();
            if (i % 2 == 0)
                vibrate();
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                mediaPlayer.stop();
                if (flashlightOn) {
                    flashlightService();
                }
                break;
            }
        }
    }

    protected void runGoogleAssistant() {
        setMaxVolume();
        context.startActivity(new Intent(Intent.ACTION_VOICE_COMMAND).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//        Intent intent = new Intent();
//        intent.setClassName("com.google.android.googlequicksearchbox", "com.google.android.apps.gsa.staticplugins.opa.hq.OpaHqActivity");
//        if(intent == null) {
//            intent = new Intent(Intent.ACTION_VIEW);
//            intent.setData(Uri.parse("market://details?id=com.google.android.googlequicksearchbox"));
//        }
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void pickUpCall() {
        TelecomManager telecomManager = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            telecomManager.acceptRingingCall();
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            audioManager.setMode(AudioManager.MODE_IN_CALL);
            audioManager.setSpeakerphoneOn(true);

//            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
//            if (!audioManager.isSpeakerphoneOn()) {
//                audioManager.setMode(AudioManager.MODE_IN_CALL);
//                audioManager.setSpeakerphoneOn(true);
//            }
        }
    }


    public void speakerService() {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        if(audioManager.getStreamVolume(AudioManager.STREAM_RING) < audioManager.getStreamMaxVolume(AudioManager.STREAM_RING)) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),AudioManager.FLAG_SHOW_UI);
                audioManager.setStreamVolume(AudioManager.STREAM_RING,audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),AudioManager.FLAG_SHOW_UI);
                MainActivity.muteButton.setImageResource(R.drawable.volume_on_icon);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMinVolume(AudioManager.STREAM_MUSIC), AudioManager.FLAG_SHOW_UI);
                audioManager.setStreamVolume(AudioManager.STREAM_RING, audioManager.getStreamMinVolume(AudioManager.STREAM_RING), AudioManager.FLAG_SHOW_UI);
                MainActivity.muteButton.setImageResource(R.drawable.volume_off_icon);
            }
        }
    }

    @SuppressLint("MissingPermission")
    protected void emergencyCall(String number) {
        context.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+number)).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    protected void emergencySms(String number, String message) {
        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> parts = smsManager.divideMessage(message);
        smsManager.sendMultipartTextMessage(number,null, parts, null, null);
        Toast.makeText(context, "SMS send!",Toast.LENGTH_SHORT).show();
    }



}
