package com.example.lostintime;

public class GameState {
    private int currentLevel;
    private int score;
    private boolean isGameOver;
    private boolean isLevelComplete;
    
    public GameState() {
        this.currentLevel = 1;
        this.score = 0;
        this.isGameOver = false;
        this.isLevelComplete = false;
    }
    
    public int getCurrentLevel() {
        return currentLevel;
    }
    
    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }
    
    public int getScore() {
        return score;
    }
    
    public void addScore(int points) {
        this.score += points;
    }
    
    public boolean isGameOver() {
        return isGameOver;
    }
    
    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }
    
    public boolean isLevelComplete() {
        return isLevelComplete;
    }
    
    public void setLevelComplete(boolean levelComplete) {
        isLevelComplete = levelComplete;
    }
    
    public void reset() {
        this.score = 0;
        this.isGameOver = false;
        this.isLevelComplete = false;
    }
}