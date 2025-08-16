package com.example.lostintime;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class MovingTrap extends GameObject {
    private float startX, startY, endX, endY;
    private float moveSpeed = 60.0f;
    private float moveProgress = 0;
    private boolean movingForward = true;
    
    public MovingTrap(float x, float y, float width, float height, 
                      float startX, float startY, float endX, float endY) {
        super(x, y, width, height);
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }
    
    @Override
    public void update(double deltaTime) {
        float dt = (float) deltaTime;
        
        // Update movement progress
        if (movingForward) {
            moveProgress += moveSpeed * dt;
            if (moveProgress >= 1.0f) {
                moveProgress = 1.0f;
                movingForward = false;
            }
        } else {
            moveProgress -= moveSpeed * dt;
            if (moveProgress <= 0.0f) {
                moveProgress = 0.0f;
                movingForward = true;
            }
        }
        
        // Interpolate position
        x = startX + (endX - startX) * moveProgress;
        y = startY + (endY - startY) * moveProgress;
        
        updateBounds();
    }
    
    @Override
    public void draw(Canvas canvas, Paint paint) {
        // Save paint state
        Paint.Style originalStyle = paint.getStyle();
        
        // Draw moving trap with different color
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#D32F2F"));
        canvas.drawRect(bounds, paint);
        
        // Draw trap outline
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        paint.setColor(Color.BLACK);
        canvas.drawRect(bounds, paint);
        
        // Draw danger symbol (X)
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        paint.setColor(Color.WHITE);
        
        float centerX = x + width / 2;
        float centerY = y + height / 2;
        float size = Math.min(width, height) / 3;
        
        canvas.drawLine(centerX - size, centerY - size, centerX + size, centerY + size, paint);
        canvas.drawLine(centerX + size, centerY - size, centerX - size, centerY + size, paint);
        
        // Draw movement indicator
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.YELLOW);
        float indicatorSize = 6;
        canvas.drawCircle(x + width/2, y + height/2, indicatorSize, paint);
        
        // Restore paint state
        paint.setStyle(originalStyle);
    }
}