package com.prgm.eliotshekhtman.educreature;

/**
 * Created by eliotshekhtman on 2/7/18.
 */

public abstract class loot {
    private int rarity;

    protected void setRarity(int r) {
        rarity = r;
    }

    public int returnRarity() {
        return rarity;
    }

    public String getRarity() {
        switch(rarity) {
            case 0:
                return "Common";
            case 1:
                return "Uncommon";
            case 2:
                return "Rare";
            case 3:
                return "Ultrarare";
            case 4:
                return "Unique";
            default:
                return "Unknown";
        }
    }

    public String getSize() {
        switch(rarity) {
            case 0:
                return "Tiny";
            case 1:
                return "Small";
            case 2:
                return "Medium";
            case 3:
                return "Large";
            case 4:
                return "Huge";
            default:
                return "Unknown";
        }
    }
}
