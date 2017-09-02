package com.example.adam.asteroids;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by adam on 4/22/17.
 */

public class GameView extends SurfaceView implements Runnable {

    private final int NUMBER_OF_STARS = 25;
    private final int NUMBER_OF_ASTEROIDS = 3;

    Thread gameThread = null;
    SurfaceHolder gameHolder;
    volatile boolean playingGame;
    Canvas canvas;
    Paint paint;
    long lastFrameTime;
    int fps, command, health, score, highScore;

    int screenWidth, screenHeight;

    Ship ship;
    Bitmap shipImage;

    DirectionalPad directionalPad;
    VirtualButton virtualButton;

    List<Laser> lasers;
    List<Point> stars;
    List<Asteroid> asteroids;

    SoundPool soundPool;

    // Default constructor.
    public GameView(Context context) {
        super(context);
        gameHolder = getHolder();
    }

    // Overload the constructor using the screen size and SoundPool.
    public GameView(Context context, Point size, SoundPool soundPool){
        super(context);
        gameHolder = getHolder();

        //Set the screen width and height.
        screenWidth = size.x;
        screenHeight = size.y;


        // Instantiate the ship.
        shipImage = BitmapFactory.decodeResource(getResources(), R.drawable.ship);
        ship = new Ship(size.x / 2, size.y / 2, shipImage);

        // Instantiate the directional pad.
        directionalPad = new DirectionalPad(screenWidth / 10, 4 * screenHeight / 5, screenWidth / 12);

        // Instantiate the virtual button.
        virtualButton = new VirtualButton(screenWidth - screenWidth / 10, 4 * screenHeight / 5, screenWidth / 12);

        // Instantiate the list to hold the lasers.
        lasers = new ArrayList<>();

        // Instantiate the stars list and randomly set their points.
        stars = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_STARS; i++) {
            Random random = new Random();
            int x = random.nextInt(screenWidth);
            int y = random.nextInt(screenHeight);

            Point point = new Point(x, y);
            stars.add(point);
        }
        
        // Instantiate the asteroids list.
        asteroids = new ArrayList<>();

        // Set the soundPool.
        this.soundPool = soundPool;

        // Initialize the game statistics.
        command = 0;
        health = 100;
        score = 0;
        highScore = 0;
    }


    @Override
    public void run() {
        while (playingGame){
            updateGame();
            drawGame();
            controlFPS();
        }
    }

    public void updateGame() {
        detectCollisions();

        // Update the asteroids.
        for (int i = 0; i < asteroids.size(); i++) {
            Asteroid asteroid = asteroids.get(i);
            asteroid.update();
        }

        // Update the lasers.
        for (int i = 0; i < lasers.size(); i++){
            Laser laser = lasers.get(i);
            // Check to see if a laser is off the screen. If so, flag it for removal.
            if (laser.getX() > screenWidth || laser.getX() < 0 || laser.getY() > screenHeight || laser.getY() < 0) {
                laser.setFlag();
            }

            // Update the laser's positions.
            else {
                laser.update();
            }
        }

        // Add additional asteroids.
        if (asteroids.size() < NUMBER_OF_ASTEROIDS) {
            Random random = new Random();
            int quadrant = random.nextInt(4) + 1;
            int speed = random.nextInt(3) + 5;
            int angleFactor = random.nextInt(15);
            Asteroid asteroid;

            switch(quadrant) {
                case 4:
                    asteroid = new Asteroid(screenWidth - 20, screenWidth, screenHeight - 20, screenHeight, (float) speed, (float) angleFactor - 28.0f);
                    break;

                case 3:
                    asteroid = new Asteroid(20, screenWidth, screenHeight - 20, screenHeight, (float) speed, (float) angleFactor + 42.0f);
                    break;

                case 2:
                    asteroid = new Asteroid(20, screenWidth, 20, screenHeight, (float) speed, (float) angleFactor + 113.0f);
                    break;

                default:
                    asteroid = new Asteroid(screenWidth - 20, screenWidth, 20, screenHeight, (float) speed, (float) angleFactor -128.0f);
                    break;


            }
            asteroids.add(asteroid);
        }

        // Update the score.
        if (score > highScore)
            highScore = score;

        if (!(health > 0)) {
            soundPool.play(GameActivity.error, 1, 1, 0, 0, 1);
            score = 0;
            health = 100;
        }
    }

    private void detectCollisions() {
        // Detect collisions between the ship and the asteroids.
        for (int i = 0; i < asteroids.size(); i++) {
            Asteroid asteroid = asteroids.get(i);
            PointF pointF = new PointF(screenWidth / 2, screenHeight / 2);

            if (Math.abs(asteroid.getX() - pointF.x) < asteroid.getRadius() + ship.getShipRadius() / 2 &&
                    Math.abs(asteroid.getY() - pointF.y) < asteroid.getRadius() + ship.getShipRadius() / 2) {
                soundPool.play(GameActivity.whack, 1, 1, 0, 0, 1);
                health--;
            }
        }

        // Detect collisions between the lasers and the asteroids, setting flags if they collide.
        for (int i = 0; i < asteroids.size(); i++) {
            Asteroid asteroid = asteroids.get(i);

            for (int j = 0; j < lasers.size(); j++) {
                Laser laser = lasers.get(j);

                if (Math.abs(laser.getX() - asteroid.getX()) < asteroid.getRadius() && Math.abs(laser.getY() - asteroid.getY()) < asteroid.getRadius()) {
                    score+= 10;
                    soundPool.play(GameActivity.smash, 1, 1, 0, 0, 1);
                    laser.setFlag();
                    asteroid.setFlag();
                }
            }
        }


        // Remove flagged asteroids and add child asteroids as appropriate.
        for (int i = asteroids.size() - 1; i > -1; i--) {
            Asteroid asteroid = asteroids.get(i);

            if (asteroid.getFlag()) {
                adjustAsteroids(asteroids, i);
                asteroids.remove(i);
            }
        }

        // Remove flagged lasers.
        for (int i = lasers.size() - 1; i > -1; i--) {
            Laser laser = lasers.get(i);

            if (laser.getFlag()) {
                lasers.remove(i);
            }
        }
    }

    // Adjust the asteroids list with child asteroids if a large or medium asteroid collides with a laser.
    public void adjustAsteroids(List<Asteroid> asteroids, int index) {
        Asteroid asteroid = asteroids.get(index);
        Asteroid firstAsteroidChild, secondAsteroidChild;

        Random random = new Random();
        int angleFactor = random.nextInt(20) + 15;

        switch ((int) asteroid.getRadius()) {
            // Large asteroid, add two medium asteroids.
            case 45:
                firstAsteroidChild = new Asteroid(asteroid.getX(), asteroid.getMaxX(), asteroid.getY(), asteroid.getMaxY(),
                        asteroid.getSpeed(), asteroid.getAngle() + (float) angleFactor, 30.0f);
                asteroids.add(firstAsteroidChild);

                secondAsteroidChild = new Asteroid(asteroid.getX(), asteroid.getMaxX(), asteroid.getY(), asteroid.getMaxY(),
                        asteroid.getSpeed(), asteroid.getAngle() - (float) angleFactor, 30.0f);
                asteroids.add(secondAsteroidChild);
                break;

            // Medium asteroid, add two small asteroids.
            case 30:
                firstAsteroidChild = new Asteroid(asteroid.getX(), asteroid.getMaxX(), asteroid.getY(), asteroid.getMaxY(),
                        asteroid.getSpeed(), asteroid.getAngle() + (float) angleFactor, 15.0f);
                asteroids.add(firstAsteroidChild);

                secondAsteroidChild = new Asteroid(asteroid.getX(), asteroid.getMaxX(), asteroid.getY(), asteroid.getMaxY(),
                        asteroid.getSpeed(), asteroid.getAngle() - (float) angleFactor, 15.0f);
                asteroids.add(secondAsteroidChild);
                break;

            // Small asteroid, add nothing.
            case 15:
                break;

            default:
                break;
        }
    }

    public void drawGame() {
        if (gameHolder.getSurface().isValid()){
            canvas = gameHolder.lockCanvas();

            paint = new Paint();

            // Draw the background color.
            canvas.drawColor(Color.BLACK);

            // Draw the stars list.
            paint.setColor(Color.argb(255, 255, 255, 255));
            for (int i = 0; i < stars.size(); i++) {
                Point point = stars.get(i);
                canvas.drawRect(point.x, point.y, point.x + 2, point.y + 2, paint);
            }

            // Draw the directional pad.
            directionalPad.draw(canvas);

            // Draw the virtual button.
            virtualButton.draw(canvas);

            // Draw game statistics.
            paint.setColor(Color.argb(255, 255, 255, 255));
            paint.setTextSize(60);
            canvas.drawText("Health: " + health + "\tScore: " + score, 10, 60, paint);
            canvas.drawText("High Score: " + highScore, 7 * screenWidth / 10, 60, paint);

            // Draw the ship.
            ship.draw(canvas);

            // Draw the hit box.
            /*
            float width = ship.getShipRadius() / 2 + 45;
            paint.setStrokeWidth(3.5f);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(screenWidth / 2 - width / 2, screenHeight / 2 - width / 2,
                    screenWidth / 2 + width / 2, screenHeight / 2 + width / 2, paint);
            */

            // Draw the asteroids.
            for (int i = 0; i < asteroids.size(); i++) {
                Asteroid asteroid = asteroids.get(i);
                asteroid.draw(canvas);
            }

            // Draw the lasers.
            for (int i = 0; i < lasers.size(); i++) {
                Laser laser = lasers.get(i);
                laser.draw(canvas);
            }

            gameHolder.unlockCanvasAndPost(canvas);
        }

    }

    public void controlFPS() {
        long timeThisFrame = (System.currentTimeMillis() - lastFrameTime);
        long timeToSleep = 15 - timeThisFrame;
        if (timeThisFrame > 0) {
            fps = (int) (1000 / timeThisFrame);
        }
        if (timeToSleep > 0) {

            try {
                gameThread.sleep(timeToSleep);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        lastFrameTime = System.currentTimeMillis();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();



        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:

                // Rotate the ship.
                if (directionalPad.inRange(x, y)) {
                    command = directionalPad.getCommand(x);

                    if (command == 1)
                        ship.setRotationAngle(ship.getRotationAngle() - 8);

                    else if (command == 2)
                        ship.setRotationAngle(ship.getRotationAngle() + 8);
                }

                // Fire the laser.
                else if (virtualButton.inRange(x, y)) {
                    command = virtualButton.getCommand();
                    if (command == 3) {
                        lasers.add(new Laser(ship.getShipGun().x, ship.getShipGun().y, 14.2f, ship.getRotationAngle()));
                        soundPool.play(GameActivity.laserBlast, 1, 1, 0, 0, 1);
                    }
                }

                // Touch is out of range of D-Pad and button(s).
                else
                    command = 10;
                break;

            case MotionEvent.ACTION_UP:
                command = 0;
                break;

            default:
                break;


        }
        return true;
    }

    public void pause() {
        playingGame = false;
        try {
            gameThread.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void resume() {
        playingGame = true;
        gameThread = new Thread(this);
        gameThread.start();
    }
}