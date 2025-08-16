package com.example.lostintime;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

public class GameView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    
    private SurfaceHolder holder;
    private Thread gameThread;
    private boolean isRunning = false;
    
    private Paint paint;
    private GameState gameState;
    private LevelManager levelManager;
    private GameEventListener eventListener;
    
    // Game objects
    private Player player;
    private List<GameObject> gameObjects;
    private List<GameObject> traps;
    private Portal portal;
    
    // Time power system
    private boolean isTimePaused = false;
    private boolean isTimeReversed = false;
    private boolean isTimeSlowed = false;
    private float timePowerDuration = 0;
    private static final float TIME_POWER_DURATION = 3.0f; // 3 seconds
    
    // Animation
    private float animationTime = 0;
    private static final float ANIMATION_SPEED = 0.1f;
    
    public interface GameEventListener {
        void onLevelComplete();
        void onGameOver();
    }
    
    public GameView(Context context) {
        super(context);
        init();
    }
    
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    private void init() {
        holder = getHolder();
        holder.addCallback(this);
        
        paint = new Paint();
        paint.setAntiAlias(true);
        
        gameObjects = new ArrayList<>();
        traps = new ArrayList<>();
        
        levelManager = new LevelManager();
        
        setFocusable(true);
    }
    
    public void setGameEventListener(GameEventListener listener) {
        this.eventListener = listener;
    }
    
    public void startLevel(int level) {
        gameState = new GameState();
        levelManager.loadLevel(level, this);
        resetPlayer();
        isRunning = true;
        
        if (gameThread == null || !gameThread.isAlive()) {
            gameThread = new Thread(this);
            gameThread.start();
        }
    }
    
    public void setPlayerMovingLeft(boolean moving) {
        if (player != null) {
            player.setMovingLeft(moving);
        }
    }
    
    public void setPlayerMovingRight(boolean moving) {
        if (player != null) {
            player.setMovingRight(moving);
        }
    }
    
    public void playerJump() {
        if (player != null && !isTimePaused) {
            player.jump();
        }
    }
    
    public void activatePauseTime() {
        if (!isTimePaused && timePowerDuration <= 0) {
            isTimePaused = true;
            timePowerDuration = TIME_POWER_DURATION;
        }
    }
    
    public void activateReverseTime() {
        if (!isTimeReversed && timePowerDuration <= 0) {
            isTimeReversed = true;
            timePowerDuration = TIME_POWER_DURATION;
            // In a real implementation, you'd store and replay the last 5 seconds
        }
    }
    
    public void activateSlowTime() {
        if (!isTimeSlowed && timePowerDuration <= 0) {
            isTimeSlowed = true;
            timePowerDuration = TIME_POWER_DURATION;
        }
    }
    
    private void resetPlayer() {
        player = new Player(100, getHeight() - 200);
    }
    
    public void setLevelData(List<GameObject> objects, List<GameObject> levelTraps, Portal levelPortal) {
        this.gameObjects = objects;
        this.traps = levelTraps;
        this.portal = levelPortal;
    }
    
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // Surface is created, start the game loop
    }
    
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Surface size changed
    }
    
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // Surface is destroyed, stop the game loop
        isRunning = false;
        if (gameThread != null) {
            try {
                gameThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double deltaTime = 0;
        double targetFPS = 60.0;
        double targetTime = 1_000_000_000 / targetFPS;
        
        while (isRunning) {
            long currentTime = System.nanoTime();
            deltaTime = currentTime - lastTime;
            
            if (deltaTime >= targetTime) {
                update(deltaTime / 1_000_000_000.0);
                draw();
                lastTime = currentTime;
            }
        }
    }
    
    private void update(double deltaTime) {
        if (gameState == null || player == null) return;
        
        // Update time power duration
        if (timePowerDuration > 0) {
            timePowerDuration -= deltaTime;
            if (timePowerDuration <= 0) {
                isTimePaused = false;
                isTimeReversed = false;
                isTimeSlowed = false;
            }
        }
        
        // Update animation time
        animationTime += deltaTime * ANIMATION_SPEED;
        
        // Update player
        if (!isTimePaused) {
            float timeScale = isTimeSlowed ? 0.5f : 1.0f;
            player.update(deltaTime * timeScale);
        }
        
        // Update game objects (traps, moving platforms, etc.)
        if (!isTimePaused) {
            for (GameObject obj : gameObjects) {
                obj.update(deltaTime * (isTimeSlowed ? 0.5f : 1.0f));
            }
        }
        
        // Check collisions
        checkCollisions();
        
        // Check win condition
        if (portal != null && player.getBounds().intersect(portal.getBounds())) {
            if (eventListener != null) {
                eventListener.onLevelComplete();
            }
        }
    }
    
    private void checkCollisions() {
        if (player == null) return;
        
        // Check collision with platforms
        for (GameObject obj : gameObjects) {
            if (obj instanceof Platform) {
                Platform platform = (Platform) obj;
                if (player.getBounds().intersect(platform.getBounds())) {
                    player.handlePlatformCollision(platform);
                }
            }
        }
        
        // Check collision with traps
        for (GameObject trap : traps) {
            if (player.getBounds().intersect(trap.getBounds())) {
                if (eventListener != null) {
                    eventListener.onGameOver();
                }
                return;
            }
        }
    }
    
    private void draw() {
        Canvas canvas = holder.lockCanvas();
        if (canvas == null) return;
        
        try {
            // Clear background
            canvas.drawColor(Color.parseColor("#212121"));
            
            // Draw background elements (gears, clocks, etc.)
            drawBackground(canvas);
            
            // Draw game objects
            for (GameObject obj : gameObjects) {
                obj.draw(canvas, paint);
            }
            
            // Draw traps
            for (GameObject trap : traps) {
                trap.draw(canvas, paint);
            }
            
            // Draw portal
            if (portal != null) {
                portal.draw(canvas, paint);
            }
            
            // Draw player
            if (player != null) {
                player.draw(canvas, paint, animationTime);
            }
            
            // Draw UI elements
            drawUI(canvas);
            
        } finally {
            holder.unlockCanvasAndPost(canvas);
        }
    }
    
    private void drawBackground(Canvas canvas) {
        // Draw placeholder background elements
        paint.setColor(Color.parseColor("#424242"));
        
        // Draw some geometric shapes to represent broken clocks and gears
        for (int i = 0; i < 5; i++) {
            float x = (i * 200) % getWidth();
            float y = 50 + (i * 100) % 200;
            
            // Draw gear-like shapes
            canvas.drawCircle(x, y, 30, paint);
            paint.setColor(Color.parseColor("#616161"));
            canvas.drawCircle(x, y, 20, paint);
            paint.setColor(Color.parseColor("#424242"));
        }
    }
    
    private void drawUI(Canvas canvas) {
        paint.setColor(Color.WHITE);
        paint.setTextSize(30);
        
        // Draw time power status
        String timePowerText = "";
        if (isTimePaused) timePowerText = "TIME PAUSED";
        else if (isTimeReversed) timePowerText = "TIME REVERSED";
        else if (isTimeSlowed) timePowerText = "TIME SLOWED";
        
        if (!timePowerText.isEmpty()) {
            canvas.drawText(timePowerText, 20, 50, paint);
        }
    }
    
    public void pause() {
        isRunning = false;
    }
    
    public void resume() {
        isRunning = true;
    }
    
    public void destroy() {
        isRunning = false;
        if (gameThread != null) {
            try {
                gameThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}