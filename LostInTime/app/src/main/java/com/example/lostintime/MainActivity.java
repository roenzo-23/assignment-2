package com.example.lostintime;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "LostInTimePrefs";
    private static final String KEY_LAST_LEVEL = "lastUnlockedLevel";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        
        Button startButton = findViewById(R.id.startButton);
        Button continueButton = findViewById(R.id.continueButton);
        
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame(1); // Start from level 1
            }
        });
        
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int lastLevel = sharedPreferences.getInt(KEY_LAST_LEVEL, 1);
                startGame(lastLevel);
            }
        });
        
        // Check if there's saved progress
        int lastLevel = sharedPreferences.getInt(KEY_LAST_LEVEL, 1);
        if (lastLevel <= 1) {
            continueButton.setEnabled(false);
            continueButton.setAlpha(0.5f);
        }
    }
    
    private void startGame(int level) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("level", level);
        startActivity(intent);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        // Update continue button state
        Button continueButton = findViewById(R.id.continueButton);
        int lastLevel = sharedPreferences.getInt(KEY_LAST_LEVEL, 1);
        if (lastLevel > 1) {
            continueButton.setEnabled(true);
            continueButton.setAlpha(1.0f);
        }
    }
}