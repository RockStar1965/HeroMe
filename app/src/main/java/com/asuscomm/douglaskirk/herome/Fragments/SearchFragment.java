package com.asuscomm.douglaskirk.herome.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
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
 * {@link SearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private SearchInteractionListener mListener;

    private static final Constants constants = new Constants();
    private int imageArraySize = constants.numberRC() * constants.numberRC();


    private TextView textViewSearchTitle;
    private TableLayout searchTableLayout;


    private Heroes heroIcons = new Heroes();
    private int gameList[] = new int[imageArraySize];

    private int iconIndex = 0;

    ImageButton imageButton[] = new ImageButton[imageArraySize];
    TableRow tableSearchRow[] = new TableRow[constants.numberRC()];
    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
      /* do what you need to do */
            //foobar();
            int timeValue = 2000 / imageArraySize;

            if (iconIndex < imageArraySize) {
                imageButton[iconIndex].setBackgroundResource(heroIcons.On(gameList[iconIndex]));
                imageButton[iconIndex].getBackground().setAlpha(255);
                textViewSearchTitle.setText(heroIcons.Text(iconIndex));
                if (++iconIndex == imageArraySize) {
                    textViewSearchTitle.setText("Genetic Scanning Complete");
                    mListener.onSearch(gameList);
                }
                if (handler != null) {
                    handler.postDelayed(this, timeValue);
                }
            }
        }};

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        int gameLevel = getArguments().getInt("gameLevel");


        textViewSearchTitle = (TextView) view.findViewById(R.id.textViewSearchTitle);

        searchTableLayout = (TableLayout)view.findViewById(R.id.searchTableLayout);
        //editText = (EditText)view.findViewById(R.id.editText);

        final float densityValue = getContext().getResources().getDisplayMetrics().density;
        final float widthPixelsValue = getContext().getResources().getDisplayMetrics().widthPixels;

        final float newTextSizeSmall  =  (float)( widthPixelsValue / densityValue / 30 );
        final float newTextSizeMedium  =  (float)( widthPixelsValue / densityValue / 20 );
        final float newTextSizeBig  =  (float)( widthPixelsValue / densityValue / 15 );

        textViewSearchTitle.setTextSize(newTextSizeBig);

        Random randomNumber = new Random();

        //final float scale = getContext().getResources().getDisplayMetrics().density;
        //int pixels = (int) (50 * scale + 0.5f);
        final int newSize  = getContext().getResources().getDisplayMetrics().widthPixels / constants.numberRC();

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
                imageButton[imageIndex].setBackgroundResource(R.drawable.itemselectedholder);
                imageButton[imageIndex].getBackground().setAlpha(255);
                imageButton[imageIndex].setOnClickListener(this);
                tableSearchRow[i].addView(imageButton[imageIndex]);
                imageIndex++;
            }
            tableSearchRow[i].setLayoutParams(tableLayout);
            tableSearchRow[i].setEnabled(true);
            this.searchTableLayout.addView(tableSearchRow[i], new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
        }

        /* Initialize the list of game icons. */
        for (int i = 0; i < imageArraySize; i++) {
            boolean found  = false;

            while (!found) {
                int randomIcon = randomNumber.nextInt(constants.numberIcons());
                boolean duplicate = false;
                for (int j = 0; j < i; j++) {
                    if (gameList[j] == randomIcon) {
                        duplicate = true;
                    }
                }
                if (!duplicate) {
                    gameList[i] = randomIcon;
                    found = true;
                }
            }
        }

        iconIndex = 0;
        handler.postDelayed(runnable, 10);
        return view;
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onSearchInteractionListener(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof SearchInteractionListener) {
            mListener = (SearchInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ButtonInteractiveListener");
        }
   }
    @Override
    public void onClick(View v) {
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
    public interface SearchInteractionListener {
        // TODO: Update argument type and name
        void onSearchInteractionListener(Uri uri);
        void onSearch( int[] gameList);
    }
}

