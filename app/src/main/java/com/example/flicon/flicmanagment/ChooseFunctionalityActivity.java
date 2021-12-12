package com.example.flicon.flicmanagment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.flicon.R;

public class ChooseFunctionalityActivity extends AppCompatActivity {
    Button flashlightButton, pickUpPhoneButton, changeVolumeButton, emergencyCallButton, emergencySmsButton;
    //    Button noneButton, flashButton, cancelButton;
    Intent functionality;
    private static final int REQUEST_CALL = 1;
    private static final int CAMERA_REQUEST = 3;
    private static final int PICK_UP_PHONE = 4;
    private static final int MODIFY_AUDIO = 5;
    private static final int PHONE_STATE = 6;
    private static final int REQUEST_SMS = 7;

    private static final int EMERGENCY_CALL = 1;
    private static final int EMERGENCY_SMS = 2;

    private String functionId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_functionality);

//        noneButton = (Button) findViewById(R.id.noneButton);
        flashlightButton = (Button) findViewById(R.id.flashlightButton);
//        flashButton = (Button) findViewById(R.id.flashButton);
//        cancelButton = (Button) findViewById(R.id.cancelButton);

        functionality = new Intent();
        flashlightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flashlightButtonClicked();
            }
        });
        pickUpPhoneButton = (Button) findViewById(R.id.pickUpPhoneButton);
        pickUpPhoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickUpPhoneClicked();
            }
        });
        changeVolumeButton = (Button) findViewById(R.id.changeVolumeButton);
        changeVolumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeVolumeButtonClicked();
            }
        });
        emergencyCallButton = (Button) findViewById(R.id.emergencyCallButton);
        emergencyCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emergencyCallButtonClicked();
            }
        });
        emergencySmsButton = (Button) findViewById(R.id.emergencySmsButton);
        emergencySmsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emergencySmsButtonClicked();
            }
        });

        Intent intent = getIntent();
        functionId = intent.getExtras().getString("functionId");

    }

    public void cancelButtonClicked(View v) {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void noneButtonClicked(View v) {
        functionality.putExtra("functionality", FunctionType.NONE.getType());
        setResult(RESULT_OK, functionality);
        finish();
    }

    public void flashlightButtonClicked() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);
        } else {
            functionality.putExtra("functionality", FunctionType.FLASHLIGHT.getType());
            setResult(RESULT_OK, functionality);
            finish();
        }
    }

    public void flashButtonClicked(View v) {
        functionality.putExtra("functionality", FunctionType.FLASH.getType());
        setResult(RESULT_OK, functionality);
        finish();
    }

    public void soundAlarmButtonClicked(View v) {
        functionality.putExtra("functionality", FunctionType.SOUND_ALARM.getType());
        setResult(RESULT_OK, functionality);
        finish();
    }

    public void vibrateButtonClicked(View v) {
        functionality.putExtra("functionality", FunctionType.VIBRATE.getType());
        setResult(RESULT_OK, functionality);
        finish();
    }

    public void findPhoneClicked(View v) {
        functionality.putExtra("functionality", FunctionType.FIND_PHONE.getType());
        setResult(RESULT_OK, functionality);
        finish();
    }

    public void googleAssistantClicked(View v) {
        functionality.putExtra("functionality", FunctionType.GOOGLE_ASSISTANT.getType());
        setResult(RESULT_OK, functionality);
        finish();
    }

    public void pickUpPhoneClicked() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ANSWER_PHONE_CALLS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ANSWER_PHONE_CALLS}, PICK_UP_PHONE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.MODIFY_AUDIO_SETTINGS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.MODIFY_AUDIO_SETTINGS}, MODIFY_AUDIO);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, PHONE_STATE);
        }
        functionality.putExtra("functionality", FunctionType.PICK_UP_PHONE.getType());
        setResult(RESULT_OK, functionality);
        finish();
    }

    public void changeVolumeButtonClicked() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.MODIFY_AUDIO_SETTINGS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.MODIFY_AUDIO_SETTINGS}, MODIFY_AUDIO);
        } else {
            functionality.putExtra("functionality", FunctionType.CHANGE_VOLUME.getType());
            setResult(RESULT_OK, functionality);
            finish();
        }
    }

    public void emergencyCallButtonClicked() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        } else {
            Intent intent = new Intent(ChooseFunctionalityActivity.this, EmergencyPhoneActivity.class);
            intent.putExtra("functionId", functionId+",call");
            startActivityForResult(intent, EMERGENCY_CALL);
        }
    }

    public void emergencySmsButtonClicked() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, REQUEST_SMS);
        } else {
            Intent intent = new Intent(ChooseFunctionalityActivity.this, EmergencyPhoneActivity.class);
            intent.putExtra("functionId", functionId+",sms");
            startActivityForResult(intent, EMERGENCY_SMS);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    flashlightButtonClicked();
                } else {
                    Toast.makeText(ChooseFunctionalityActivity.this, "Flashlight Permission DENIED", Toast.LENGTH_SHORT).show();
                }
                break;
            case PICK_UP_PHONE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    pickUpPhoneClicked();
                } else {
                    Toast.makeText(ChooseFunctionalityActivity.this, "PickUpPhone Permission DENIED", Toast.LENGTH_SHORT).show();
                }
                break;
            case MODIFY_AUDIO:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    pickUpPhoneClicked();
                } else {
                    Toast.makeText(ChooseFunctionalityActivity.this, "ModifyAudio Permission DENIED", Toast.LENGTH_SHORT).show();
                }
                break;
            case PHONE_STATE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    pickUpPhoneClicked();
                } else {
                    Toast.makeText(ChooseFunctionalityActivity.this, "PhoneState Permission DENIED", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_CALL:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    emergencyCallButtonClicked();
                } else {
                    Toast.makeText(ChooseFunctionalityActivity.this, "CallPhone Permission DENIED", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_SMS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    emergencySmsButtonClicked();
                } else {
                    Toast.makeText(ChooseFunctionalityActivity.this, "SendSms Permission DENIED", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case EMERGENCY_CALL:
                functionality.putExtra("functionality", FunctionType.EMERGENCY_CALL.getType());
                setResult(RESULT_OK, functionality);
                finish();
                break;
            case EMERGENCY_SMS:
                functionality.putExtra("functionality", FunctionType.EMERGENCY_SMS.getType());
                setResult(RESULT_OK, functionality);
                finish();
                break;
        }
    }

}
