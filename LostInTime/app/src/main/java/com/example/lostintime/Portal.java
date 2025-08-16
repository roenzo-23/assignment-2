package com.example.lostintime;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Portal extends GameObject {
    private float animationTime = 0;
    private static final float ANIMATION_SPEED = 0.15f;
    
    public Portal(float x, float y) {
        super(x, y, 60, 80);
    }
    
    @Override
    public void update(double deltaTime) {
        animationTime += ANIMATION_SPEED * deltaTime;
    }
    
    @Override
    public void draw(Canvas canvas, Paint paint) {
        // Save paint state
        Paint.Style originalStyle = paint.getStyle();
        
        // Animated portal effect
        float pulse = (float) (0.5 + 0.5 * Math.sin(animationTime * 3));
        float glowSize = 20 * pulse;
        
        // Draw outer glow
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#2196F3"));
        paint.setAlpha(100);
        canvas.drawCircle(x + width/2, y + height/2, width/2 + glowSize, paint);
        
        // Draw portal body
        paint.setAlpha(255);
        paint.setColor(Color.parseColor("#2196F3"));
        canvas.drawRect(bounds, paint);
        
        // Draw portal outline
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        paint.setColor(Color.WHITE);
        canvas.drawRect(bounds, paint);
        
        // Draw portal center
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        float centerX = x + width / 2;
        float centerY = y + height / 2;
        float centerSize = 15 * pulse;
        canvas.drawCircle(centerX, centerY, centerSize, paint);
        
        // Draw portal particles
        paint.setColor(Color.CYAN);
        for (int i = 0; i < 8; i++) {
            float angle = (float) (i * Math.PI * 2 / 8 + animationTime);
            float particleX = centerX + (float) Math.cos(angle) * (width/2 + 10);
            float particleY = centerY + (float) Math.sin(angle) * (height/2 + 10);
            float particleSize = 3 * pulse;
            canvas.drawCircle(particleX, particleY, particleSize, paint);
        }
        
        // Restore paint state
        paint.setStyle(originalStyle);
        paint.setAlpha(255);
    }
}