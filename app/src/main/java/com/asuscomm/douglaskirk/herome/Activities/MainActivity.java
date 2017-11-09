package com.asuscomm.douglaskirk.herome.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;

import com.asuscomm.douglaskirk.herome.Constants;
import com.asuscomm.douglaskirk.herome.Fragments.AboutFragment;
import com.asuscomm.douglaskirk.herome.Fragments.LeaderFragment;
import com.asuscomm.douglaskirk.herome.Fragments.ListFragment;
import com.asuscomm.douglaskirk.herome.Fragments.LoginFragment;
import com.asuscomm.douglaskirk.herome.Fragments.ResultsFragment;
import com.asuscomm.douglaskirk.herome.Fragments.SearchFragment;
import com.asuscomm.douglaskirk.herome.Fragments.TestFragment;

import com.asuscomm.douglaskirk.herome.R;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


import static com.asuscomm.douglaskirk.herome.Activities.MainActivity.menuItems.MENU_ABOUT;
import static com.asuscomm.douglaskirk.herome.Activities.MainActivity.menuItems.MENU_LEADER;
import static com.asuscomm.douglaskirk.herome.Activities.MainActivity.menuItems.MENU_LIST;
import static com.asuscomm.douglaskirk.herome.Activities.MainActivity.menuItems.MENU_LOGIN;
import static com.asuscomm.douglaskirk.herome.Activities.MainActivity.menuItems.MENU_REGISTER;
import static com.asuscomm.douglaskirk.herome.Activities.MainActivity.menuItems.MENU_RESULTS;
import static com.asuscomm.douglaskirk.herome.Activities.MainActivity.menuItems.MENU_SEARCH;
import static com.asuscomm.douglaskirk.herome.Activities.MainActivity.menuItems.MENU_TEST;

public class MainActivity extends AppCompatActivity implements
        LoginFragment.LoginInteractionListener,
        LeaderFragment.ScoreInteractionListener,
        SearchFragment.SearchInteractionListener,
        ListFragment.ListInteractionListener,
        TestFragment.TestInteractionListener,
        AboutFragment.AboutInteractionListener,
        ResultsFragment.ResultsInteractionListener {

    Bundle bundle = new Bundle();

    private static ProgressDialog progress;
    private static SharedPreferences prefs;
    private static SharedPreferences.Editor editor;

    private static final Constants constants = new Constants();
    private static ImageView[] gameLevelImage = new ImageView[constants.maxGameLevel()];

    private static Button menuLevelUpBtn;
    private static Button menuLevelDown2Btn;
    private static Button menuTestBtn;
    private static Button menuLeaderBtn;
    private static Button menuExitBtn;
    private static Button menuAboutBtn;

    enum menuItems {MENU_LOGIN, MENU_REGISTER, MENU_SEARCH, MENU_LIST, MENU_TEST, MENU_RESULTS, MENU_LEADER, MENU_ABOUT};

    private static menuItems menuType = MENU_LOGIN;
    private static int gameType = 0;
    private static int gameLevel = constants.startGameLevel();


    private static int[] powerList = new int[constants.maxGameLevel()];
    private static int[] powerSelected = new int[constants.maxGameLevel()];
    private static int[] gameList = new int[constants.numberRC() * constants.numberRC()];

    private static String textEmail = new String();
    private static String textPassword  = new String();
    private static String textPowerup  = new String();
    private static String textUsername  = new String();
    private static String textName  = new String();
    private static String powerRank  = new String();

    private static String restApiSource = "";
    private static String restApiKey = "";
    private static String restApiMessage = null;

    private static String[] textHighScore = new String[constants.maxGameLevel()];
    private static String[] textUserHighScore = new String[constants.maxGameLevel()];

    private static long[] highScore = new long[constants.maxGameLevel()];

    private static long timeList = constants.maxGameTimeValue();
    private static long timeTest = constants.maxGameTimeValue();
    private static long timeTotal = constants.maxGameTimeValue();

    private boolean online = true;
    private static boolean powerDebug = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragment_container);
        prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);

        mainResizeLayout();
        mainPreferences();

        if (fragment == null) {
            fragment = new LoginFragment();
            fragment.setArguments(bundle);
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.fragment_container, fragment);
            transaction.commit();
        }

    }

    private void mainPreferences() {
        menuType = MENU_LOGIN;
        mainMenuInit();

        for (int i = 0; i < constants.maxGameLevel(); i++) {
            String parmName = "highScore" +Integer.toString(i);
            highScore[i] = prefs.getLong(parmName,99999);
        }
        bundle.putLongArray("highScore", highScore);

        gameLevel = prefs.getInt("gamelevel",constants.startGameLevel());

        setGameParameters();

        textEmail = prefs.getString("textEmail","");
        textPassword = prefs.getString("textPassword","");
        textPowerup = prefs.getString("textPowerup","");
        textUsername = prefs.getString("textUsername","");
        textName = prefs.getString("textName","");

        restApiSource = "login";
        restApiKey = "";
        restApiMessage = null;

        bundle.putString("restApiSource",restApiSource);
        bundle.putString("restApiMessage",restApiMessage);
        bundle.putString("textEmail", textEmail);
        bundle.putString("textPassword", textPassword);
        bundle.putString("textPowerup", textPowerup);
        bundle.putString("textUsername", textUsername);
        bundle.putString("textName", textName);
        bundle.putBoolean("online", online);
    }

    private void mainResizeLayout() {
        ImageView gameTitleImage = (ImageView) findViewById(R.id.gameTitleImage);

        gameLevelImage[0] = (ImageView) findViewById(R.id.gameLevelImage1);
        gameLevelImage[1] = (ImageView) findViewById(R.id.gameLevelImage2);
        gameLevelImage[2] = (ImageView) findViewById(R.id.gameLevelImage3);
        gameLevelImage[3] = (ImageView) findViewById(R.id.gameLevelImage4);
        gameLevelImage[4] = (ImageView) findViewById(R.id.gameLevelImage5);
        gameLevelImage[5] = (ImageView) findViewById(R.id.gameLevelImage6);
        gameLevelImage[6] = (ImageView) findViewById(R.id.gameLevelImage7);
        gameLevelImage[7] = (ImageView) findViewById(R.id.gameLevelImage8);
        gameLevelImage[8] = (ImageView) findViewById(R.id.gameLevelImage9);
        gameLevelImage[9] = (ImageView) findViewById(R.id.gameLevelImage10);

        menuLevelUpBtn = (Button) findViewById(R.id.menuLevelUpBtn);
        menuLevelDown2Btn = (Button) findViewById(R.id.menuLevelDown1Btn);
        menuTestBtn = (Button) findViewById(R.id.menuTestBtn);
        menuLeaderBtn = (Button) findViewById(R.id.menuLeaderBtn);
        menuExitBtn = (Button) findViewById(R.id.menuExitBtn);
        menuAboutBtn = (Button) findViewById(R.id.menuAboutBtn);

        final float densityValue = this.getResources().getDisplayMetrics().density;
        final float widthPixelsValue = this.getResources().getDisplayMetrics().widthPixels;

        final int newMenuWidth = (int) ( this.getResources().getDisplayMetrics().widthPixels * 100 / 350 );
        final int newMenuHeight = (int) ( this.getResources().getDisplayMetrics().widthPixels * 1 / 10 );
        final int newMenuMargin = (int) ( this.getResources().getDisplayMetrics().widthPixels * 1 / 40 );

        final int newImageTitleLayoutWidth  = (int) ( this.getResources().getDisplayMetrics().widthPixels * 2 / 3 );
        final int newImageTitleLayoutHeight = newImageTitleLayoutWidth / 5;

        // Five icons per larger image
        final int newGameLevelHeight = newImageTitleLayoutHeight / 5;
        final int newGameLevelWidth = newGameLevelHeight;

        final float newTextSize  =  (float)( this.getResources().getDisplayMetrics().widthPixels / densityValue/ 25 );

        menuLevelUpBtn.setTextSize(newTextSize);
        menuLevelDown2Btn.setTextSize(newTextSize);
        menuTestBtn.setTextSize(newTextSize);
        menuLeaderBtn.setTextSize(newTextSize);
        menuExitBtn.setTextSize(newTextSize);
        menuAboutBtn.setTextSize(newTextSize);

        ConstraintLayout newLayout = (ConstraintLayout)findViewById(R.id.menuConstraintLayout);
        ConstraintSet newSet = new ConstraintSet();
        newSet.clone(newLayout);

        newSet.constrainHeight(R.id.menuLevelUpBtn, newMenuHeight);
        newSet.constrainWidth(R.id.menuLevelUpBtn, newMenuWidth);
        newSet.setMargin(R.id.menuLevelUpBtn, ConstraintSet.BOTTOM ,newMenuMargin);

        newSet.constrainHeight(R.id.menuTestBtn, newMenuHeight);
        newSet.constrainWidth(R.id.menuTestBtn, newMenuWidth);
        newSet.setMargin(R.id.menuTestBtn, ConstraintSet.BOTTOM ,newMenuMargin);

        newSet.constrainHeight(R.id.menuLevelDown1Btn, newMenuHeight);
        newSet.constrainWidth(R.id.menuLevelDown1Btn, newMenuWidth);
        newSet.setMargin(R.id.menuLevelDown1Btn, ConstraintSet.BOTTOM ,newMenuMargin);

        newSet.constrainHeight(R.id.menuLeaderBtn, newMenuHeight);
        newSet.constrainWidth(R.id.menuLeaderBtn, newMenuWidth);
        newSet.setMargin(R.id.menuLeaderBtn, ConstraintSet.BOTTOM ,0);

        newSet.constrainHeight(R.id.menuExitBtn, newMenuHeight);
        newSet.constrainWidth(R.id.menuExitBtn, newMenuWidth);
        newSet.setMargin(R.id.menuExitBtn, ConstraintSet.BOTTOM ,0);

        newSet.constrainHeight(R.id.menuAboutBtn, newMenuHeight);
        newSet.constrainWidth(R.id.menuAboutBtn, newMenuWidth);
        newSet.setMargin(R.id.menuAboutBtn, ConstraintSet.BOTTOM ,0);
        newSet.applyTo(newLayout);

        newLayout = (ConstraintLayout)findViewById(R.id.titleConstraintLayout);
        newSet.clone(newLayout);

        newSet.constrainHeight(R.id.gameTitleImage, newImageTitleLayoutHeight);
        //set1.constrainWidth(R.id.gameTitleImage, newImageTitleLayoutWidth);
        newSet.constrainHeight(R.id.gameLevelImage1, newGameLevelHeight);
        newSet.constrainWidth(R.id.gameLevelImage1, newGameLevelWidth);
        newSet.constrainHeight(R.id.gameLevelImage2, newGameLevelHeight);
        newSet.constrainWidth(R.id.gameLevelImage2, newGameLevelWidth);
        newSet.constrainHeight(R.id.gameLevelImage3, newGameLevelHeight);
        newSet.constrainWidth(R.id.gameLevelImage3, newGameLevelWidth);
        newSet.constrainHeight(R.id.gameLevelImage4, newGameLevelHeight);
        newSet.constrainWidth(R.id.gameLevelImage4, newGameLevelWidth);
        newSet.constrainHeight(R.id.gameLevelImage5, newGameLevelHeight);
        newSet.constrainWidth(R.id.gameLevelImage5, newGameLevelWidth);
        newSet.constrainHeight(R.id.gameLevelImage6, newGameLevelHeight);
        newSet.constrainWidth(R.id.gameLevelImage6, newGameLevelWidth);
        newSet.constrainHeight(R.id.gameLevelImage7, newGameLevelHeight);
        newSet.constrainWidth(R.id.gameLevelImage7, newGameLevelWidth);
        newSet.constrainHeight(R.id.gameLevelImage8, newGameLevelHeight);
        newSet.constrainWidth(R.id.gameLevelImage8, newGameLevelWidth);
        newSet.constrainHeight(R.id.gameLevelImage9, newGameLevelHeight);
        newSet.constrainWidth(R.id.gameLevelImage9, newGameLevelWidth);
        newSet.constrainHeight(R.id.gameLevelImage10, newGameLevelHeight);
        newSet.constrainWidth(R.id.gameLevelImage10, newGameLevelWidth);
        newSet.applyTo(newLayout);
    }

    // Step 0 Login or Register
    public void loadLoginFragment() {
        mainMenuInit();
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        //getSupportFragmentManager().beginTransaction();
        LoginFragment loginFragment = new LoginFragment();
        loginFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, loginFragment).commit();
    }



   // TODO

    @Override
    public void onSearch(int[] gamesListNew ) {
        gameList = gamesListNew;
        bundle.putIntArray("gameList", gameList);
        menuTestBtn.setEnabled(true);
        menuTestBtn.getBackground().setAlpha(255);
        menuTestBtn.setText("Show Results");
    }


    @Override
    public void onList(int[] newPowerList, long newTimeList) {
        powerList = newPowerList;
        timeList = newTimeList;

        bundle.putIntArray("powerList", powerList);
        bundle.putLong("timeList", timeList);
     }



    @Override
    public void onTestButtonClick(int[] powerSelectedNew, long newTimeTest, boolean testButtonEnable ) {
        if (testButtonEnable) {
            timeTest = newTimeTest;
            timeTotal = timeList + timeTest;
            powerSelected = processTest(powerList, powerSelectedNew, gameLevel);
            bundle.putLong("timeTest", timeTest);
            bundle.putLong("timeTotal", timeTotal);
            bundle.putIntArray("powerSelected",powerSelected);

            menuTestBtn.setEnabled(true);
            menuTestBtn.getBackground().setAlpha(255);
        } else {
            menuTestBtn.setEnabled(false);
            menuTestBtn.getBackground().setAlpha(128);
        }
         menuTestBtn.setEnabled(testButtonEnable);
    }


     // Step 2 Update teh screen with random icons, retrieve the gameList
    public void loadSearchFragment() {
        SearchFragment searchFragment = new SearchFragment();
        searchFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, searchFragment).commit();
    }


    public void loadResultsFragment() {
        ResultsFragment gameResultsFragment = new ResultsFragment();
        gameResultsFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, gameResultsFragment).commit();
    }


    public void initLeaderFragment() {
        if (online){
            String param1 = "http://DouglasKirk.asuscomm.com/task_manager/v1/index.php/highscore";
            String param2 = "game=" + Integer.toString(gameType);
            param2 += "&level=" + Integer.toString(gameLevel) + "&source=loadLeaderFragment";
            String param3 = restApiKey;
            new PostClass(this).execute(param1, param2, param3);
        } else {
            loadLeaderFragment();
        }
    }


    public void loadLeaderFragment(){
        LeaderFragment leaderFragment = new LeaderFragment();
        leaderFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, leaderFragment).commit();
    }

    private int findIcon(int[] source, int icon) {
        if ( source.length >= gameLevel ) {
            for (int i = 0; i < gameLevel; i++) {
                if (source[i] == icon) {
                    return i;
                }
            }
        }
        return -1;
    }

    private boolean processScore(int[] powerList, int[] powerSelected, int gameLevel) {
        int[] powerTest = new int[constants.maxGameLevel()];
        int[] powerError = new int[constants.maxGameLevel()];

        int powerCorrect = 0;

        for (int i = 0; i < gameLevel; i++) {
            powerTest[i] = -1;
            powerError[i] = powerSelected[i];
        }

        // Mask off any correct values in the powerError which was a copy of what the user selected.
        powerCorrect = 0;
        for (int i = 0; i < gameLevel; i++) {
            int found = findIcon(powerSelected, powerList[i]);
            if (found >= 0) {
                powerTest[i] = powerSelected[found];
                powerError[found] = -1;
                powerCorrect++;
            }
        }

        if (powerCorrect != gameLevel) {
            return false;
        } else {
            return true;
        }
    }


    private int[] processTest(int[] powerList, int[] powerSelected, int gameLevel) {
        int[] powerTest = new int[constants.maxGameLevel()];
        int[] powerError = new int[constants.maxGameLevel()];

        int powerCorrect = 0;

        for (int i = 0; i < gameLevel; i++) {
            powerTest[i] = -1;
            powerError[i] = powerSelected[i];
        }

        // Mask off any correct values in the powerError which was a copy of what the user selected.
        powerCorrect = 0;
        for (int i = 0; i < gameLevel; i++) {
            int found = findIcon(powerSelected, powerList[i]);
            if (found >= 0) {
                powerTest[i] = powerSelected[found];
                powerError[found] = -1;
                powerCorrect++;
            }
        }

        if (powerCorrect != gameLevel) {
            int errorIndex = 0;
            int powerIndex = 0;
            int testIndex = powerCorrect;

            while ((testIndex++ < gameLevel) && (errorIndex < gameLevel) && (powerIndex < gameLevel)) {
                while ((errorIndex < gameLevel) && (powerError[errorIndex] < 0)) {
                    errorIndex++;
                }
                while ((powerIndex < gameLevel) && (powerTest[powerIndex] >= 0)) {
                    powerIndex++;
                }
                powerTest[powerIndex] = powerError[errorIndex];
                errorIndex++;
                powerIndex++;
            }
        }
        return (powerTest);
    }




    private void setGameParameters() {

        if (gameLevel > constants.maxGameLevel()) {
            gameLevel = constants.maxGameLevel();
        } else if (gameLevel < constants.minGameLevel()) {
            gameLevel = constants.minGameLevel();
        }
        constants.setGameLevel(gameLevel);
        bundle.putInt("gameLevel",gameLevel);

        editor = prefs.edit();
        editor.putInt("gamelevel", gameLevel);
        editor.commit();

        switch (menuType) {
            case MENU_LIST:
            case MENU_SEARCH:
            case MENU_TEST:
                menuLevelUpBtn.setEnabled(false);
                menuLevelUpBtn.getBackground().setAlpha(128);
                menuLevelDown2Btn.setEnabled(false);
                menuLevelDown2Btn.getBackground().setAlpha(128);
                break;
            case MENU_LOGIN:
            case MENU_REGISTER:
                menuLevelUpBtn.setEnabled(true);
                menuLevelUpBtn.getBackground().setAlpha(255);
                break;
            default:
                if (gameLevel < constants.maxGameLevel()) {
                    menuLevelUpBtn.setEnabled(true);
                    menuLevelUpBtn.getBackground().setAlpha(255);
                } else {
                    menuLevelUpBtn.setEnabled(false);
                    menuLevelUpBtn.getBackground().setAlpha(128);
                }
                if (gameLevel > constants.minGameLevel()) {
                    menuLevelDown2Btn.setEnabled(true);
                    menuLevelDown2Btn.getBackground().setAlpha(255);
                } else {
                    menuLevelDown2Btn.setEnabled(false);
                    menuLevelDown2Btn.getBackground().setAlpha(128);
                }

                break;
            }


        for (int i = 0; i < constants.maxGameLevel(); i++) {
            if (i < gameLevel) {
                if (powerDebug) {
                    gameLevelImage[i].setBackgroundResource(R.drawable.vhc32);
                } else {
                    gameLevelImage[i].setBackgroundResource(R.drawable.vhc03);
                }
                // TODO upon closing test I believe one of the icons gets left in a lower Alpha state
                gameLevelImage[i].getBackground().setAlpha(255);
            } else {
                gameLevelImage[i].setBackgroundResource(R.drawable.itemselectedholder);
            }
        }
    }


    public void setResponse(String data) {
        String restApi = "";
        bundle.putString("restApiMessage", null);

        String[] dataParts = data.replaceAll( "[\"{}]", "").split(",");

        if ((dataParts[0].equals("error:false")) && (dataParts.length > 1)) {
            for (String param : dataParts) {
                String[] subParts = param.split(":");
                if ((subParts[0].equals("apikey")) && (subParts.length > 1)) {
                    restApiKey = subParts[1];

                } else if ((subParts[0].equals("rank")) && (subParts.length > 1)) {
                    powerRank = subParts[1];
                    bundle.putString("powerRank", powerRank);

                } else if ((subParts[0].equals("highscore_all")) && (subParts.length > 1)) {
                    textHighScore = subParts[1].split("#");
                    bundle.putStringArray("textHighScore", textHighScore);

                } else if ((subParts[0].equals("highscore_user")) && (subParts.length > 1)) {
                    textUserHighScore = subParts[1].split("#");
                    bundle.putStringArray("textUserHighScore", textUserHighScore);

                } else if ((subParts[0].equals("name")) && (subParts.length > 1)) {
                    textName = subParts[1];
                    bundle.putString("textName", textName);

                } else if ((subParts[0].equals("username")) && (subParts.length > 1)) {
                    textUsername = subParts[1];
                    bundle.putString("textUsername", textUsername);

                } else if ((subParts[0].equals("rest")) && (subParts.length > 1)) {
                    restApi = subParts[1];

                } else if ((subParts[0].equals("source")) && (subParts.length > 1)) {
                    restApiSource = subParts[1];

                } else if ((subParts[0].equals("message")) && (subParts.length > 1)) {
                    restApiMessage = subParts[1];
                    bundle.putString("restApiMessage", restApiMessage);
                }
            }

            if (restApiSource.equals("loadResultsFragment")) {
                menuType = MENU_RESULTS;
                mainMenuInit();
                loadResultsFragment();
            } else if (restApiSource.equals("initLeaderFragment")) {
                progress.dismiss();
                menuType = MENU_LEADER;
                mainMenuInit();
                initLeaderFragment();
            } else if (restApiSource.equals("loadLeaderFragment")) {
                menuType = MENU_LEADER;
                mainMenuInit();
                loadLeaderFragment();
            } else if (restApiSource.equals("loadSearchFragment")) {
                menuType = MENU_SEARCH;
                mainMenuInit();
                loadSearchFragment();
            }

        } else {
            for (String param : dataParts) {
                String[] subParts = param.split(":");
                if ((subParts[0].equals("rest")) && (subParts.length > 1)) {
                    restApi = subParts[1];
                    bundle.putString("restApi", restApi);

                } else if ((subParts[0].equals("message")) && (subParts.length > 1)) {
                    restApiMessage = subParts[1];
                    bundle.putString("restApiMessage", restApiMessage);

                } else if ((subParts[0].equals("source")) && (subParts.length > 1))  {
                    restApiSource = subParts[1];
                    bundle.putString("restApiSource", restApiSource);
                }
            }

            if (restApi.equals("login") || restApi.equals("register")) {
                menuType = MENU_LOGIN;
                mainMenuInit();
                loadLoginFragment();

                //textViewExtra.setText("Error" );
            } else if (restApi.equals("rest:post-highscore")) {
                // TODO Figure out what to do lol
                online = false;
                loadResultsFragment();
            }
        }
    }


    private void mainMenuInit( ) {

        switch ( menuType ) {
            case MENU_LOGIN:
                menuLevelDown2Btn.setText("New User");
                menuLevelDown2Btn.setEnabled(true);
                menuLevelDown2Btn.getBackground().setAlpha(255);

                menuTestBtn.setText("Login");
                menuTestBtn.setEnabled(true);
                menuTestBtn.getBackground().setAlpha(255);

                menuLevelUpBtn.setText("Offline");
                menuLevelUpBtn.setEnabled(true);
                menuLevelUpBtn.getBackground().setAlpha(255);

                menuLeaderBtn.setText("High Score");
                menuLeaderBtn.setEnabled(false);
                menuLeaderBtn.getBackground().setAlpha(128);

                menuExitBtn.setText("Exit");
                menuExitBtn.setEnabled(true);
                menuExitBtn.getBackground().setAlpha(255);

                menuAboutBtn.setText("About");
                menuAboutBtn.setEnabled(false);
                menuAboutBtn.getBackground().setAlpha(128);
                break;

            case MENU_REGISTER:
                menuLevelDown2Btn.setText("Cancel");
                menuLevelDown2Btn.setEnabled(true);
                menuLevelDown2Btn.getBackground().setAlpha(255);

                menuTestBtn.setText("Register");
                menuTestBtn.setEnabled(true);
                menuTestBtn.getBackground().setAlpha(255);

                menuLevelUpBtn.setText("Offline");
                menuLevelUpBtn.setEnabled(true);
                menuLevelUpBtn.getBackground().setAlpha(255);

                menuLeaderBtn.setText("High Score");
                menuLeaderBtn.setEnabled(false);
                menuLeaderBtn.getBackground().setAlpha(128);

                menuExitBtn.setText("Exit");
                menuExitBtn.setEnabled(true);
                menuExitBtn.getBackground().setAlpha(255);

                menuAboutBtn.setText("About");
                menuAboutBtn.setEnabled(false);
                menuAboutBtn.getBackground().setAlpha(128);
                break;

            case MENU_SEARCH:
                menuLevelDown2Btn.setText("Level Down");
                menuLevelDown2Btn.setEnabled(false);
                menuLevelDown2Btn.getBackground().setAlpha(128);

                menuTestBtn.setText("Scanning");
                menuTestBtn.setEnabled(false);
                menuTestBtn.getBackground().setAlpha(128);

                menuLevelUpBtn.setText("Level Up");
                menuLevelUpBtn.setEnabled(false);
                menuLevelUpBtn.getBackground().setAlpha(128);


                menuLeaderBtn.setText("High Score");
                menuLeaderBtn.setEnabled(true);
                menuLeaderBtn.getBackground().setAlpha(255);

                menuExitBtn.setText("Exit");
                menuExitBtn.setEnabled(true);
                menuExitBtn.getBackground().setAlpha(255);

                menuAboutBtn.setText("About");
                menuAboutBtn.setEnabled(true);
                menuAboutBtn.getBackground().setAlpha(255);
                break;

            case MENU_LIST:
                menuLevelDown2Btn.setText("Level Down");
                menuLevelDown2Btn.setEnabled(false);
                menuLevelDown2Btn.getBackground().setAlpha(128);

                menuTestBtn.setText("Next Test");
                menuTestBtn.setEnabled(true);
                menuTestBtn.getBackground().setAlpha(255);

                menuLevelUpBtn.setText("Level Up");
                menuLevelUpBtn.setEnabled(false);
                menuLevelUpBtn.getBackground().setAlpha(128);


                menuLeaderBtn.setText("High Score");
                menuLeaderBtn.setEnabled(true);
                menuLeaderBtn.getBackground().setAlpha(255);

                menuExitBtn.setText("Exit");
                menuExitBtn.setEnabled(true);
                menuExitBtn.getBackground().setAlpha(255);

                menuAboutBtn.setText("About");
                menuAboutBtn.setEnabled(true);
                menuAboutBtn.getBackground().setAlpha(255);
                break;

            case MENU_TEST:
                menuLevelDown2Btn.setText("Level Down");
                menuLevelDown2Btn.setEnabled(false);
                menuLevelDown2Btn.getBackground().setAlpha(128);

                menuTestBtn.setText("Finish");
                menuTestBtn.setEnabled(false);
                menuTestBtn.getBackground().setAlpha(128);

                menuLevelUpBtn.setText("Level Up");
                menuLevelUpBtn.setEnabled(false);
                menuLevelUpBtn.getBackground().setAlpha(128);


                menuLeaderBtn.setText("High Score");
                menuLeaderBtn.setEnabled(true);
                menuLeaderBtn.getBackground().setAlpha(255);

                menuExitBtn.setText("Exit");
                menuExitBtn.setEnabled(true);
                menuExitBtn.getBackground().setAlpha(255);

                menuAboutBtn.setText("About");
                menuAboutBtn.setEnabled(true);
                menuAboutBtn.getBackground().setAlpha(255);
                break;

            case MENU_RESULTS:
            case MENU_LEADER:
            case MENU_ABOUT:
                // setGameParameters modifies the Level Up and Level Down visibility.
                menuLevelDown2Btn.setText("Level Down");

                menuTestBtn.setText("Play Again");
                menuTestBtn.setEnabled(true);
                menuTestBtn.getBackground().setAlpha(255);

                // setGameParameters modifies the Level Up and Level Down visibility.
                menuLevelUpBtn.setText("Level Up");


                menuLeaderBtn.setText("High Score");
                menuLeaderBtn.setEnabled(true);
                menuLeaderBtn.getBackground().setAlpha(255);

                menuExitBtn.setText("Exit");
                menuExitBtn.setEnabled(true);
                menuExitBtn.getBackground().setAlpha(255);

                menuAboutBtn.setText("About");
                menuAboutBtn.setEnabled(true);
                menuAboutBtn.getBackground().setAlpha(255);
                break;

             default:
                 break;

        }
        // setGameParameters modifies the Level Up and Level Down visibility.
        setGameParameters();
    }

    // This button doubles as the Offline button
    public void mainMenuLevelUpBtn (View view) {
        if (menuType == MENU_LOGIN || menuType == MENU_REGISTER) {
            // Offline
            online = false;
            bundle.putBoolean("online", online);
            menuType = MENU_SEARCH;
            mainMenuInit();
            loadSearchFragment();

        } else {
            if (gameLevel < constants.maxGameLevel()) {
                gameLevel++;
                setGameParameters();
            }
        }
    }

    // This button doubles as the Register New User button
    public void mainMenuLevelDownBtn (View view) {
        if (menuType == MENU_LOGIN) {
            menuType = MENU_REGISTER;
            restApiSource = "register";
            bundle.putString("restApiSource", restApiSource);
            bundle.putString("restApiMessage", null);
            loadLoginFragment();
        } else if (menuType == MENU_REGISTER) {
            restApiSource = "login";
            bundle.putString("restApiSource", restApiSource);
            menuType = MENU_LOGIN;
            loadLoginFragment();
        } else {
             if (gameLevel > constants.minGameLevel()) {
                gameLevel--;
                setGameParameters();
            }
        }
    }

    public void mainMenuTestBtn (View view) {
        FragmentManager manager;
        manager = getSupportFragmentManager();
        String param1;
        String param2;
        String param3;

        switch ( menuType ) {

            case MENU_LOGIN:
            case MENU_REGISTER:
                mainLoginLoad();
                if (menuType == MENU_LOGIN) {
                    restApiSource = "login";
                } else {
                    restApiSource = "register";
                }
                bundle.putString("restApiSource", restApiSource);


               if (textEmail.equals("") || textPassword.equals("")) {
                    restApiMessage = "Both Email and Password are Required";
                    bundle.putString("restApiMessage", restApiMessage);
                    loadLoginFragment();
               } else {
                   online = true;
                   param1 = "http://DouglasKirk.asuscomm.com/task_manager/v1/index.php/" + restApiSource;
                   param2 = "email=" + textEmail;
                   param2 += "&password=" + textPassword;
                   param2 += "&username=" + textUsername;
                   param2 += "&name=" + textName;
                   param2 += "&game=" + Integer.toString(gameType);
                   param2 += "&source=loadSearchFragment";
                   param3 = restApiKey;
                   new PostClass(this).execute(param1, param2, param3);
               }
               break;

            case MENU_SEARCH:
                menuType = MENU_LIST;
                mainMenuInit();
                ListFragment listFragment = new ListFragment();
                listFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, listFragment).commit();
                break;

            case MENU_LIST:
                menuType = MENU_TEST;
                mainMenuInit();
                TestFragment testFragment = new TestFragment();
                testFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, testFragment).commit();
                break;

            case MENU_TEST:
                menuType = MENU_RESULTS;
                mainMenuInit();
                // Lets see if we qualify
                boolean scored = false;
                if (processScore(powerList, powerSelected, gameLevel)) {
                    scored = true;
                    if (timeTotal < highScore[gameLevel - 1]) {
                        highScore[gameLevel - 1] = timeTotal;
                        bundle.putLongArray("highScore", highScore);
                        String parmName = "highScore" + Integer.toString(gameLevel);
                        editor = prefs.edit();
                        editor.putLong(parmName, highScore[gameLevel - 1]);
                        editor.commit();
                    }
                }
                if (scored && online) {
                    param1 = "http://DouglasKirk.asuscomm.com/task_manager/v1/index.php/score";
                    param2 = "game=";
                    if (powerDebug) {
                        param2 += Integer.toString(gameType + 100);
                    } else {
                        param2 += Integer.toString(gameType);
                    }
                    param2 += "&level=" + Integer.toString(gameLevel);
                    param2 += "&score1=" + Long.toString(timeList);
                    param2 += "&score2=" + Long.toString(timeTest);
                    param2 += "&score=" + Long.toString(timeTotal);
                    param2 += "&source=loadResultsFragment";
                    param3 = restApiKey;
                    new PostClass(this).execute(param1, param2, param3);

                } else if (!scored && online) {
                    param1 = "http://DouglasKirk.asuscomm.com/task_manager/v1/index.php/highscore";
                    param2 = "game=" + Integer.toString(gameType);
                    param2 += "&level=" + Integer.toString(gameLevel);
                    param2 += "&source=loadResultsFragment";
                    param3 = restApiKey;
                    new PostClass(this).execute(param1, param2, param3);

                } else {
                    loadResultsFragment();
                }
                break;

            case MENU_RESULTS:
            case MENU_LEADER:
            case MENU_ABOUT:
                menuType = MENU_SEARCH;
                mainMenuInit();

                if (manager.getBackStackEntryCount() > 0) {
                    FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
                    manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
                menuType = MENU_SEARCH;
                mainMenuInit();
                loadSearchFragment();
                break;
        }
    }

    public void mainMenuLeaderBtn (View view) {
        menuType = MENU_LEADER;
        mainMenuInit();
        initLeaderFragment();
    }

    public void mainMenuExitBtn (View view) {
        if (menuType == MENU_LOGIN) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                this.finishAndRemoveTask();
            } else {
                this.finishAffinity();
            }
        } else {
            menuType = MENU_LOGIN;
                restApiSource = "login";
                restApiMessage = null;
                bundle.putString("restApiSource", restApiSource);
                bundle.putString("restApiMessage", restApiMessage);
                loadLoginFragment();


            mainMenuInit();
            //getSupportFragmentManager().beginTransaction();
            LoginFragment loginFragment = new LoginFragment();
            loginFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, loginFragment).commit();
        }
    }

    public void mainMenuAboutBtn (View view) {
        menuType = MENU_ABOUT;
        mainMenuInit();
        AboutFragment aboutFragment = new AboutFragment();
        //aboutFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, aboutFragment).commit();
    }


    private void mainLoginLoad() {
        online = true;

        LoginFragment fragment = (LoginFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        View loginFragment = fragment.getView();

        EditText editTextEmail = (EditText)loginFragment.findViewById(R.id.editTextEmail);
        EditText editTextPassword = (EditText)loginFragment.findViewById(R.id.editTextPassword);
        EditText editTextPowerup = (EditText)loginFragment.findViewById(R.id.editTextPowerup);
        EditText editTextUsername = (EditText)loginFragment.findViewById(R.id.editTextUsername);
        EditText editTextName = (EditText)loginFragment.findViewById(R.id.editTextName);

        textEmail = editTextEmail.getText().toString().trim();
        textPassword = editTextPassword.getText().toString().trim();
        textPowerup = editTextPowerup.getText().toString().trim();
        textUsername = editTextUsername.getText().toString().trim();
        textName = editTextName.getText().toString().trim();

        bundle.putBoolean("online", online);
        bundle.putString("textEmail", textEmail);
        bundle.putString("textPassword", textPassword);
        bundle.putString("textPowerup", textPowerup);
        bundle.putString("textUsername", textUsername);
        bundle.putString("textName", textName);

        powerDebug = textPowerup.equals(constants.SuperPowers());
        bundle.putBoolean("powerDebug", powerDebug);

        //setting preferences
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("textEmail", textEmail);
        editor.putString("textPassword", textPassword);
        editor.putString("textPowerup", textPowerup);
        editor.putString("textUsername", textUsername);
        editor.putString("textName", textName);
        editor.commit();

        restApiMessage = null;
        restApiKey = "";
    }



    private class PostClass extends AsyncTask<String, Void, Void> {

        private final Context context;

        public PostClass(Context c){
            this.context = c;
//            this.error = status;
//            this.type = t;
        }

        protected void onPreExecute(){
            progress= new ProgressDialog(this.context);
            progress.setMessage("Loading");
            progress.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {

                //final TextView textViewResults = (TextView) findViewById(R.id.textViewResults);
                URL url = new URL( params[0] ); //"http://DouglasKirk.asuscomm.com/task_manager/v1/test1.php");

                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                String urlParameters = params[1]; // "name=DouglasKirk&email=DouglasKirk@outlook.com";
                connection.setRequestMethod("POST");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                connection.setRequestProperty("Authorization", params[2]);
                connection.setDoOutput(true);
                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                dStream.writeBytes(urlParameters);
                dStream.flush();
                dStream.close();
                int responseCode = connection.getResponseCode();

                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters);
                System.out.println("Response Code : " + responseCode);

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters);
                output.append(System.getProperty("line.separator")  + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator")  + "Type " + "POST");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                final StringBuilder responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while((line = br.readLine()) != null ) {
                    responseOutput.append(line);
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());

                MainActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        setResponse(responseOutput.toString());
                        progress.dismiss();
                     }
                });

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute() {
            progress.dismiss();
        }

    }


    @Override
    public void onLoginFragmentInteraction(Uri uri) {
    }

    @Override
    public void onHighScoreListener(Uri uri) {
    }

    @Override
    public void onAboutInteractionListener(Uri uri) {
    }

    @Override
    public void onSearchInteractionListener(Uri uri) {
    }

    @Override
    public void onListInteractionListener(Uri uri) {
    }

    @Override
    public void onPowerTestInteractionListener(Uri uri) {
    }

    @Override
    public void onGameResultsInteractionListener(Uri uri) {
    }

    @Override
    public void onBackPressed() {
    }
}
