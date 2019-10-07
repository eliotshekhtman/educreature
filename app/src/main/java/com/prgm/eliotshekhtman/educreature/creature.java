package com.prgm.eliotshekhtman.educreature;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

/**
 * Created by eliotshekhtman on 1/31/18.
 */

public abstract class creature extends loot {
    Context context;
    private String name;
    private String desc;
    //private int rarity;
    private int level;
    private int exp;
    private int hcap;
    private int health;
    private double happiness; // When given things, happiness increases, when win, happiness increases, when lose, happiness decreases
    protected double tiredness; // as it's played with more, tiredness goes up
    private int[] status;
    protected inventoryItem[] inventory;
    protected int[] passive; // Measures time in milliseconds before it does the thingy
    // [healing, growth, resources, duplication, money]
    public Bitmap[] pic;
    protected int size;
    final protected long birthday;

    public creature() {
        birthday = System.currentTimeMillis();
        context = MyApplication.getAppContext();
        setRarity(0); level = 0; exp = 0; health = 10; happiness = 100; desc = "";
        status = new int[] {0, 0, 0, 0, 0, 0, 0, 0}; // measures number of turns with a status effect
        // fire, poison, wither, paralysis, nausea, smokescreen, martial trance, berserk
        pic = setupPics(); // sitting, attack 1, 2, move 1, 2
        size = 500;
        inventory = setInventory();
    }

    // Current, Stationary, Stationary blink, Left 1, Left 2, Right 1, Right 2, sad stat, sad blink
    abstract Bitmap[] setupPics();

    public void setStat() { pic[0] = pic[1]; }
    public void setBlink() { pic[0] = pic[2]; }
    public void setl1() { pic[0] = pic[3]; }
    public void setl2() { pic[0] = pic[4]; }
    public void setr1() { pic[0] = pic[5]; }
    public void setr2() { pic[0] = pic[6]; }
    public void setSStat() { pic[0] = pic[7]; }
    public void setSBlink() { pic[0] = pic[8]; }

    public creature( int r, int h) {
        this();
        setRarity(r);
        hcap = h;
        health = h;
    }

    protected abstract inventoryItem[] setInventory();

    public int getLevel() { return level; }
    public int getExp() { return exp; }
    public int getHealth() { return health; }
    public double getHappiness() { return happiness; }
    //public int getRarity() { return rarity; }
    public String getDesc() { return desc; }
    public String getName() { return name; }

    protected void setDesc( String d ) {
        desc = d;
    }
    protected void setName( String n ) {
        name = n;
    }

    public void changeHealth( int h ) {
        if( health + h <= hcap )
            health += h;
        else
            health = hcap;
    }
    public void changeHappiness( int h ) { happiness += h; }
    public void changeExp( int e ) {
        exp += e;
        if( exp >= ((Math.pow(level, 2) * 10) - (Math.pow(level-1, 2) * 10))) {
            exp -= ((Math.pow(level, 2) * 10) - (Math.pow(level-1, 2) * 10));
            levelUp();
        }
    }
    public void changeExp( boolean w, creature c ) {
        int e;
        if (w) { // If the creature won
            e = 1 + c.getLevel() * 3 + c.returnRarity() * 10;
        }
        else {
            e = c.getLevel() * 2;
        }
        changeExp(e);
    }
    public void changeHappiness( boolean w, creature c ) {
        int h;
        if (w) {
            h = 10 + c.getLevel() * 5 + c.returnRarity() * 2;
        }
        else {
            h = -5 - (level - c.getLevel()) - c.returnRarity();
        }
        changeHappiness(h);
    }

    // [attack, fire, poison, wither, paralysis, nausea, smokescreen]
    public abstract int[] atk();

    // [healing, growth, resources, duplication, money]
    // Child classes control the timers, and then they return true when it happens
    public abstract boolean[] passive();

    public int[] attack() {
        int[] a = atk();
        if(status[2] > 0)
            a[0] *= 0.8;
        if(status[4] > 0)
            a[0] *= 0.8;
        if(status[6] > 0)
            a[0] *= 1.3;
        if(status[7] > 0)
            a[0] *= 1.3;
        return a;
    }

    public void levelUp() {
        level++;
        hcap = (int)(hcap * 1.1);
        changeHealth(100);
        changeHappiness(20);
    }

    public String toString() {
        String s = "";
        s += "It looks ";
        String feel = "";
        if(tiredness <= 0)
            feel += "";
        else if(tiredness < 50 & happiness < 50)
            feel += "tired and ";
        else if(tiredness < 50)
            feel += "tired but ";
        if(happiness < 0)
            feel += "miserable";
        else if(happiness < 50)
            feel += "sad";
        else if(happiness < 100)
            feel += "content";
        else
            feel += "happy";
        if(tiredness > 50)
            feel = "exhausted";
        s += feel + "." + "\n";
        s += "Creature" + "\n" + "Name: " + name + "     " + "Rarity: ";
        s += getRarity() + "\n";
        s += "Description: " + desc + "\n";
        s += "Level: " + level + "     " + "Experience: " + exp + " (" + (((Math.pow(level, 2) * 10) - (Math.pow(level-1, 2) * 10)) - exp) + " to go)" + "\n";
        s += "Max health: " + hcap + "     " + "Health: " + health;
        //s += "\n" + "Happiness: " + happiness;
        return s;
    }

    protected void fire(int a) { status[0] = a; }
    protected void poison(int a) { status[1] = a; }
    protected void wither(int a) { status[2] = a; }
    protected void paralyze(int a) { status[3] = a; }
    protected void nausea(int a) { status[4] = a; }
    protected void smokescreen(int a) { status[5] = a; }
    protected void trance(int a) { status[6] = a; }
    protected void berserk(int a) { status[7] = a; }

    public void attacked(int[] a) {
        for(int i = 0; i < a.length; i++) {
            if(a[i] != 0 & a[i] > status[i+1])
                status[i+1] = a[i];
            else if(status[i+1] != 0)
                status[i+1]--;
        }
        health = health - a[0] < 0 ? 0 : health - a[0];
    }

    public void tick() {
        for(int i = 0; i < status.length; i++) {
            if(status[i] > 0) {
                switch(i) {
                    case 0:
                        inventory[(int) (Math.random() * inventory.length)].fire();
                    case 1:
                    case 2:
                        health = health - (int) (0.05 * hcap) < 0 ? 0 : health - (int) (0.05 * hcap);
                        break;
                }
            }
        }
        if(tiredness > 0)
            tiredness -= 0.01;
        if(happiness > -50)
            happiness -= 0.001;
        else
            happiness += 0.01;
    }

    public void play() {
        Log.w("MyApp", "Played");
        tiredness += 15;
        happiness += 10 - tiredness / 5;
    }

    public void feed() {
        if(MainActivity.gold > 10) {
            MainActivity.gold -= 10;
            happiness += 10;
            if(tiredness > 50)
                tiredness -= 50;
            else
                tiredness = 0;
        }
    }
}
