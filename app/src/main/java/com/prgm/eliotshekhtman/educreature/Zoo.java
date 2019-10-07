package com.prgm.eliotshekhtman.educreature;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.TextView;

import java.util.Date;
import java.util.Random;

public class Zoo extends AppCompatActivity {

    GameView gameView;
    int[][] coör;
    final Context context = this;
    private Handler handler;

    float tapx;
    float tapy;
    boolean played = false;
    boolean feed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zoo);
        tapx = 1; tapy = 0;
        handler = new Handler(Looper.getMainLooper());


        // Initialize gameView and set it as the view
        gameView = new GameView(this);
        setContentView(gameView);

    }

    // GameView class will go here

    // Here is our implementation of GameView
    // It is an inner class.
    // Note how the final closing curly brace }
    // is inside SimpleGameEngine

    // Notice we implement runnable so we have
    // A thread and can override the run method.
    class GameView extends SurfaceView implements Runnable {

        // This is our thread
        Thread gameThread = null;

        // This is new. We need a SurfaceHolder
        // When we use Paint and Canvas in a thread
        // We will see it in action in the draw method soon.
        SurfaceHolder ourHolder;

        // A boolean which we will set and unset
        // when the game is running- or not.
        volatile boolean playing;

        // A Canvas and a Paint object
        Canvas canvas;
        Paint paint;

        // This variable tracks the game frame rate
        long fps;

        // This is used to help calculate the fps
        private long timeThisFrame;

        // When it is first opened, it knows that you aren't tapping
        boolean tap = false;

        // When the we initialize (call new()) on gameView
        // This special constructor method runs
        public GameView(Context context) {
            // The next line of code asks the
            // SurfaceView class to set up our object.
            // How kind.
            super(context);



            // Initialize ourHolder and paint objects
            ourHolder = getHolder();
            paint = new Paint();

            // Load base picture
            // epic = BitmapFactory.decodeResource(this.getResources(), R.drawable.basesymbol);

            // Set our boolean to true - game on!
            playing = true;

        }

        @Override
        public void run() {
            coör = new int[5][MainActivity.zoo.size()];
            for(int i = 0; i < coör[0].length; i++) {
                coör[0][i] = randInt(0, getScreenWidth()); // x
                coör[1][i] = randInt(0, getScreenHeight()); // y
                coör[2][i] = 0; // angle of movement
                coör[3][i] = 0; // positive = moving, negative = turning till it reaches that angle
                coör[4][i] = 0; // for timing purposes
            }
            while (playing) {

                // Capture the current time in milliseconds in startFrameTime
                long startFrameTime = System.currentTimeMillis();

                // Update the frame
                update();

                // Draw the frame
                draw();

                // Calculate the fps this frame
                // We can then use the result to
                // time animations and more.
                timeThisFrame = System.currentTimeMillis() - startFrameTime;
                if (timeThisFrame > 0) {
                    fps = 1000 / timeThisFrame;
                }

            }

        }

        // Everything that needs to be updated goes in here
        public void update() {
            for(int i = 0; i < MainActivity.zoo.size(); i++) {
                // Make sure angle is always valid
                if(coör[2][i] < 0)
                    coör[2][i] += 360;
                if(MainActivity.inzoo == true)
                    MainActivity.zoo.get(i).tick();

                // If c3 = 0, have a random chance to either continue doing nothing, or set a distance to move or an angle to turn
                if(coör[3][i] == 0) {
                    //Log.w("MyApp", "Equal");
                    MainActivity.zoo.get(i).setStat();
                    if(MainActivity.zoo.get(i).getHappiness() < 50)
                        MainActivity.zoo.get(i).setSStat();
                    int a = 300;
                    if(MainActivity.zoo.get(i).getHappiness() < 50)
                        a = 600;
                    if (randInt(0, a) == 0) {
                        if (randInt(0, 2) == 0) { // Move forwards
                            Log.w("MyApp", i + " moves");
                            a = 200;
                            if(MainActivity.zoo.get(i).getHappiness() < 50)
                                a = 80;
                            coör[3][i] = randInt(0, a);
                            coör[2][i] = randInt(0, 360);
                            coör[4][i] = 21;
                        }
                        else { // Blink
                            Log.w("MyApp", i + " blinks");
                            coör[3][i] = -1;
                            coör[4][i] = 30;
                        }
                    }
                }
                // If it's positive, make it go forwards in accordance to angle, and decrement c3
                else if (coör[3][i] > 0) {

                    //Log.w("MyApp", "Greater");
                    int s = 20; // Distance to move - for now, a constant.  Perhaps later, add movement speed to each creature?
                    if(MainActivity.zoo.get(i).getHappiness() < 50) // if sad, moves less
                        s = 10;
                    // Make it so that it steps across the screen
                    coör[4][i]--;
                    if(coör[2][i] > 90 & coör[2][i] < 270) {
                        if(coör[4][i] == 20) {
                            coör[0][i] += s * Math.cos(Math.toRadians(coör[2][i]));
                            coör[1][i] -= s * Math.sin(Math.toRadians(coör[2][i]));
                            MainActivity.zoo.get(i).setl1();
                        }
                        else if(coör[4][i] == 10) {
                            coör[0][i] += s * Math.cos(Math.toRadians(coör[2][i]));
                            coör[1][i] -= s * Math.sin(Math.toRadians(coör[2][i]));
                            MainActivity.zoo.get(i).setl2();
                        }
                        else if(coör[4][i] == 0) {
                            coör[4][i] = 21;
                        }
                    }
                    else { // hopefully make this thing """"turn""""
                        if(coör[4][i] == 20) {
                            coör[0][i] += s * Math.cos(Math.toRadians(coör[2][i]));
                            coör[1][i] -= s * Math.sin(Math.toRadians(coör[2][i]));
                            MainActivity.zoo.get(i).setr1();
                        }
                        else if(coör[4][i] == 10) {
                            coör[0][i] += s * Math.cos(Math.toRadians(coör[2][i]));
                            coör[1][i] -= s * Math.sin(Math.toRadians(coör[2][i]));
                            MainActivity.zoo.get(i).setr2(); // uggo as a literal buggo but goddamnit it's too late for beauty
                        }
                        else if(coör[4][i] == 0) {
                            coör[4][i] = 21;
                        }
                    }

                    // handling cases where it hits the edge - bounce
                    if(coör[0][i] < 0 | coör[0][i] > getScreenWidth() | coör[1][i] < 0 | coör[1][i] > getScreenHeight()) {
                        coör[2][i] += 180;
                        if(coör[2][i] < 0)
                            coör[2][i] += 360;
                        if(coör[2][i] > 360)
                            coör[2][i] -= 360;
                    }
                    if(coör[0][i] < 0) {
                        coör[0][i] = 1;
                    }
                    else if(coör[0][i] > getScreenWidth()) {
                        coör[0][i] = getScreenWidth() - 1;
                    }
                    else if(coör[1][i] < 0) {
                        coör[1][i] = 0;
                    }
                    else if(coör[1][i] > getScreenHeight()) {
                        coör[1][i] = getScreenHeight() - 1;
                    }
                    coör[3][i]--;
                }
                // blink u foo
                else if (coör[3][i] == -1) {
                    coör[4][i]--;
                    if(coör[4][i] / 10 == 2)
                        MainActivity.zoo.get(i).setBlink();
                    else if(coör[4][i] / 10 == 1)
                        MainActivity.zoo.get(i).setStat();
                    else if(coör[4][i] / 10 == 0)
                        MainActivity.zoo.get(i).setBlink();
                    if(coör[4][i] == 0)
                        coör[3][i] = 0;
                    if(MainActivity.zoo.get(i).getHappiness() < 50) {
                        if(coör[4][i] / 10 == 2)
                            MainActivity.zoo.get(i).setSBlink();
                        else if(coör[4][i] / 10 == 1)
                            MainActivity.zoo.get(i).setSStat();
                        else if(coör[4][i] / 10 == 0)
                            MainActivity.zoo.get(i).setSBlink();
                        if(coör[4][i] == 0)
                            coör[3][i] = 0;
                    }

                }
                /*
                else {
                    Log.w("MyApp", "Lesser");
                    int s = 1; // Same dealio as the other thingy
                    if(coör[2][i] + coör[3][i] <= -180)
                        coör[2][i] -= s;
                    else
                        coör[2][i] += s;
                    if (coör[2][i] == (-1) * coör[3][i]) {
                        coör[3][i] = 0;
                    }
                }
                //*/

                double diff = Math.sqrt((tapx - coör[0][i]) * (tapx - coör[0][i]) + (tapy - coör[1][i]) * (tapy - coör[1][i]));
                if(tap & diff < MainActivity.zoo.get(i).pic[0].getHeight()) {
                    final int j = i;
                    handler.post(new Runnable() {
                        public void run() {
                            // Instanitiate your dialog here
                            boolean p = showMyDialog(MainActivity.zoo.get(j).toString());
                            if(p) MainActivity.zoo.get(j).play();
                            if(feed) MainActivity.zoo.get(j).feed();
                        }
                    });
                    tap = false;
                }
                if(tap) {
                    Log.w("myApp", diff + "");
                    //tap = false;
                }
            }
        }

        // Draw the newly updated scene
        public void draw() {

            // Make sure our drawing surface is valid or we crash
            if (ourHolder.getSurface().isValid()) {
                // Lock the canvas ready to draw
                canvas = ourHolder.lockCanvas();
                // Draw the background color
                canvas.drawColor(Color.argb(255, 144, 238, 144));

                // Draw enemy's position
                for (int i = 0; i < MainActivity.zoo.size(); i++) { // pls don't ask why this is like this - it's a warzone, with nothing but broken bodies and broken souls
                    //canvas.drawBitmap(MainActivity.zoo.get(i).pic[0], coör[0][i], coör[1][i], paint);
                    //Matrix matrix = new Matrix();
                    //matrix.setRotate(coör[2][i], coör[0][i] /* - MainActivity.zoo.get(i).pic[0].getWidth() / 2 */, coör[1][i] /* - MainActivity.zoo.get(i).pic[0].getHeight() / 2 */);
                    //canvas.drawBitmap(MainActivity.zoo.get(i).pic[0], matrix, null);
                    //canvas.save(Canvas.ALL_SAVE_FLAG); //Saving the canvas and later restoring it so only this image will be rotated.
                    //canvas.rotate(-coör[2][i]);
                    // canvas.drawBitmap(MainActivity.zoo.get(i).pic[0], (int) (coör[0][i] * Math.cos(coör[2][i]) + coör[1][i] * Math.sin(coör[2][i])) - MainActivity.zoo.get(i).pic[0].getWidth() / 2, (int) (coör[1][i] * Math.cos(coör[2][i]) - coör[0][i] * Math.sin(coör[2][i])) - MainActivity.zoo.get(i).pic[0].getHeight() / 2, paint);
                    canvas.drawBitmap(MainActivity.zoo.get(i).pic[0], coör[0][i] - MainActivity.zoo.get(i).pic[0].getWidth() / 2, coör[1][i] - MainActivity.zoo.get(i).pic[0].getHeight() / 2, paint);
                    //canvas.restore();

                }
                // Draw everything to the screen
                ourHolder.unlockCanvasAndPost(canvas);
            }
        }



        private boolean showMyDialog(String s) {
            //played = false;
            new AlertDialog.Builder(context)
                    .setTitle("")
                    .setMessage(s)
                    .setPositiveButton("Play", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            played = true; feed = false;
                        }
                    })
                    .setNeutralButton("Feed", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                            played = false; feed = true;
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                            played = false; feed = false;
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return played;
        }

        // If SimpleGameEngine Activity is paused/stopped
        // shutdown our thread.
        public void pause() {
            playing = false;
            try {
                gameThread.join();
            } catch (InterruptedException e) {
                Log.e("Error:", "joining thread");
            }

        }

        // If SimpleGameEngine Activity is started then
        // start our thread.
        public void resume() {
            playing = true;
            gameThread = new Thread(this);
            gameThread.start();
        }

        // The SurfaceView class implements onTouchListener
        // So we can override this method and detect screen touches.
        @Override
        public boolean onTouchEvent(MotionEvent motionEvent) {

            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

                // Player has touched the screen
                case MotionEvent.ACTION_DOWN:

                    // Set tap so tap is put down
                    tap = true;
                    tapx = (int)motionEvent.getX();
                    tapy = (int)motionEvent.getY();
                    break;

                // Player has removed finger from screen
                case MotionEvent.ACTION_UP:

                    // Set tap is raised
                    tap = false;

                    break;
            }
            return true;
        }

    }
    // This is the end of our GameView inner class

    // More SimpleGameEngine methods will go here

    // This method executes when the player starts the game
    @Override
    protected void onResume() {
        super.onResume();

        // Tell the gameView resume method to execute
        gameView.resume();
    }

    // This method executes when the player quits the game
    @Override
    protected void onPause() {
        super.onPause();

        // Tell the gameView pause method to execute
        gameView.pause();
    }

    private static int randInt(int lower, int upper) {
        Random rand = new Random();
        int result = rand.nextInt(upper+1) + lower;
        return result;
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }


}