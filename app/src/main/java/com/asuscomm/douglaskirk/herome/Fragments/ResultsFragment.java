package com.asuscomm.douglaskirk.herome.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.asuscomm.douglaskirk.herome.Constants;
import com.asuscomm.douglaskirk.herome.Heroes;
import com.asuscomm.douglaskirk.herome.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ResultsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ResultsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultsFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final Constants constants = new Constants();
    private int imageArraySize = constants.numberRC() * constants.numberRC();

    private static final String[] summaryText = {
            "Show Time ",
            "Test Time ",
            "Your Score ",
            "Your Rank ",
            "High Score "

    };

    private static final String[] commentText = {
            "100% You're a RockStar!",
            "Side Kick!  ",
            "Wanna Be!   ",
            "Lucky!      ",
            "Hero Not!   "
    };

    private int scrollIndexTop = 0;
    private int scrollIndexBottom = 0;
    private int scrollIndex = 0;
    private int powerCorrect = 0;


    private static TextView textViewResults;
    private static TextView textViewHighScore;


    private TextView textViewResultsTitle;
    private TextView textViewResultsInfo;
    private String[] textViewText = new String[6];
    private String[] textViewInfoText;

    TextView textViewPowerList[] = new TextView[constants.maxGameLevel()];
    TextView textViewPowerSelected[] = new TextView[constants.maxGameLevel()];
    ImageButton imageButtonPowerList[] = new ImageButton[constants.maxGameLevel()];
    ImageButton imageButtonPowerCorrect[] = new ImageButton[constants.maxGameLevel()];
    ImageButton imageButtonPowerSelected[] = new ImageButton[constants.maxGameLevel()];

    TableRow tableResultsRow[] = new TableRow[constants.maxGameLevel()];
    private TableLayout resultsTableLayout;

    private static final Heroes heroIcons = new Heroes();
    private ResultsInteractionListener mListener;
    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
      /* do what you need to do */
            if (scrollIndex == 0) {
                scrollIndex = 1;
                textViewResultsTitle.setText(textViewText[scrollIndexTop]);
                if (++scrollIndexTop >= textViewText.length)
                    scrollIndexTop = 0;
            } else {
                scrollIndex = 0;
                textViewResultsInfo.setText(textViewInfoText[scrollIndexBottom]);
                if (++scrollIndexBottom >= textViewInfoText.length)
                    scrollIndexBottom = 0;
            }
           if (handler != null) {
            handler.postDelayed(this, 1000);
        }
        }};

    public ResultsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     *
     *
     *
     *
     * @return A new instance of fragment ResultsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ResultsFragment newInstance(String param1, String param2) {
        ResultsFragment fragment = new ResultsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_results, container, false);
        boolean online = getArguments().getBoolean("online");
        int gameLevel = getArguments().getInt("gameLevel");
        int[] powerList = getArguments().getIntArray("powerList");
        int[] powerSelected = getArguments().getIntArray("powerSelected");
        String powerRank = getArguments().getString("powerRank");
        String[] textHighScore = getArguments().getStringArray("textHighScore");
        long timeList = getArguments().getLong("timeList");
        long timeTest = getArguments().getLong("timeTest");
        long timeTotal = getArguments().getLong("timeTotal");
        long[] highScore = getArguments().getLongArray("highScore");

        textViewResultsTitle = (TextView) view.findViewById(R.id.textViewResultsTitle);
        textViewResultsInfo = (TextView) view.findViewById(R.id.textViewResultsInfo);
        resultsTableLayout = (TableLayout)view.findViewById(R.id.resultsTableLayout);

        final float densityValue = getContext().getResources().getDisplayMetrics().density;
        final float widthPixelsValue = getContext().getResources().getDisplayMetrics().widthPixels;

        final float newTextSizeSmall  =  (float)( widthPixelsValue / densityValue / 30 );
        final float newTextSizeMedium  =  (float)( widthPixelsValue / densityValue / 20 );
        final float newTextSizeBig  =  (float)( widthPixelsValue / densityValue / 15 );

        textViewResultsTitle.setTextSize(newTextSizeBig);
        textViewResultsInfo.setTextSize(newTextSizeMedium);

        TableRow.LayoutParams tableLayout = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        TableRow.LayoutParams buttonLayout = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        TableRow.LayoutParams buttonSmallLayout = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);

        //final float scale = getContext().getResources().getDisplayMetrics().density;
        //int pixels = (int) (50 * scale + 0.5f);
        final int newSize  = getContext().getResources().getDisplayMetrics().widthPixels / 6;
        buttonLayout.height = newSize;//pixels;
        buttonLayout.width = newSize;//pixels;
        buttonLayout.leftMargin = 10;
        buttonLayout.rightMargin = 10;

        buttonSmallLayout.height = newSize/3;//pixels;
        buttonSmallLayout.width = newSize/3;//pixels;
        buttonSmallLayout.leftMargin = 10;
        buttonSmallLayout.rightMargin = 10;

        buttonSmallLayout.gravity = Gravity.CENTER_VERTICAL;

        int numberRows = gameLevel;
        if (gameLevel >= 5) {
            numberRows = gameLevel / 2;
        }

        int imageIndex = 0;
        for (int i = 0; i < 5; i++) {
            tableResultsRow[i] = new TableRow(getActivity());

            if (imageIndex < gameLevel) {
                imageButtonPowerList[imageIndex] = new ImageButton(getActivity());
                imageButtonPowerList[imageIndex].setLayoutParams(buttonLayout);//new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                imageButtonPowerList[imageIndex].setPadding(0, 0, 0, 0);
                imageButtonPowerList[imageIndex].setBackgroundResource(R.drawable.itemselectedholder);
                imageButtonPowerList[imageIndex].getBackground().setAlpha(255);

                imageButtonPowerCorrect[imageIndex] = new ImageButton(getActivity());
                imageButtonPowerCorrect[imageIndex].setLayoutParams(buttonSmallLayout);//new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                imageButtonPowerCorrect[imageIndex].setPadding(0, 0, 0, 0);
                imageButtonPowerCorrect[imageIndex].setBackgroundResource(R.drawable.itemselectedholder);
                imageButtonPowerCorrect[imageIndex].getBackground().setAlpha(255);

                imageButtonPowerSelected[imageIndex] = new ImageButton(getActivity());
                imageButtonPowerSelected[imageIndex].setLayoutParams(buttonLayout);//new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                imageButtonPowerSelected[imageIndex].setPadding(0, 0, 0, 0);
                imageButtonPowerSelected[imageIndex].setBackgroundResource(R.drawable.itemselectedholder);
                imageButtonPowerSelected[imageIndex].getBackground().setAlpha(255);

                tableResultsRow[i].addView(imageButtonPowerList[imageIndex]);
                tableResultsRow[i].addView(imageButtonPowerCorrect[imageIndex]);
                tableResultsRow[i].addView(imageButtonPowerSelected[imageIndex]);
                imageIndex++;
            }
            if ((gameLevel > 5)&&(imageIndex < gameLevel)) {
                imageButtonPowerList[imageIndex] = new ImageButton(getActivity());
                imageButtonPowerList[imageIndex].setLayoutParams(buttonLayout);//new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                imageButtonPowerList[imageIndex].setPadding(0, 0, 0, 0);
                imageButtonPowerList[imageIndex].setBackgroundResource(R.drawable.itemselectedholder);
                imageButtonPowerList[imageIndex].getBackground().setAlpha(255);

                imageButtonPowerCorrect[imageIndex] = new ImageButton(getActivity());
                imageButtonPowerCorrect[imageIndex].setLayoutParams(buttonSmallLayout);//new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                imageButtonPowerCorrect[imageIndex].setPadding(0, 0, 0, 0);
                imageButtonPowerCorrect[imageIndex].setBackgroundResource(R.drawable.itemselectedholder);
                imageButtonPowerCorrect[imageIndex].getBackground().setAlpha(255);

                imageButtonPowerSelected[imageIndex] = new ImageButton(getActivity());
                imageButtonPowerSelected[imageIndex].setLayoutParams(buttonLayout);//new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                imageButtonPowerSelected[imageIndex].setPadding(0, 0, 0, 0);
                imageButtonPowerSelected[imageIndex].setBackgroundResource(R.drawable.itemselectedholder);
                imageButtonPowerSelected[imageIndex].getBackground().setAlpha(255);

                tableResultsRow[i].addView(imageButtonPowerList[imageIndex]);
                tableResultsRow[i].addView(imageButtonPowerCorrect[imageIndex]);
                tableResultsRow[i].addView(imageButtonPowerSelected[imageIndex]);
                imageIndex++;
            }

            tableResultsRow[i].setLayoutParams(tableLayout);
            tableResultsRow[i].setEnabled(true);
            this.resultsTableLayout.addView(tableResultsRow[i], new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
        }



        for (int i = 0; i < gameLevel; i++) {
            imageButtonPowerList[i].setBackgroundResource(R.drawable.itemselectedholder);
            imageButtonPowerCorrect[i].setBackgroundResource(R.drawable.itemselectedholder);
            imageButtonPowerSelected[i].setBackgroundResource(R.drawable.itemselectedholder);
        }

        powerCorrect = 0;
        for (int i = 0; i < constants.maxGameLevel(); i++) {
            if (gameLevel > i) {
                imageButtonPowerList[i].setBackgroundResource(heroIcons.On(powerList[i]));
                imageButtonPowerSelected[i].setBackgroundResource(heroIcons.On(powerSelected[i]));
                if (powerList[i] == powerSelected[i]) {
                    imageButtonPowerCorrect[i].setBackgroundResource(R.drawable.checkmark_icon);
                    powerCorrect++;
                } else {
                    imageButtonPowerCorrect[i].setBackgroundResource(R.drawable.error_icon);
                }
            }
        }

        int errors = gameLevel - powerCorrect;
        if (errors >= 4) {
            errors = 4;
        }





        textViewText[0] = "Level " + Integer.toString(gameLevel);
        if (errors == 0) {
            if (online) {
                textViewText[1] = "Your Rank is " + powerRank;
            } else {
                textViewText[1] = "Your Rank is Unavailable";
            }
        } else {
            textViewText[1] = "You Did Not Rank";
        }
        textViewText[2] = "Show Time " + Long.toString(timeList) + " mS";
        textViewText[3] = "Test Time " + Long.toString(timeTest) + " mS";
        textViewText[4] = "Total Time  " + Long.toString(timeTotal) + " mS";
        if (errors > 0) {
            textViewText[5] = commentText[errors] + Integer.toString(powerCorrect) + " of " + Integer.toString(gameLevel);
        } else {
            textViewText[5] = commentText[errors];
        }

        textViewInfoText = new String[]{"No Recorded High Score"};
        if (online) {
            if (!(textHighScore[gameLevel - 1].equals(constants.InvalidScore()))) {
                textViewInfoText = textHighScore[gameLevel - 1].split(";");
            }
        } else {
            String text = "Device High Score " + Long.toString(highScore[gameLevel - 1]);
            textViewInfoText = text.split(";");
        }


        scrollIndexTop = 0;
        scrollIndexBottom = 0;
        scrollIndex = 0;

        handler.postDelayed(runnable, 250);

        return view;
    }

    @Override
    public void onClick(View v) {
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onGameResultsInteractionListener(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ResultsInteractionListener) {
            mListener = (ResultsInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ButtonInteractiveListener");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        handler = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface ResultsInteractionListener {
        // TODO: Update argument type and name
        void onGameResultsInteractionListener(Uri uri);
    }
}
