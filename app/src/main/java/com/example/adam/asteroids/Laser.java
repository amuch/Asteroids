package com.example.adam.asteroids;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by adam on 8/26/17.
 */

public class Laser {
    private final float LASER_WIDTH = 6;
    private float x, y, speed, angle;
    private boolean flag;

    public Laser(float x, float y, float speed, float angle) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.angle = angle;
        this.flag = false;
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();

        paint.setColor(Color.argb(255, 124, 3, 3));
        canvas.drawRect(this.x, this.y, this.x + LASER_WIDTH, this.y + LASER_WIDTH, paint);
    }

    public void update() {
        this.x = this.x + this.speed * (float) Math.sin(this.angle * Math.PI / 180);
        this.y = this.y - this.speed * (float) Math.cos(this.angle * Math.PI / 180);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public boolean getFlag() {
        return flag;
    }

    public void setFlag() {
        this.flag = true;
    }
}
