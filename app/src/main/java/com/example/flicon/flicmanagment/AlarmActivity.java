package com.example.flicon.flicmanagment;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.flicon.R;

public class AlarmActivity extends AppCompatActivity {
    private ImageView doneButton;
    private int MAX_RETRIES = 10;

    AsyncTask asyncTask = new AsyncTask() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected Object doInBackground(Object[] objects) {
            for (int i = 0; i < MAX_RETRIES; i++) {
                if (!isCancelled())
                    Functionalities.getInstance(AlarmActivity.this).panic();
            }
            return null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        doneButton = (ImageView) findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asyncTask.cancel(true);
                finish();
            }
        });
        asyncTask.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        asyncTask.cancel(true);
    }
}
