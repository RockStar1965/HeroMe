package com.asuscomm.douglaskirk.herome;

import com.asuscomm.douglaskirk.herome.Activities.MainActivity;

/**
 * Created by RockStar on 2017-10-20.
 */


public final class Constants {
    private static int level;
    public void setGameLevel(int newLevel) { level = newLevel;}
    public String SuperPowers() { return "Robersaurus"; }
    public String InvalidScore() { return "System 999999";}
    public int maxGameLevel() { return 10; }        // The number of levels that is possible
    public int startGameLevel() { return 5;}       // The initial start level of the game.
    public int minGameLevel() { return 1;}
    public long maxGameTimeValue() { return 99999;}
    public int numberRC( ) {
        switch (level) {
            case 1:
            case 2:
                return 3;
            case 3:
                return 4;
            case 4:
            case 5:
                return 5;
            case 6:
            case 7:
                return 6;
            case 8:
            case 9:
                return 7;
            case 10:
                return 8;
        }
        return 8;
    };
    public int numberIcons() { return 100; }
}
