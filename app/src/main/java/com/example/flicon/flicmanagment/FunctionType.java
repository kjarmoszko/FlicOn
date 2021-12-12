package com.example.flicon.flicmanagment;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum FunctionType {
    NONE("0","none"),
    FLASHLIGHT("1", "flashlight"),
    FLASH("3", "flashing"),
    SOUND_ALARM("4", "sound alarm"),
    VIBRATE("5", "vibrate"),
    FIND_PHONE("2", "find my phone"),
    GOOGLE_ASSISTANT("6", "run google assistant"),
    PICK_UP_PHONE("7", "pick up phone"),
    CHANGE_VOLUME("8", "mute/unmute sound"),
    EMERGENCY_CALL("9", "emergency call"),
    EMERGENCY_SMS("10", "emergency sms");

    private String type;
    private String name;

    private static final Map<String, FunctionType> FUNCTION_NAME_MAP;

    FunctionType(String type, String name){
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }
    public String getName() {
        return name;
    }
    static {
        Map<String, FunctionType> map = new HashMap<String, FunctionType>();
        for(FunctionType functionType: FunctionType.values()){
            map.put(functionType.getType(), functionType);
        }
        FUNCTION_NAME_MAP = Collections.unmodifiableMap(map);
    }

    public static FunctionType get (String type){
        return FUNCTION_NAME_MAP.get(type);
    }
}
