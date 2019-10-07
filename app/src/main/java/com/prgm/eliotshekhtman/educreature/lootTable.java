package com.prgm.eliotshekhtman.educreature;

import android.content.Context;

/**
 * Created by eliotshekhtman on 1/31/18.
 */

public interface lootTable {
    //final Context con = this;
    // Commons
    String[] cref = {"gold", "sheeper"};
    Object[] cite = {500, new Sheeper()};

    // Uncommons
    String[] uref = {"gold", "human"};
    Object[] uite = {1000, new Human()};

    // Rares
    String[] rref = {"gold"};
    Object[] rite = {3000};

    // Ultrarares
    String[] aref = {"gold"};
    Object[] aite = {5000};
}
