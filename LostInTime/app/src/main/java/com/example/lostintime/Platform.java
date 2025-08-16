package com.example.lostintime;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Platform extends GameObject {
    
    public Platform(float x, float y, float width, float height) {
        super(x, y, width, height);
    }
    
    @Override
    public void update(double deltaTime) {
        // Static platforms don't need updates
    }
    
    @Override
    public void draw(Canvas canvas, Paint paint) {
        // Save paint state
        Paint.Style originalStyle = paint.getStyle();
        
        // Draw platform
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#795548"));
        canvas.drawRect(bounds, paint);
        
        // Draw platform outline
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        paint.setColor(Color.BLACK);
        canvas.drawRect(bounds, paint);
        
        // Restore paint state
        paint.setStyle(originalStyle);
    }
}