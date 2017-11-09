package com.asuscomm.douglaskirk.herome.Fragments;

import android.content.Context;
import android.graphics.Color;
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

import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ListInteractionListener mListener;
    private Heroes heroIcons = new Heroes();
    private Constants constants = new Constants();
    private int imageArraySize = constants.numberRC() * constants.numberRC();

    TextView textViewListInfo;
    private int[] powerList;

    private long timeList;
    private long timeStart;
    
    TableLayout listTableLayout;

    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
      /* do what you need to do */
        long timeEnd = System.currentTimeMillis();
        timeList = timeEnd - timeStart;
        long displayTime = ( timeList / 25 ) * 25;
        textViewListInfo.setText("Your Score is " + Long.toString(displayTime)+" mS");
        if (handler != null) {
            handler.postDelayed(this, 250);
        }
        }
    };

    public ListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListFragment newInstance(String param1, String param2) {
        ListFragment fragment = new ListFragment();
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

    private int[] getPowers(int gameLevel, int[] gameList) {
        int[] powerList = new int[gameLevel];
        Random randomNumber = new Random();
        /* Initialize the list of game icons. */
        for (int i = 0; i < gameLevel; i++) {
            boolean found  = false;

            while (!found) {
                int randomIcon = randomNumber.nextInt(imageArraySize);
                boolean duplicate = false;
                for (int j = 0; j < i; j++) {
                    if (powerList[j] == gameList[randomIcon]) {
                        duplicate = true;
                    }
                }
                if (!duplicate) {
                    powerList[i] = gameList[randomIcon];
                    found = true;
                }
            }
        }
        return (powerList);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list, container, false);

        int gameLevel = getArguments().getInt("gameLevel");
        int[] gameList = getArguments().getIntArray("gameList");

        listTableLayout = (TableLayout)view.findViewById(R.id.listTableLayout);
        TextView textViewListTitle = (TextView) view.findViewById(R.id.textViewListTitle);
        textViewListInfo = (TextView) view.findViewById(R.id.textViewListInfo);

        final float densityValue = getContext().getResources().getDisplayMetrics().density;
        final float widthPixelsValue = getContext().getResources().getDisplayMetrics().widthPixels;

        final float newTextSizeSmall  =  (float)( widthPixelsValue / densityValue / 30 );
        final float newTextSizeMedium  =  (float)( widthPixelsValue / densityValue / 20 );
        final float newTextSizeBig  =  (float)( widthPixelsValue / densityValue / 15 );

        textViewListTitle.setTextSize(newTextSizeBig);
        textViewListInfo.setTextSize(newTextSizeMedium);

        TextView textListPowerList[] = new TextView[constants.maxGameLevel()];
        ImageButton imageButtonPowerList[] = new ImageButton[constants.maxGameLevel()];

        TableRow tableListRow[] = new TableRow[constants.maxGameLevel()];

        TableRow.LayoutParams tableLayout = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        TableRow.LayoutParams imageLayout = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        TableRow.LayoutParams textLayout = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

        powerList = getPowers(gameLevel, gameList);

        final float scale = getContext().getResources().getDisplayMetrics().density;
        //int newSize = (int) (75 * scale + 0.5f);
        final int newSize  = getContext().getResources().getDisplayMetrics().widthPixels / 6;
        imageLayout.height = newSize;//pixels;
        imageLayout.width = newSize;//pixels;

        textLayout.gravity = Gravity.CENTER_VERTICAL;
        textLayout.width = newSize * 2;
        textLayout.leftMargin = 10;

        tableLayout.leftMargin = 10;
        tableLayout.rightMargin = 10;

        int imageIndex = 0;

        for (int i = 0; i < 5; i++) {
            tableListRow[i] = new TableRow(getActivity());
            if (imageIndex < gameLevel) {
                imageButtonPowerList[imageIndex] = new ImageButton(getActivity());
                imageButtonPowerList[imageIndex].setLayoutParams(imageLayout);
                imageButtonPowerList[imageIndex].setPadding(0, 0, 0, 0);
                imageButtonPowerList[imageIndex].setBackgroundResource(heroIcons.On(powerList[imageIndex]));
                imageButtonPowerList[imageIndex].getBackground().setAlpha(255);

                textListPowerList[imageIndex] = new TextView(getActivity());
                textListPowerList[imageIndex].setLayoutParams(textLayout);
                textListPowerList[imageIndex].setPadding(0, 0, 0, 0);
                textListPowerList[imageIndex].setTextColor(Color.parseColor("#FAC740"));
                textListPowerList[imageIndex].setText(heroIcons.Text(powerList[imageIndex]));
                textListPowerList[imageIndex].setTextSize(newTextSizeMedium);
                tableListRow[i].addView(imageButtonPowerList[imageIndex]);
                tableListRow[i].addView(textListPowerList[imageIndex]);
                imageIndex++;
            }
            if ((gameLevel > 5)&&(imageIndex < gameLevel)) {
                imageButtonPowerList[imageIndex] = new ImageButton(getActivity());
                imageButtonPowerList[imageIndex].setLayoutParams(imageLayout);
                imageButtonPowerList[imageIndex].setPadding(0, 0, 0, 0);
                imageButtonPowerList[imageIndex].setBackgroundResource(heroIcons.On(powerList[imageIndex]));
                imageButtonPowerList[imageIndex].getBackground().setAlpha(255);

                textListPowerList[imageIndex] = new TextView(getActivity());
                textListPowerList[imageIndex].setLayoutParams(textLayout);
                textListPowerList[imageIndex].setPadding(0, 0, 0, 0);
                textListPowerList[imageIndex].setTextColor(Color.parseColor("#FAC740"));
                textListPowerList[imageIndex].setText(heroIcons.Text(powerList[imageIndex]));
                textListPowerList[imageIndex].setTextSize(newTextSizeMedium);
                tableListRow[i].addView(imageButtonPowerList[imageIndex]);
                tableListRow[i].addView(textListPowerList[imageIndex]);
                imageIndex++;
            }
            tableListRow[i].setLayoutParams(tableLayout);
            tableListRow[i].setEnabled(true);


            this.listTableLayout.addView(tableListRow[i], new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
        }

        // Inflate the layout for this fragment
        timeStart = System.currentTimeMillis();
        handler.postDelayed(runnable, 10);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onListInteractionListener(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ListInteractionListener) {
            mListener = (ListInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ButtonInteractiveListener");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        handler = null;
        mListener.onList(powerList, timeList);
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
    public interface ListInteractionListener {
        // TODO: Update argument type and name
        void onListInteractionListener(Uri uri);
        void onList(int[] powerList, long timeList);

    }
}
