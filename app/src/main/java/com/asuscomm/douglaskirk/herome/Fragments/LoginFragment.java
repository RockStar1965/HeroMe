package com.asuscomm.douglaskirk.herome.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.asuscomm.douglaskirk.herome.R;
import com.asuscomm.douglaskirk.herome.Activities.MainActivity;

import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    private LoginInteractionListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        String restApiSource = getArguments().getString("restApiSource");
        String textEmail = getArguments().getString("textEmail");
        String textPassword = getArguments().getString("textPassword");
        String textPowerup = getArguments().getString("textPowerup");
        String textUsername = getArguments().getString("textUsername");
        String textName = getArguments().getString("textName");
        String restApiMessage = getArguments().getString("restApiMessage");

        EditText editTextEmail = (EditText)view.findViewById(R.id.editTextEmail);
        EditText editTextPassword = (EditText)view.findViewById(R.id.editTextPassword);
        EditText editTextPowerup = (EditText)view.findViewById(R.id.editTextPowerup);
        EditText editTextUsername = (EditText)view.findViewById(R.id.editTextUsername);
        EditText editTextName = (EditText)view.findViewById(R.id.editTextName);

        final TextView textViewLoginTitle = (TextView)view.findViewById(R.id.textViewLoginTitle);
        final TextView textViewEmail = (TextView)view.findViewById(R.id.textViewEmail);
        final TextView textViewPassword = (TextView)view.findViewById(R.id.textViewPassword);
        final TextView textViewPowerup = (TextView)view.findViewById(R.id.textViewPowerup);
        final TextView textViewUsername = (TextView)view.findViewById(R.id.textViewUsername);
        final TextView textViewName = (TextView)view.findViewById(R.id.textViewName);

        final TextView textViewMessage = (TextView)view.findViewById(R.id.textViewMessage);
        textViewMessage.setText(restApiMessage);

        final float densityValue = getContext().getResources().getDisplayMetrics().density;
        final float widthPixelsValue = getContext().getResources().getDisplayMetrics().widthPixels;

        final float newTextSizeSmall  =  (float)( widthPixelsValue / densityValue / 30 );
        final float newTextSizeMedium  =  (float)( widthPixelsValue / densityValue / 20 );
        final float newTextSizeBig  =  (float)( widthPixelsValue / densityValue / 15 );

        textViewLoginTitle.setTextSize(newTextSizeBig);
        editTextEmail.setTextSize(newTextSizeMedium);
        editTextPassword.setTextSize(newTextSizeMedium);
        editTextPowerup.setTextSize(newTextSizeMedium);
        editTextUsername.setTextSize(newTextSizeMedium);
        editTextName.setTextSize(newTextSizeMedium);
        textViewEmail.setTextSize(newTextSizeMedium);
        textViewPassword.setTextSize(newTextSizeMedium);
        textViewPowerup.setTextSize(newTextSizeMedium);
        textViewUsername.setTextSize(newTextSizeMedium);
        textViewName.setTextSize(newTextSizeMedium);
        textViewMessage.setTextSize(newTextSizeMedium);

        final Random randomNumber = new Random();

        editTextUsername.setVisibility(View.INVISIBLE);
        textViewUsername.setVisibility(View.INVISIBLE);
        editTextName.setVisibility(View.INVISIBLE);
        textViewName.setVisibility(View.INVISIBLE);
        editTextPowerup.setVisibility(View.INVISIBLE);
        textViewPowerup.setVisibility(View.INVISIBLE);

        editTextEmail.setText(textEmail);
        editTextPassword.setText(textPassword);
        editTextPowerup.setText(textPowerup);
        editTextUsername.setText(textUsername);
        editTextName.setText(textName);

        String sourceApi = "";
        if (restApiSource.equals("login") || restApiMessage != null) {
            editTextUsername.setVisibility(View.INVISIBLE);
            textViewUsername.setVisibility(View.INVISIBLE);
            editTextName.setVisibility(View.INVISIBLE);
            textViewName.setVisibility(View.INVISIBLE);
            editTextPowerup.setVisibility(View.INVISIBLE);
            textViewPowerup.setVisibility(View.INVISIBLE);
        } else {
            editTextUsername.setVisibility(View.VISIBLE);
            textViewUsername.setVisibility(View.VISIBLE);
            editTextName.setVisibility(View.VISIBLE);
            textViewName.setVisibility(View.VISIBLE);
            editTextPowerup.setVisibility(View.VISIBLE);
            textViewPowerup.setVisibility(View.VISIBLE);

            if (editTextUsername.getText().toString().equals("")) {
                int randomUser = randomNumber.nextInt(10000);
                editTextUsername.setText("Hero" + Integer.toString(randomUser));
            }
            if (editTextName.getText().toString().equals("")) {
                editTextName.setText("Mr Hero");
            }
        }
        // Inflate the layout for this fragment
        return view;
    }



    @Override
    public void onClick(View v) {
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onLoginFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoginInteractionListener) {
            mListener = (LoginInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ButtonInteractiveListener");
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface LoginInteractionListener {
        // TODO: Update argument type and name
        void onLoginFragmentInteraction(Uri uri);
    }
}
