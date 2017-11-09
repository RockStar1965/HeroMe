package com.asuscomm.douglaskirk.herome.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asuscomm.douglaskirk.herome.Constants;
import com.asuscomm.douglaskirk.herome.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LeaderFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LeaderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LeaderFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private int levelIndex = 0;
    private int titleBarIndex = 0;
    private TextView textViewLeaderTitle;
    private TextView textViewLeaderLeft;
    private TextView textViewLeaderRight;
    private boolean online;
    private long[] highScore;
    String[][] titleBar = new String[20][3];
    private Constants constants = new Constants();
    private ScoreInteractionListener mListener;
    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
      /* do what you need to do */

            if (online) {
                textViewLeaderTitle.setText(titleBar[levelIndex][0]);
                textViewLeaderLeft.setText(titleBar[levelIndex][1]);
                textViewLeaderRight.setText(titleBar[levelIndex][2]);


            } else {
                textViewLeaderTitle.setText("Level " + Integer.toString(levelIndex + 1));
                textViewLeaderLeft.setText("Local");
                textViewLeaderRight.setText(Long.toString(highScore[levelIndex]));

            }
            if (++levelIndex >= titleBarIndex) {
                levelIndex = 0;
            }
            if (handler != null) {
                handler.postDelayed(this, 1600);
            }
            }
        };

    public LeaderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LeaderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LeaderFragment newInstance(String param1, String param2) {
        LeaderFragment fragment = new LeaderFragment();
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

        View view = inflater.inflate(R.layout.fragment_leader, container, false);

        online = getArguments().getBoolean("online");
        highScore = getArguments().getLongArray("highScore");
        String[] textHighScore = getArguments().getStringArray("textHighScore");
        String[] textUserHighScore = getArguments().getStringArray("textUserHighScore");

        textViewLeaderTitle = (TextView)view.findViewById(R.id.textViewLeaderTitle);
        textViewLeaderLeft = (TextView)view.findViewById(R.id.textViewLeaderLeft);
        textViewLeaderRight = (TextView)view.findViewById(R.id.textViewLeaderRight);

        final float densityValue = getContext().getResources().getDisplayMetrics().density;
        final float widthPixelsValue = getContext().getResources().getDisplayMetrics().widthPixels;

        final float newTextSizeSmall  =  (float)( widthPixelsValue / densityValue / 30 );
        final float newTextSizeMedium  =  (float)( widthPixelsValue / densityValue / 20 );
        final float newTextSizeBig  =  (float)( widthPixelsValue / densityValue / 15 );

        textViewLeaderTitle.setTextSize(newTextSizeBig);
        textViewLeaderLeft.setTextSize(newTextSizeMedium);
        textViewLeaderRight.setTextSize(newTextSizeMedium);

        final int newLeftWidth  = (int) ( this.getResources().getDisplayMetrics().widthPixels * 2 / 3 );
        final int newRightWidth  = (int) ( this.getResources().getDisplayMetrics().widthPixels * 1 / 3 );

        ConstraintLayout titleLayout = (ConstraintLayout) view.findViewById(R.id.leaderConstraintLayout);
        ConstraintSet newTitleLayout = new ConstraintSet();
        newTitleLayout.clone(titleLayout);
        newTitleLayout.constrainWidth(R.id.textViewLeaderLeft, newLeftWidth);
        newTitleLayout.constrainWidth(R.id.textViewLeaderRight, newRightWidth);
        newTitleLayout.applyTo(titleLayout);

        titleBarIndex = 0;
        if (online) {
            for (int i = 0; i < constants.maxGameLevel(); i++) {

                if (!(textHighScore[i].equals("System 999999"))) {
                    String[] levelText = textHighScore[i].split(";");
                    String title = "High Scores For Level " + Integer.toString(i + 1);
                    String left = "";
                    String right = "";
                    for (String line : levelText) {
                        String[] param = line.split(" ");
                        left += param[0] + "\n";
                        right += param[1] + "\n";
                    }
                    titleBar[titleBarIndex][0] = title;
                    titleBar[titleBarIndex][1] = left;
                    titleBar[titleBarIndex][2] = right;
                    titleBarIndex++;
                }

                if (!(textUserHighScore[i].equals("System 999999"))) {
                    String[] levelText = textUserHighScore[i].split(";");
                    String title = "Your Scores For Level " + Integer.toString(i + 1);

                    String left = "";
                    String right = "";
                    for (String line : levelText) {
                        String[] param = line.split(" ");
                        left += param[0] + "\n";
                        right += param[1] + "\n";
                    }
                    titleBar[titleBarIndex][0] = title;
                    titleBar[titleBarIndex][1] = left;
                    titleBar[titleBarIndex][2] = right;
                    titleBarIndex++;
                }
            }
        } else {
            titleBarIndex = 10;
        }


        levelIndex = 0;
        handler.postDelayed(runnable, 250);

        // Inflate the layout for this fragment
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onHighScoreListener(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ScoreInteractionListener) {
            mListener = (ScoreInteractionListener) context;
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
    public interface ScoreInteractionListener {
        // TODO: Update argument type and name
        void onHighScoreListener(Uri uri);
    }
}
