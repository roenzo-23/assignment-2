package com.example.lostintime;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class Player extends GameObject {
    private static final float MOVE_SPEED = 200.0f;
    private static final float JUMP_FORCE = -400.0f;
    private static final float GRAVITY = 800.0f;
    private static final float TERMINAL_VELOCITY = 600.0f;
    
    private float velocityX = 0;
    private float velocityY = 0;
    private boolean isMovingLeft = false;
    private boolean isMovingRight = false;
    private boolean isOnGround = false;
    
    // Animation
    private float animationTime = 0;
    private static final float ANIMATION_SPEED = 0.2f;
    
    public Player(float x, float y) {
        super(x, y, 40, 60);
    }
    
    @Override
    public void update(double deltaTime) {
        float dt = (float) deltaTime;
        
        // Handle horizontal movement
        if (isMovingLeft) {
            velocityX = -MOVE_SPEED;
        } else if (isMovingRight) {
            velocityX = MOVE_SPEED;
        } else {
            velocityX *= 0.8f; // Friction
        }
        
        // Apply gravity
        if (!isOnGround) {
            velocityY += GRAVITY * dt;
            if (velocityY > TERMINAL_VELOCITY) {
                velocityY = TERMINAL_VELOCITY;
            }
        }
        
        // Update position
        x += velocityX * dt;
        y += velocityY * dt;
        
        // Update bounds
        updateBounds();
        
        // Reset ground state
        isOnGround = false;
    }
    
    @Override
    public void draw(Canvas canvas, Paint paint) {
        // Save paint state
        Paint.Style originalStyle = paint.getStyle();
        
        // Draw player body
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#4CAF50"));
        canvas.drawRect(bounds, paint);
        
        // Draw player outline
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        paint.setColor(Color.BLACK);
        canvas.drawRect(bounds, paint);
        
        // Draw simple face
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        
        // Eyes
        canvas.drawCircle(x + 12, y + 15, 3, paint);
        canvas.drawCircle(x + 28, y + 15, 3, paint);
        
        // Simple walking animation
        if (Math.abs(velocityX) > 10) {
            animationTime += ANIMATION_SPEED;
            float bounce = (float) Math.sin(animationTime) * 5;
            y += bounce;
            updateBounds();
        }
        
        // Restore paint state
        paint.setStyle(originalStyle);
    }
    
    public void setMovingLeft(boolean moving) {
        isMovingLeft = moving;
    }
    
    public void setMovingRight(boolean moving) {
        isMovingRight = moving;
    }
    
    public void jump() {
        if (isOnGround) {
            velocityY = JUMP_FORCE;
            isOnGround = false;
        }
    }
    
    public void handlePlatformCollision(Platform platform) {
        RectF platformBounds = platform.getBounds();
        
        // Check if player is above the platform
        if (velocityY > 0 && bounds.bottom >= platformBounds.top && 
            bounds.bottom <= platformBounds.top + 10) {
            y = platformBounds.top - height;
            velocityY = 0;
            isOnGround = true;
            updateBounds();
        }
    }
    
    public float getVelocityX() {
        return velocityX;
    }
    
    public float getVelocityY() {
        return velocityY;
    }
    
    public boolean isOnGround() {
        return isOnGround;
    }
}