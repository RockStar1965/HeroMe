package com.asuscomm.douglaskirk.herome.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.os.Handler;
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
 * {@link SearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TestFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TestInteractionListener mListener;

    private static final Constants constants = new Constants();
    private int imageArraySize = constants.numberRC() * constants.numberRC();


    private TextView textViewTestTitle;
    private TableLayout testTableLayout;
    ImageButton imageButton[] = new ImageButton[constants.numberRC()*constants.numberRC()+1];


    private long timeStart;
    private long timeList;
    private long timeTest;
    private Heroes heroIcons = new Heroes();

    private int gameLevel;
    private int[] gameList;
    private int[] powerList;

    private int powerSelected[] = new int[constants.maxGameLevel()];

    private int iconState[] = new int[imageArraySize];

    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
      /* do what you need to do */
            long timeEnd = System.currentTimeMillis();
            long displayTime = timeList + timeEnd - timeStart;
            displayTime /= 25;
            displayTime *= 25;
            textViewTestTitle.setText("Your Score is " + Long.toString(displayTime) + " mS");

            if (handler != null) {
                handler.postDelayed(this, 150);
            }
        }

    };

  public TestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PowerSearchResultsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TestFragment newInstance(String param1, String param2) {
        TestFragment fragment = new TestFragment();
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

    private boolean isPowerList(int icon, int[] powerList) {

        boolean found = false;
        for (int i = 0; i < powerList.length; i++) {
            if (powerList[i] == icon) {
                found = true;
            }
        }
        return found;

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        
        boolean powerDebug = getArguments().getBoolean("powerDebug");
        gameLevel = getArguments().getInt("gameLevel");
        gameList = getArguments().getIntArray("gameList");
        powerList = getArguments().getIntArray("powerList");
        timeList = getArguments().getLong("timeList");

        textViewTestTitle = (TextView) view.findViewById(R.id.textViewTestTitle);
        testTableLayout = (TableLayout)view.findViewById(R.id.testTableLayout);

        final float densityValue = getContext().getResources().getDisplayMetrics().density;
        final float widthPixelsValue = getContext().getResources().getDisplayMetrics().widthPixels;

        final float newTextSizeSmall  =  (float)( widthPixelsValue / densityValue / 30 );
        final float newTextSizeMedium  =  (float)( widthPixelsValue / densityValue / 20 );
        final float newTextSizeBig  =  (float)( widthPixelsValue / densityValue / 15 );

        textViewTestTitle.setTextSize(newTextSizeBig);

        for (int i = 0; i < imageArraySize ; i++) {
            iconState[i] = 0;
        }

        //final float scale = getContext().getResources().getDisplayMetrics().density;
        //int pixels = (int) (50 * scale + 0.5f);
        final int newSize  = getContext().getResources().getDisplayMetrics().widthPixels / constants.numberRC();

        TableRow tableSearchRow[] = new TableRow[constants.numberRC()];
        TableRow.LayoutParams tableLayout = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        TableRow.LayoutParams buttonLayout = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);

        int imageIndex = 0;
        for (int i = 0; i < constants.numberRC(); i++) {
            tableSearchRow[i] = new TableRow(getActivity());
            for (int i1 = 0; i1 < constants.numberRC(); i1++) {
                imageButton[imageIndex] = new ImageButton(getActivity());
                buttonLayout.height = newSize;//pixels;
                buttonLayout.width = newSize;//pixels;

                imageButton[imageIndex].setLayoutParams(buttonLayout);//new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                imageButton[imageIndex].setPadding(0, 0, 0, 0);
                imageButton[imageIndex].setBackgroundResource(heroIcons.On(gameList[imageIndex]));
                if ((powerDebug) && isPowerList(gameList[imageIndex], powerList)) {
                    imageButton[imageIndex].setBackgroundResource(heroIcons.Off(gameList[imageIndex]));
                    imageButton[imageIndex].getBackground().setAlpha(128);
                } else {
                    imageButton[imageIndex].getBackground().setAlpha(64);
                }
                imageButton[imageIndex].setOnClickListener(this);
                tableSearchRow[i].addView(imageButton[imageIndex]);
                imageIndex++;
            }
            tableSearchRow[i].setLayoutParams(tableLayout);
            tableSearchRow[i].setEnabled(true);
            this.testTableLayout.addView(tableSearchRow[i], new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
        }
        // Inflate the layout for this fragment
        timeStart = System.currentTimeMillis();
        handler.postDelayed(runnable, 10);
        return view;
    }

    @Override
    public void onClick(View v) {
        timeTest = System.currentTimeMillis() - timeStart;

        ImageButton btn = (ImageButton)v;
        for (int i = 0; i < imageArraySize; i++) {
            if ( btn == imageButton[i] ) {

                if (iconState[i] == 0) {
                    iconState[i] = 1;
                    imageButton[i].getBackground().setAlpha(255);
                    imageButton[i].setBackgroundResource(heroIcons.On(gameList[i]));
                } else {
                    iconState[i] = 0;
                    imageButton[i].getBackground().setAlpha(64);
                    imageButton[i].setBackgroundResource(heroIcons.On(gameList[i]));
                }
            }
        }
        int count = 0;
        for (int i = 0; i < imageArraySize; i++) {
            if (iconState[i] == 1) {
                if ( count < gameLevel) {
                    powerSelected[count++] = gameList[i];
                } else {
                    count++;
                }
            }
        }

        if ( count == gameLevel) {
            mListener.onTestButtonClick(powerSelected, timeTest, true);
        } else {
            mListener.onTestButtonClick(powerSelected, timeTest, false);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onPowerTestInteractionListener(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof TestInteractionListener) {
            mListener = (TestInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ButtonInteractiveListener");
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        handler = null;
        for (int i = 0; i < imageArraySize; i++) {
            imageButton[i].getBackground().setAlpha(255);
        }
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
    public interface TestInteractionListener {
        // TODO: Update argument type and name
        void onPowerTestInteractionListener(Uri uri);
        void onTestButtonClick(int[] powerSelected, long timeTest, boolean testButtonEnable);
    }
}
