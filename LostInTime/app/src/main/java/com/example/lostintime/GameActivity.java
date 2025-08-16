package com.example.lostintime;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {
    
    private GameView gameView;
    private TextView levelText;
    private TextView timePowerText;
    private Button leftButton, rightButton, jumpButton, timePowerButton, activatePowerButton;
    
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "LostInTimePrefs";
    private static final String KEY_LAST_LEVEL = "lastUnlockedLevel";
    
    private int currentLevel;
    private int currentTimePower = 0; // 0: Pause, 1: Reverse, 2: Slow
    private String[] timePowerNames = {"Pause", "Reverse", "Slow"};
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        currentLevel = getIntent().getIntExtra("level", 1);
        
        initializeViews();
        setupGameView();
        setupControls();
        updateUI();
    }
    
    private void initializeViews() {
        gameView = findViewById(R.id.gameView);
        levelText = findViewById(R.id.levelText);
        timePowerText = findViewById(R.id.timePowerText);
        
        leftButton = findViewById(R.id.leftButton);
        rightButton = findViewById(R.id.rightButton);
        jumpButton = findViewById(R.id.jumpButton);
        timePowerButton = findViewById(R.id.timePowerButton);
        activatePowerButton = findViewById(R.id.activatePowerButton);
    }
    
    private void setupGameView() {
        gameView.setGameEventListener(new GameView.GameEventListener() {
            @Override
            public void onLevelComplete() {
                showLevelCompleteDialog();
            }
            
            @Override
            public void onGameOver() {
                showGameOverDialog();
            }
        });
        
        gameView.startLevel(currentLevel);
    }
    
    private void setupControls() {
        leftButton.setOnTouchListener(new HoldTouchListener() {
            @Override
            public void onTouchDown() {
                gameView.setPlayerMovingLeft(true);
            }
            
            @Override
            public void onTouchUp() {
                gameView.setPlayerMovingLeft(false);
            }
        });
        
        rightButton.setOnTouchListener(new HoldTouchListener() {
            @Override
            public void onTouchDown() {
                gameView.setPlayerMovingRight(true);
            }
            
            @Override
            public void onTouchUp() {
                gameView.setPlayerMovingRight(false);
            }
        });
        
        jumpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameView.playerJump();
            }
        });
        
        timePowerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cycleTimePower();
            }
        });
        
        activatePowerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activateTimePower();
            }
        });
    }
    
    private void cycleTimePower() {
        currentTimePower = (currentTimePower + 1) % 3;
        updateUI();
    }
    
    private void activateTimePower() {
        switch (currentTimePower) {
            case 0: // Pause
                gameView.activatePauseTime();
                break;
            case 1: // Reverse
                gameView.activateReverseTime();
                break;
            case 2: // Slow
                gameView.activateSlowTime();
                break;
        }
    }
    
    private void updateUI() {
        levelText.setText("Level " + currentLevel);
        timePowerText.setText(timePowerNames[currentTimePower]);
    }
    
    private void showLevelCompleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_level_complete, null);
        builder.setView(dialogView);
        
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        
        Button nextLevelButton = dialogView.findViewById(R.id.nextLevelButton);
        Button mainMenuButton = dialogView.findViewById(R.id.mainMenuButton);
        
        nextLevelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startNextLevel();
            }
        });
        
        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
        
        dialog.show();
    }
    
    private void showGameOverDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_game_over, null);
        builder.setView(dialogView);
        
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        
        Button retryButton = dialogView.findViewById(R.id.retryButton);
        Button mainMenuButton = dialogView.findViewById(R.id.mainMenuButton);
        
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                restartLevel();
            }
        });
        
        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
        
        dialog.show();
    }
    
    private void startNextLevel() {
        int nextLevel = currentLevel + 1;
        if (nextLevel <= 3) { // Maximum 3 levels for now
            currentLevel = nextLevel;
            saveProgress(nextLevel);
            gameView.startLevel(currentLevel);
            updateUI();
        } else {
            // Game completed
            Toast.makeText(this, "Congratulations! You've completed the game!", Toast.LENGTH_LONG).show();
            finish();
        }
    }
    
    private void restartLevel() {
        gameView.startLevel(currentLevel);
    }
    
    private void saveProgress(int level) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_LAST_LEVEL, level);
        editor.apply();
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        if (gameView != null) {
            gameView.pause();
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        if (gameView != null) {
            gameView.resume();
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gameView != null) {
            gameView.destroy();
        }
    }
}