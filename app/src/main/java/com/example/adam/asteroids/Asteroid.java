package com.example.adam.asteroids;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.List;

/**
 * Created by adam on 8/27/17.
 */

public class Asteroid {
    private float radius;
    private float x, maxX, y, maxY, speed, angle;
    private boolean flag;

    public Asteroid(float x, float maxX, float y, float maxY, float speed, float angle) {
        this.x = x;
        this.maxX = maxX;
        this.y = y;
        this.maxY = maxY;
        this.speed = speed;
        this.angle = angle;
        this.radius = 45;
        this.flag = false;
    }

    public Asteroid(float x, float maxX, float y, float maxY, float speed, float angle, float radius) {
        this.x = x;
        this.maxX = maxX;
        this.y = y;
        this.maxY = maxY;
        this.speed = speed;
        this.angle = angle;
        this.radius = radius;
        this.flag = false;
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();

        paint.setColor(Color.argb(255, 96, 128, 0));
        paint.setStrokeWidth(3.5f);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle (this.x, this.y, this.radius, paint);
    }

    public void update() {
        if (this.x < 0) {
            this.x = this.maxX;
            //this.y = this.maxY - this.y;
        }

        else if (this.x > this.maxX) {
            this.x = 0;
            //this.y = this.maxY - this.y;
        }

        else if (this.y < 0) {
            //this.x = this.maxX - this.x;
            this.y = this.maxY;
        }

        else if (this.y > this.maxY) {
            //this.x = this.maxX - this.x;
            this.y = 0;
        }

        this.x = this.x + this.speed * (float) Math.sin(this.angle * Math.PI / 180);
        this.y = this.y - this.speed * (float) Math.cos(this.angle * Math.PI / 180);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getRadius() {
        return radius;
    }

    public float getMaxX() {
        return maxX;
    }

    public float getMaxY() {
        return maxY;
    }

    public float getSpeed() {
        return speed;
    }

    public float getAngle() {
        return angle;
    }

    public boolean getFlag() {
        return flag;
    }

    public void setFlag() {
        this.flag = true;
    }
}
