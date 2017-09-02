package com.example.adam.asteroids;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by adam on 8/26/17.
 */

public class VirtualButton {
    private float xPosition, yPosition, width;

    public VirtualButton(float x, float y, float w) {
        this.xPosition = x;
        this.yPosition = y;
        this.width = w;
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();

        // Fill the Directional pad's circle.
        paint.setColor(Color.argb(127, 127, 127, 127));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(this.xPosition - this.width / 2, this.yPosition - this.width / 2,
                        this.xPosition + this.width / 2, this.yPosition + this.width / 2, paint);

        // Stroke the border.
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        canvas.drawRect(this.xPosition - this.width / 2, this.yPosition - this.width / 2,
                        this.xPosition + this.width / 2, this.yPosition + this.width / 2, paint);

    }

    public boolean inRange(float x, float y) {
        if (Math.abs(this.xPosition - x) < this.width / 2 && Math.abs(this.yPosition - y) < this.width / 2)
            return true;

        else
            return false;
    }

    public int getCommand() {
            return 3;
    }
}
