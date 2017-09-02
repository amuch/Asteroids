package com.example.adam.asteroids;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by adam on 7/22/17.
 */

class DirectionalPad {
    private float xPosition, yPosition, radius;

    public DirectionalPad(float x, float y, float r) {
        this.xPosition = x;
        this.yPosition = y;
        this.radius = r;
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();

        // Fill the Directional pad's circle.
        paint.setColor(Color.argb(127, 127, 127, 127));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(this.xPosition, this.yPosition, this.radius, paint);

        // Stroke the border.
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        canvas.drawCircle(this.xPosition, this.yPosition, this.radius, paint);
    }

    public boolean inRange(float x, float y) {
        if (Math.abs(this.xPosition - x) < this.radius && Math.abs(this.yPosition - y) < this.radius)
            return true;

        else
            return false;
    }

    public int getCommand(float x) {

        // Left side of directional pad.
        if (x < this.xPosition)
            return 1;

        // Right side of controller.
        else if (x > this.xPosition)
            return 2;

        // Default.
        else
            return 0;
    }
}
