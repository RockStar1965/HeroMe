package com.asuscomm.douglaskirk.herome.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.asuscomm.douglaskirk.herome.Constants;
import com.asuscomm.douglaskirk.herome.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AboutFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AboutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AboutFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final Constants constants = new Constants();
    private int imageArraySize = constants.numberRC() * constants.numberRC();

    private AboutInteractionListener mListener;
    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
      /* do what you need to do */
        if (handler != null) {
            handler.postDelayed(this, 1000);
        }
    }};

    public AboutFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AboutFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AboutFragment newInstance(String param1, String param2) {
        AboutFragment fragment = new AboutFragment();
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
        View view = inflater.inflate(R.layout.fragment_about, container, false);



        TextView textViewAboutTitle = (TextView) view.findViewById(R.id.textViewAboutTitle);
        TextView textViewAboutWebsite = (TextView) view.findViewById(R.id.textViewAboutWebsite);
        TextView textViewAboutDescription = (TextView) view.findViewById(R.id.textViewAboutDescription);
        TextView textViewAboutFreepik = (TextView) view.findViewById(R.id.textViewAboutFreepik);
        TextView textViewAboutStackskills = (TextView) view.findViewById(R.id.textViewAboutStackskills);

        final float densityValue = getContext().getResources().getDisplayMetrics().density;
        final float widthPixelsValue = getContext().getResources().getDisplayMetrics().widthPixels;

        final float newTextSizeSmall  =  (float)( widthPixelsValue / densityValue / 30 );
        final float newTextSizeMedium  =  (float)( widthPixelsValue / densityValue / 20 );
        final float newTextSizeBig  =  (float)( widthPixelsValue / densityValue / 15 );

        textViewAboutTitle.setTextSize(newTextSizeBig);
        textViewAboutWebsite.setTextSize(newTextSizeMedium);
        textViewAboutDescription.setTextSize(newTextSizeSmall);
        textViewAboutFreepik.setTextSize(newTextSizeSmall);
        textViewAboutStackskills.setTextSize(newTextSizeSmall);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onAboutInteractionListener(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AboutInteractionListener) {
            mListener = (AboutInteractionListener) context;
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
    public interface AboutInteractionListener {
        // TODO: Update argument type and name
        void onAboutInteractionListener(Uri uri);
    }
}
