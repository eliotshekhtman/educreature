package com.prgm.eliotshekhtman.educreature;

import java.util.Random;

/**
 * Created by eliotshekhtman on 1/31/18.
 */

public class lootcrate extends loot implements lootTable {
    private int size;

    public lootcrate() {
        Random rand = new Random();
        setRarity(0);
        size = rand.nextInt(3);
    }
    public lootcrate(int a) {
        Random rand = new Random();
        setRarity(a);
        size = rand.nextInt(3) + size * 3;
    }

    public String[] opencrate() {
        String[] loot = new String[size];
        Random rand = new Random();
        for(int i = 0; i < size; i++) {
            int ref; String str;
            int staint = rand.nextInt(1000); // All items can be common, uncommon, rare, and ultrarare
            int status;

            if(staint < 750) status = 0; // Common
            else if(staint < 950) status = 1; // Uncommon
            else if (staint < 999) status = 2; // Rare
            else status = 3; // Ultrarare

            if(status == 0) {
                ref = rand.nextInt(cref.length);
                str = "c" + cref[ref];
            }
            else if(status == 1) {
                ref = rand.nextInt(uref.length);
                str = "u" + uref[ref];
            }
            else if(status == 2) {
                ref = rand.nextInt(rref.length);
                str = "r" + rref[ref];
            }
            else {
                ref = rand.nextInt(aref.length);
                str = "a" + aref[ref];
            }

            loot[i] = str;
        }
        return loot; // Loot will be parsed in MainActivity using the lootTable interface
    }

    public int scrapcrate() {
        return size * 10;
    }

    public String toString() {
        String s = "";
        s += "Lootcrate " + "\t" + "Size: " + getSize();
        return s;
    }


}
