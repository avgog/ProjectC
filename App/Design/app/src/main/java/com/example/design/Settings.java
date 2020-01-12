package com.example.design;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

public class Settings extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        Button closeButton = findViewById(R.id.CloseButton);
        Button changeUsernameButton = findViewById(R.id.changepasswordbutton);
        Button changePasswordButton = findViewById(R.id.changepasswordbutton);
        ToggleButton themeButton = findViewById(R.id.ThemeButton);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
