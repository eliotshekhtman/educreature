package com.prgm.eliotshekhtman.educreature;

import android.graphics.Bitmap;
import android.content.Context;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.media.MediaPlayer;
import android.graphics.BitmapFactory;
import android.view.View;

/**
 * Created by eliotshekhtman on 2/7/18.
 */

public class Sheeper extends creature {

    public Sheeper() {
        super(0, 30);

        setName("Sheeper");
        String d;
        d = "A common herbivore found grazing in the northern steppe, legend says it was once bred all across the world, but its numbers were driven down since the Fall.  Has tasty meat, and is the favorite food of many creatures.";
        passive = new int[] {0, 0, 0, 60000, 0};
        setDesc(d);
    }

    // Current, Stationary, Stationary blink, Left 1, Left 2, Right 1, Right 2, sad stat, sad blink
    public Bitmap[] setupPics() {
        Bitmap[] pics = new Bitmap[9];
        pics[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.sheeper_stationary);
        pics[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.sheeper_stationary);
        pics[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.sheeper_stationary_blink);
        pics[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.sheeper_left_1);
        pics[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.sheeper_left_2);
        pics[5] = BitmapFactory.decodeResource(context.getResources(), R.drawable.sheeper_right_1);
        pics[6] = BitmapFactory.decodeResource(context.getResources(), R.drawable.sheeper_right_2);
        pics[7] = BitmapFactory.decodeResource(context.getResources(), R.drawable.sheep_sad_stat);
        pics[8] = BitmapFactory.decodeResource(context.getResources(), R.drawable.sheep_sad_blink);
        return pics;
    }

    public int[] atk() {
        int[] a = new int[] {(int) (5 * Math.pow(1.1, getLevel())), 0, 0, 0, 0, 0, 0, 0};
        return a;
    }

    public inventoryItem[] setInventory() {
        inventoryItem[] a = new inventoryItem[1];
        return a;
    }

    public boolean[] passive() {
        boolean[] a = new boolean[] {false, false, false, false, false};
        if(passive[2] == 0) {
            a[2] = true;
            passive[2] = 60000;
        }
        for(int i = 0; i < passive.length; i++) { if(passive[i] != 0) passive[i]--; }
        return a;
    }
}
