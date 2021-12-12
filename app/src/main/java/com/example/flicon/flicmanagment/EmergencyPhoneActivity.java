package com.example.flicon.flicmanagment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flicon.R;
import com.example.flicon.database.DatabaseHelper;
import com.example.flicon.database.Function;

public class EmergencyPhoneActivity extends AppCompatActivity {
    private EditText numberField;
    private ImageView acceptButton;
    private String functionId;
    private EditText messageField;
    private TextView emergencyTextView;
    private String functionType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_phone);

        numberField = (EditText) findViewById(R.id.emergencyNumber);
        acceptButton = (ImageView) findViewById(R.id.acceptButton);
        messageField = (EditText) findViewById(R.id.emergencyMessage);
        emergencyTextView = (TextView) findViewById(R.id.emergencySmsTextView);

        Intent intent = getIntent();
        String str = intent.getExtras().getString("functionId");
        String[] strTab = str.split(",");
        functionId = strTab[0];
        functionType = strTab[1];
        Function function = DatabaseHelper.getInstance(this).getFunction(Long.parseLong(functionId));
        numberField.setText(function.getNumber());
        Toast.makeText(this, function.getType(), Toast.LENGTH_SHORT).show();
        if(functionType.equals("call")){
            messageField.setVisibility(View.GONE);
            emergencyTextView.setVisibility(View.GONE);
            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addEmergencyPhone();
                }
            });
        } else {
            messageField.setText(function.getMessage());
            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addEmergencyMessege();
                }
            });
        }


    }

    public void addEmergencyPhone() {
        String number = numberField.getText().toString();
        Function function = DatabaseHelper.getInstance(this).getFunction(Long.parseLong(functionId));
        function.setNumber(number);
        long result = DatabaseHelper.getInstance(this).updateFunction(function);
        if(result >=0 )
            setResult(RESULT_OK);
        finish();
    }

    public void addEmergencyMessege() {
        String number = numberField.getText().toString();
        String message = messageField.getText().toString();
        Function function = DatabaseHelper.getInstance(this).getFunction(Long.parseLong(functionId));
        function.setNumber(number);
        function.setMessage(message);
        long result = DatabaseHelper.getInstance(this).updateFunction(function);
        if(result >=0 )
            setResult(RESULT_OK);
        finish();
    }
}
