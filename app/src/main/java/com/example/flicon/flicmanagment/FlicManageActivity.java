package com.example.flicon.flicmanagment;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.flicon.R;
import com.example.flicon.database.DatabaseHelper;
import com.example.flicon.database.Flic;

import java.util.ArrayList;

import io.flic.flic2libandroid.Flic2Button;
import io.flic.flic2libandroid.Flic2ButtonListener;
import io.flic.flic2libandroid.Flic2Manager;
import io.flic.flic2libandroid.Flic2ScanCallback;

/*import io.flic.lib.FlicAppNotInstalledException;
import io.flic.lib.FlicBroadcastReceiverFlags;
import io.flic.lib.FlicButton;
import io.flic.lib.FlicManager;
import io.flic.lib.FlicManagerInitializedCallback;*/

public class FlicManageActivity extends AppCompatActivity {
    private LinearLayout linearLayout;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flic_manage);


        listView = (ListView) findViewById(R.id.flicList);
        getConnectedFlics();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getConnectedFlics();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void grabButton(View v) {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        Flic2Manager.getInstance().startScan(new Flic2ScanCallback() {
            @Override
            public void onDiscoveredAlreadyPairedButton(Flic2Button button) {
                Flic2Manager.getInstance().forgetButton(button);
            }

            @Override
            public void onDiscovered(String bdAddr) {

            }

            @Override
            public void onConnected() {

            }

            @Override
            public void onComplete(int result, int subCode, Flic2Button button) {
                if (result == Flic2ScanCallback.RESULT_SUCCESS) {
                    DatabaseHelper.getInstance(FlicManageActivity.this).createFlic(button.getUuid());
                    Toast.makeText(FlicManageActivity.this, "Grabbed a button", Toast.LENGTH_SHORT).show();
                    addButtonListener(button, FlicManageActivity.this);
                    getConnectedFlics();
                } else {
                    Toast.makeText(FlicManageActivity.this, "Did not grab any button", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void addButtonListener(Flic2Button button, Context context) {
        button.addListener(new Flic2ButtonListener() {
            @Override
            public void onButtonSingleOrDoubleClickOrHold(Flic2Button button, boolean wasQueued, boolean lastQueued, long timestamp, boolean isSingleClick, boolean isDoubleClick, boolean isHold) {
                if (isSingleClick) {
                    FunctionType functionType = FunctionType.get(DatabaseHelper.getInstance(context).getFunction(button.getUuid(), 1).getType());
                    Toast.makeText(context, "Single Click", Toast.LENGTH_SHORT).show();
                    switch (functionType) {
                        case NONE:
                            break;
                        case FLASHLIGHT:
                            Functionalities.getInstance(context).flashlightService();
                            break;
                        case FIND_PHONE:
                            Functionalities.getInstance(context).findPhone();
                            break;
                        case FLASH:
                            Functionalities.getInstance(context).blinkFlash();
                            break;
                        case SOUND_ALARM:
                            Functionalities.getInstance(context).soundAlarm();
                            break;
                        case VIBRATE:
                            Functionalities.getInstance(context).vibrate();
                            break;
                        case GOOGLE_ASSISTANT:
                            Functionalities.getInstance(context).runGoogleAssistant();
                            break;
                        case PICK_UP_PHONE:
                            Functionalities.getInstance(context).pickUpCall();
                            break;
                        case CHANGE_VOLUME:
                            Functionalities.getInstance(context).speakerService();
                            break;
                        case EMERGENCY_CALL:
                            Functionalities.getInstance(context).emergencyCall(DatabaseHelper.getInstance(context).getFunction(button.getUuid(),1).getNumber());
                            break;
                        case EMERGENCY_SMS:
                            String number = DatabaseHelper.getInstance(context).getFunction(button.getUuid(), 1).getNumber();
                            String message = DatabaseHelper.getInstance(context).getFunction(button.getUuid(), 1).getMessage();
                            Functionalities.getInstance(context).emergencySms(number, message);
                            break;
                        default:
                            break;
                    }
                } else if (isDoubleClick) {
                    FunctionType functionType = FunctionType.get(DatabaseHelper.getInstance(context).getFunction(button.getUuid(), 2).getType());
                    Toast.makeText(context, "Double Click", Toast.LENGTH_SHORT).show();
                    switch (functionType) {
                        case NONE:
                            break;
                        case FLASHLIGHT:
                            Functionalities.getInstance(context).flashlightService();
                            break;
                        case FIND_PHONE:
                            Functionalities.getInstance(context).findPhone();
                            break;
                        case FLASH:
                            Functionalities.getInstance(context).blinkFlash();
                            break;
                        case SOUND_ALARM:
                            Functionalities.getInstance(context).soundAlarm();
                            break;
                        case VIBRATE:
                            Functionalities.getInstance(context).vibrate();
                            break;
                        case GOOGLE_ASSISTANT:
                            Functionalities.getInstance(context).runGoogleAssistant();
                            break;
                        case PICK_UP_PHONE:
                            Functionalities.getInstance(context).pickUpCall();
                            break;
                        case CHANGE_VOLUME:
                            Functionalities.getInstance(context).speakerService();
                            break;
                        case EMERGENCY_CALL:
                            Functionalities.getInstance(context).emergencyCall(DatabaseHelper.getInstance(context).getFunction(button.getUuid(),0).getNumber());
                            break;
                        case EMERGENCY_SMS:
                            String number = DatabaseHelper.getInstance(context).getFunction(button.getUuid(), 2).getNumber();
                            String message = DatabaseHelper.getInstance(context).getFunction(button.getUuid(), 2).getMessage();
                            Functionalities.getInstance(context).emergencySms(number, message);
                            break;
                        default:
                            break;
                    }
                } else if (isHold) {
                    FunctionType functionType = FunctionType.get(DatabaseHelper.getInstance(context).getFunction(button.getUuid(), 0).getType());
                    Toast.makeText(context, "Hold", Toast.LENGTH_SHORT).show();
                    switch (functionType) {
                        case NONE:
                            break;
                        case FLASHLIGHT:
                            Functionalities.getInstance(context).flashlightService();
                            break;
                        case FIND_PHONE:
                            Functionalities.getInstance(context).findPhone();
                            break;
                        case FLASH:
                            Functionalities.getInstance(context).blinkFlash();
                            break;
                        case SOUND_ALARM:
                            Functionalities.getInstance(context).soundAlarm();
                            break;
                        case VIBRATE:
                            Functionalities.getInstance(context).vibrate();
                            break;
                        case GOOGLE_ASSISTANT:
                            Functionalities.getInstance(context).runGoogleAssistant();
                            break;
                        case PICK_UP_PHONE:
                            Functionalities.getInstance(context).pickUpCall();
                            break;
                        case CHANGE_VOLUME:
                            Functionalities.getInstance(context).speakerService();
                            break;
                        case EMERGENCY_CALL:
                            Functionalities.getInstance(context).emergencyCall(DatabaseHelper.getInstance(context).getFunction(button.getUuid(),2).getNumber());
                            break;
                        case EMERGENCY_SMS:
                            String number = DatabaseHelper.getInstance(context).getFunction(button.getUuid(), 0).getNumber();
                            String message = DatabaseHelper.getInstance(context).getFunction(button.getUuid(), 0).getMessage();
                            Functionalities.getInstance(context).emergencySms(number, message);
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }

    /*@Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FlicManager.getInstance(this, new FlicManagerInitializedCallback() {
            @Override
            public void onInitialized(FlicManager manager) {
                FlicButton button = manager.completeGrabButton(requestCode, resultCode, data);
                if (button != null) {
                    button.registerListenForBroadcast(FlicBroadcastReceiverFlags.CLICK_OR_DOUBLE_CLICK_OR_HOLD);
                    DatabaseHelper.getInstance(FlicManageActivity.this).createFlic(button.getButtonId());
                    Toast.makeText(FlicManageActivity.this, "Grabbed a button", Toast.LENGTH_SHORT).show();
                    getConnectedFlics();
                } else {
                    Toast.makeText(FlicManageActivity.this, "Did not grab any button", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }*/

    private void getConnectedFlics() {

        ArrayList<Flic> flicList = new ArrayList<Flic>();

        Cursor cursor = DatabaseHelper.getInstance(this).getAllData();
        while (cursor.moveToNext()) {
            Flic flic = new Flic();
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableFlic.COLUMN_NAME));
            if(name == null) {
                flic.setName("No name");
            }
            else {
                flic.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableFlic.COLUMN_NAME)));
            }
            flic.setMac(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableFlic.COLUMN_MAC)));
            flicList.add(flic);

        }

        FlicListAdapter flicListAdapter = new FlicListAdapter(this, R.layout.flic_img, flicList);
        listView.setAdapter(flicListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(FlicManageActivity.this, ClickChangeActivity.class);
                    intent.putExtra("mac", parent.getItemAtPosition(position).toString());
                    startActivity(intent);
            }
        });
    }

    @SuppressLint("MissingSuperCall")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                grabButton(findViewById(R.id.linear));
            } else {
                Toast.makeText(getApplicationContext(), "Scanning needs Location permission!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
