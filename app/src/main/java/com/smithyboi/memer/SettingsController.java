package com.smithyboi.memer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsController extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settingslol);

        Button b = findViewById(R.id.backers);
        b.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                returnToMain();
                Log.d("TAG", "onClick: calling to go back");
            }
        });
    }
    public void returnToMain(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
