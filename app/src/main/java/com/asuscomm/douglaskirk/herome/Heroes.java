package com.asuscomm.douglaskirk.herome;

import android.app.Application;

/**
 * Created by RockStar on 2017-09-20.
 */


    public class Heroes extends Application{

        public class HeroesIcon {
            private int heroIconsBW;
            private int heroIconsColor;
            private String heroIconsName;
            private int heroIconsType;
        }

        private HeroesIcon heroData[] = new HeroesIcon[100];




        private int[] iconsBW = {
                R.drawable.vh01, R.drawable.vh02, R.drawable.vh03, R.drawable.vh04, R.drawable.vh05, R.drawable.vh06, R.drawable.vh07, R.drawable.vh08, R.drawable.vh09, R.drawable.vh10,
                R.drawable.vh11, R.drawable.vh12, R.drawable.vh13, R.drawable.vh14, R.drawable.vh15, R.drawable.vh16, R.drawable.vh17, R.drawable.vh18, R.drawable.vh19, R.drawable.vh20,
                R.drawable.vh21, R.drawable.vh22, R.drawable.vh23, R.drawable.vh24, R.drawable.vh25, R.drawable.vh26, R.drawable.vh27, R.drawable.vh28, R.drawable.vh29, R.drawable.vh30,
                R.drawable.vh31, R.drawable.vh32, R.drawable.vh33, R.drawable.vh34, R.drawable.vh35, R.drawable.vh36, R.drawable.vh37, R.drawable.vh38, R.drawable.vh39, R.drawable.vh40,
                R.drawable.vh41, R.drawable.vh42, R.drawable.vh43, R.drawable.vh44, R.drawable.vh45, R.drawable.vh46, R.drawable.vh47, R.drawable.vh48, R.drawable.vh49, R.drawable.vh50,
                R.drawable.vh51, R.drawable.vh52, R.drawable.vh53, R.drawable.vh54, R.drawable.vh55, R.drawable.vh56, R.drawable.vh57, R.drawable.vh58, R.drawable.vh59, R.drawable.vh60,
                R.drawable.vh61, R.drawable.vh62, R.drawable.vh63, R.drawable.vh64, R.drawable.vh65, R.drawable.vh66, R.drawable.vh67, R.drawable.vh68, R.drawable.vh69, R.drawable.vh70,
                R.drawable.vh71, R.drawable.vh72, R.drawable.vh73, R.drawable.vh74, R.drawable.vh75, R.drawable.vh76, R.drawable.vh77, R.drawable.vh78, R.drawable.vh79, R.drawable.vh80,
                R.drawable.vh81, R.drawable.vh82, R.drawable.vh83, R.drawable.vh84, R.drawable.vh85, R.drawable.vh86, R.drawable.vh87, R.drawable.vh88, R.drawable.vh89, R.drawable.vh90,
                R.drawable.vh91, R.drawable.vh92, R.drawable.vh93, R.drawable.vh94, R.drawable.vh95, R.drawable.vh96, R.drawable.vh97, R.drawable.vh98, R.drawable.vh99, R.drawable.vh100};

        private int[] iconsColor = {
                R.drawable.vhc01, R.drawable.vhc02, R.drawable.vhc03, R.drawable.vhc04, R.drawable.vhc05, R.drawable.vhc06, R.drawable.vhc07, R.drawable.vhc08, R.drawable.vhc09, R.drawable.vhc10,
                R.drawable.vhc11, R.drawable.vhc12, R.drawable.vhc13, R.drawable.vhc14, R.drawable.vhc15, R.drawable.vhc16, R.drawable.vhc17, R.drawable.vhc18, R.drawable.vhc19, R.drawable.vhc20,
                R.drawable.vhc21, R.drawable.vhc22, R.drawable.vhc23, R.drawable.vhc24, R.drawable.vhc25, R.drawable.vhc26, R.drawable.vhc27, R.drawable.vhc28, R.drawable.vhc29, R.drawable.vhc30,
                R.drawable.vhc31, R.drawable.vhc32, R.drawable.vhc33, R.drawable.vhc34, R.drawable.vhc35, R.drawable.vhc36, R.drawable.vhc37, R.drawable.vhc38, R.drawable.vhc39, R.drawable.vhc40,
                R.drawable.vhc41, R.drawable.vhc42, R.drawable.vhc43, R.drawable.vhc44, R.drawable.vhc45, R.drawable.vhc46, R.drawable.vhc47, R.drawable.vhc48, R.drawable.vhc49, R.drawable.vhc50,
                R.drawable.vhc51, R.drawable.vhc52, R.drawable.vhc53, R.drawable.vhc54, R.drawable.vhc55, R.drawable.vhc56, R.drawable.vhc57, R.drawable.vhc58, R.drawable.vhc59, R.drawable.vhc60,
                R.drawable.vhc61, R.drawable.vhc62, R.drawable.vhc63, R.drawable.vhc64, R.drawable.vhc65, R.drawable.vhc66, R.drawable.vhc67, R.drawable.vhc68, R.drawable.vhc69, R.drawable.vhc70,
                R.drawable.vhc71, R.drawable.vhc72, R.drawable.vhc73, R.drawable.vhc74, R.drawable.vhc75, R.drawable.vhc76, R.drawable.vhc77, R.drawable.vhc78, R.drawable.vhc79, R.drawable.vhc80,
                R.drawable.vhc81, R.drawable.vhc82, R.drawable.vhc83, R.drawable.vhc84, R.drawable.vhc85, R.drawable.vhc86, R.drawable.vhc87, R.drawable.vhc88, R.drawable.vhc89, R.drawable.vhc90,
                R.drawable.vhc91, R.drawable.vhc92, R.drawable.vhc93, R.drawable.vhc94, R.drawable.vhc95, R.drawable.vhc96, R.drawable.vhc97, R.drawable.vhc98, R.drawable.vhc99, R.drawable.vhc100};

        private String[] iconNames = {
                "Batman",               "Robin",            "Spiderman",    "Green Lantern", "Hawkeye", "Captain America", "Thor", "Loki", "Green Arrow", "Hulk",
                "Superman",             "Daredevil",        "Wolverine",    "Wonder Woman", "Catwoman", "Flash", "Punisher", "Ironman", "Bpdr", "Joker",
                "Two-Face Coin",        "Two-Face",         "Silver Surfer", "Magneto", "Xavier", "Thing", "Human Torch", "Mr Fantastic", "Infinity Gauntlet", "Dr Manhattan",
                "Rorschach",            "Venom",            "The Riddler",  "Aquaman", "Spawn", "Ghost Rider", "Rocketeer", "The Atom", "Nick Fury", "Cyclops",
                "Judge Dredd",          "Deadpool",         "Dr Spectrum",  "Solar", "Black Widow", "Iron Fist", "Poison Ivy", "Dr Doom", "Tmnt", "The Spirit",
                "Dr Octopus",           "Red Skull",        "Gambit",       "Scarecrow", "Deathstroke", "Brainiac", "Moon Knight", "Karnak", "Black Adam", "Penguin",
                "Shredder",             "Hawkeye",          "Superman-Hair", "Bane", "Adamantium\nShield", "Daredevil\n Glasses", "Mutagen", "Hellboy's Good\n Samaritan", "Penguin's\n Umbrella", "Starlord's\n Element Gun",
                "Heman's\nPower Sword",  "Luke's 1st\nLightsaber", "Luke's 2nd\nLightsaber", "Daredevil's\nBilly Cub", "Green Goblin's\n Pumpkin", "Kryptonita", "Elektra's Sais", "Bow And Arrow", "Joker's Gun", "Joker's Buzzer",
                "Leonardo's\nKatana", "Michelangelo's\nNunchakus", "Donatello's Bo Staff", "Raphael's Sais", "Freeze\nRay Gun", "New Freeze\nRay Gun", "Adamantium\nClaws", "Mjolnir", "Ghost Rider's\n Hellfire Chain", "Dr Strange's\nEye Of Agamotto",
                "Gl Power Ring", "Phantom's\nRing", "Batarang", "Batman's\nUtility Belt", "Dr Fate", "X-Men Logo", "Fantastic 4", "Avengers", "Spiderman\nSpider", "Spiderman-Web"};

        private int[] iconTypes = {
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 1, 0, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 0, 1, 1};

        private int[] iconSource = {
                3, 3, 1, 3, 3, 1, 3, 3, 3, 1,  // 3 0 7
                3, 3, 2, 3, 1, 2, 1, 1, 1, 1,  // 5 2 3
                0, 1, 3, 2, 2, 1, 1, 1, 1, 1,  // 6 2 1
                2, 2, 1, 3, 1, 1, 2, 2, 3, 3,  // 3 4 3
                3, 3, 2, 2, 3, 2, 2, 1, 1, 3,  // 2 4 4
                1, 1, 2, 2, 1, 0, 0, 0, 0, 1,  // 4 2 0
                1, 3, 0, 2, 0, 0, 0, 0, 0, 0,  // 1 1 1
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 1, 0, 0}; // 1 0 0
                                                // 25 15 15



    public int On(int index) {
        return iconsColor[index];
    }

    public int Off(int index) {
        return iconsBW[index];
    }

    public String Text (int index) { return iconNames[index]; }

    public int Type (int index) {
        return iconTypes[index];
    }

    public int Source (int index) {
        return iconSource[index];
    }

    public int getMaximum () { return 100;  }

};



