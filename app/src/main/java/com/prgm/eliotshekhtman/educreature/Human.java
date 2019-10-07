package com.prgm.eliotshekhtman.educreature;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Human extends creature implements inventoryItem {

    public Human() {
        super(0, 30);
        setName("Human");
        String d;
        d = "A previously dominant species, now down on its luck.  Fights in packs.";
        passive = new int[] {0, 0, 0, 86400000, 0};
        setDesc(d);
    }

    public Bitmap[] setupPics() {
        Bitmap[] pics = new Bitmap[9];
        pics[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.human_stationary);
        pics[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.human_stationary);
        pics[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.human_blink);
        pics[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.human_l1);
        pics[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.human_l2);
        pics[5] = BitmapFactory.decodeResource(context.getResources(), R.drawable.human_r1);
        pics[6] = BitmapFactory.decodeResource(context.getResources(), R.drawable.human_r2);
        pics[7] = BitmapFactory.decodeResource(context.getResources(), R.drawable.human_sad_stat);
        pics[8] = BitmapFactory.decodeResource(context.getResources(), R.drawable.human_sad_blink);
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
        if(passive[3] == 0) {
            a[3] = true;
            passive[3] = 86400000;
        }
        for(int i = 0; i < passive.length; i++) { if(passive[i] != 0) passive[i]--; }

        return a;
    }

    public void fire() {
        fire(3);
    }
}
