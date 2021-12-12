package com.example.flicon.flicmanagment;

import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.flicon.database.DatabaseHelper;


public class FlicItBroadcastReceiver {}/*extends FlicBroadcastReceiver {
    @Override
    protected void onRequestAppCredentials(Context context) {
        FlicConfig.setFlicCredentials();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onButtonSingleOrDoubleClickOrHold(Context context, FlicButton button, boolean wasQueued, int timeDiff, boolean isSingleClick, boolean isDoubleClick, boolean isHold) {
        if (isSingleClick) {
            FunctionType functionType = FunctionType.get(DatabaseHelper.getInstance(context).getFunction(button.getButtonId(), 1).getType());
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
                    Functionalities.getInstance(context).emergencyCall(DatabaseHelper.getInstance(context).getFunction(button.getButtonId(),1).getNumber());
                    break;
                case EMERGENCY_SMS:
                    String number = DatabaseHelper.getInstance(context).getFunction(button.getButtonId(), 1).getNumber();
                    String message = DatabaseHelper.getInstance(context).getFunction(button.getButtonId(), 1).getMessage();
                    Functionalities.getInstance(context).emergencySms(number, message);
                    break;
                default:
                    break;
            }
        } else if (isDoubleClick) {
            FunctionType functionType = FunctionType.get(DatabaseHelper.getInstance(context).getFunction(button.getButtonId(), 2).getType());
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
                    Functionalities.getInstance(context).emergencyCall(DatabaseHelper.getInstance(context).getFunction(button.getButtonId(),0).getNumber());
                    break;
                case EMERGENCY_SMS:
                    String number = DatabaseHelper.getInstance(context).getFunction(button.getButtonId(), 2).getNumber();
                    String message = DatabaseHelper.getInstance(context).getFunction(button.getButtonId(), 2).getMessage();
                    Functionalities.getInstance(context).emergencySms(number, message);
                    break;
                default:
                    break;
            }
        } else if (isHold) {
            FunctionType functionType = FunctionType.get(DatabaseHelper.getInstance(context).getFunction(button.getButtonId(), 0).getType());
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
                    Functionalities.getInstance(context).emergencyCall(DatabaseHelper.getInstance(context).getFunction(button.getButtonId(),2).getNumber());
                    break;
                case EMERGENCY_SMS:
                    String number = DatabaseHelper.getInstance(context).getFunction(button.getButtonId(), 0).getNumber();
                    String message = DatabaseHelper.getInstance(context).getFunction(button.getButtonId(), 0).getMessage();
                    Functionalities.getInstance(context).emergencySms(number, message);
                    break;
                default:
                    break;
            }
        }
    }
}
*/