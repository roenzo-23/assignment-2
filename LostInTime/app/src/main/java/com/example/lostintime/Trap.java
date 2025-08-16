package com.example.lostintime;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Trap extends GameObject {
    
    public Trap(float x, float y, float width, float height) {
        super(x, y, width, height);
    }
    
    @Override
    public void update(double deltaTime) {
        // Static traps don't need updates
    }
    
    @Override
    public void draw(Canvas canvas, Paint paint) {
        // Save paint state
        Paint.Style originalStyle = paint.getStyle();
        
        // Draw trap
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#F44336"));
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
        
        // Restore paint state
        paint.setStyle(originalStyle);
    }
}