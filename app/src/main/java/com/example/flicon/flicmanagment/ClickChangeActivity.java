package com.example.flicon.flicmanagment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.flicon.R;
import com.example.flicon.database.DatabaseHelper;
import com.example.flicon.database.Flic;
import com.example.flicon.database.Function;

public class ClickChangeActivity extends AppCompatActivity {
    private Button singleClickButton;
    private Button doubleClickButton;
    private Button holdButton;
    private Button deleteButton;
    private EditText flicNameEditText;
    private String mac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_change);

        singleClickButton = (Button) findViewById(R.id.singleButton);
        doubleClickButton = (Button) findViewById(R.id.doubleButton);
        holdButton = (Button) findViewById(R.id.holdButton);
        flicNameEditText = (EditText) findViewById(R.id.flicNameEditText);
        deleteButton = (Button) findViewById(R.id.deleteButton);

        Intent intent = getIntent();
        mac = intent.getExtras().getString("mac");

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFlic();
            }
        });
        refreshButtonNames();

        flicNameEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER) {
                    hideKeyboard(v);
                    Flic flic = DatabaseHelper.getInstance(ClickChangeActivity.this).getFlic(mac);
                    flic.setName(flicNameEditText.getText().toString());
                    DatabaseHelper.getInstance(ClickChangeActivity.this).updateFlic(flic);
                    flicNameEditText.clearFocus();
                    return true;
                }
                return false;
            }
        });

        findViewById(R.id.activityChangeLayout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(v);
                flicNameEditText.clearFocus();
                return true;
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        refreshButtonNames();
    }

    public void singleClick(View v) {
        Intent intent = new Intent(ClickChangeActivity.this, ChooseFunctionalityActivity.class);
        Function function = DatabaseHelper.getInstance(this).getFunction(mac, 1);
        intent.putExtra("functionId", Long.toString(function.getId()));
        startActivityForResult(intent, 1);
    }

    public void doubleClick(View v) {
        Intent intent = new Intent(ClickChangeActivity.this, ChooseFunctionalityActivity.class);
        Function function = DatabaseHelper.getInstance(this).getFunction(mac, 2);
        intent.putExtra("functionId", Long.toString(function.getId()));
        startActivityForResult(intent, 2);
    }

    public void holdClick(View v) {
        Intent intent = new Intent(ClickChangeActivity.this, ChooseFunctionalityActivity.class);
        Function function = DatabaseHelper.getInstance(this).getFunction(mac, 0);
        intent.putExtra("functionId", Long.toString(function.getId()));
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    Function function = DatabaseHelper.getInstance(this).getFunction(mac, 0);
                    function.setType(data.getExtras().getString("functionality"));
                    DatabaseHelper.getInstance(this).updateFunction(mac, 0, function);
                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    Function function = DatabaseHelper.getInstance(this).getFunction(mac, 1);
                    function.setType(data.getExtras().getString("functionality"));
                    DatabaseHelper.getInstance(this).updateFunction(mac, 1, function);
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    Function function = DatabaseHelper.getInstance(this).getFunction(mac, 2);
                    function.setType(data.getExtras().getString("functionality"));
                    DatabaseHelper.getInstance(this).updateFunction(mac, 2, function);
                }
                break;
        }
    }

    private void deleteFlic() {
        DatabaseHelper.getInstance(this).deleteFlic(mac);
        finish();
    }

    private void refreshButtonNames(){
        Flic flic = DatabaseHelper.getInstance(this).getFlic(mac);

        String name = flic.getName();
        if(name == null) {
            flicNameEditText.setText("No name");
        }
        else {
            flicNameEditText.setText(name);
        }

        Function singleClick = DatabaseHelper.getInstance(this).getFunction(flic.getSingleClick());
        Function doubleClick = DatabaseHelper.getInstance(this).getFunction(flic.getDoubleClick());
        Function hold = DatabaseHelper.getInstance(this).getFunction(flic.getHold());
        singleClickButton.setText(FunctionType.get(singleClick.getType()).getName());
        doubleClickButton.setText(FunctionType.get(doubleClick.getType()).getName());
        holdButton.setText(FunctionType.get(hold.getType()).getName());
    }

    private void hideKeyboard(View view){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if(inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
