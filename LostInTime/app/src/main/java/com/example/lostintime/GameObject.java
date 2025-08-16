package com.example.lostintime;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public abstract class GameObject {
    protected float x, y;
    protected float width, height;
    protected RectF bounds;
    
    public GameObject(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.bounds = new RectF(x, y, x + width, y + height);
    }
    
    public abstract void update(double deltaTime);
    public abstract void draw(Canvas canvas, Paint paint);
    
    public float getX() {
        return x;
    }
    
    public void setX(float x) {
        this.x = x;
        updateBounds();
    }
    
    public float getY() {
        return y;
    }
    
    public void setY(float y) {
        this.y = y;
        updateBounds();
    }
    
    public float getWidth() {
        return width;
    }
    
    public float getHeight() {
        return height;
    }
    
    public RectF getBounds() {
        return bounds;
    }
    
    protected void updateBounds() {
        bounds.set(x, y, x + width, y + height);
    }
    
    public boolean intersects(GameObject other) {
        return bounds.intersect(other.getBounds());
    }
}