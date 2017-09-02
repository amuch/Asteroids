package com.example.adam.asteroids;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by adam on 7/16/17.
 */

public class Ship {

    private float rotationAngle, shipRadius;
    private Bitmap shipImage;
    private PointF shipCenter, shipGun, screenCenter;
    private Rect src;
    private RectF dst;

    public Ship(float x, float y, Bitmap b) {
        setScreenCenter(x, y);
        setShipImage(b);
        setRotationAngle(0.0f);
        setShipCenter(x - b.getWidth() / 2, y - b.getHeight() / 2);

        this.shipRadius = b.getHeight() / 2 + 2;
        setShipGun(x, y - this.shipRadius);
    }

    public void draw(float x, float y, Canvas canvas) {
        Paint paint = new Paint();

        canvas.drawBitmap(shipImage, getShipCenter().x, getShipCenter().y, paint);
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        Matrix rotation = new Matrix();

        // Rotate the ship image by the rotation angle about the center of the image.
        rotation.postRotate(this.getRotationAngle(), shipImage.getWidth() / 2, shipImage.getHeight() / 2);

        // Translate the ship image to the middle of the screen.
        rotation.postTranslate(getShipCenter().x, getShipCenter().y);

        // Draw the ship image.
        canvas.drawBitmap(shipImage, rotation, paint);

        // Set the point from which a laser will be fired.
        this.setShipGun(getScreenCenter().x + shipRadius * (float) Math.sin(getRotationAngle() * Math.PI / 180),
                        getScreenCenter().y - shipRadius * (float) Math.cos(getRotationAngle() * Math.PI / 180));
    }


    public float getShipRadius() {
        return shipRadius;
    }

    public void setRotationAngle(float degrees) {
        this.rotationAngle = degrees;
    }

    public float getRotationAngle() {
        return rotationAngle;
    }

    private void setShipImage(Bitmap bitmap){
        shipImage = bitmap;
    }

    public PointF getShipCenter() {
        return shipCenter;
    }

    private void setShipCenter(float x, float y) {
        this.shipCenter = new PointF(x, y);
    }

    public PointF getShipGun() {
        return shipGun;
    }

    public void setShipGun(float x, float y) {
        this.shipGun = new PointF(x, y);
    }

    private PointF getScreenCenter() {
        return  screenCenter;
    }

    private void setScreenCenter(float x, float y) {
        this.screenCenter = new PointF(x, y);
    }
}
