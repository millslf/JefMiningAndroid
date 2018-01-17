package com.jef.jefmining.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.jef.jefmining.MainActivity;
import com.jef.jefmining.R;
import com.jef.jefmining.rest.USDRestCalls;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Anu on 22/04/17.
 */


public class FragmentUSD extends BaseFragment {

    protected USDRestCalls usdRest;

    public FragmentUSD() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_usd, container, false);
        final double spread = getActivity().getSharedPreferences(null, Context.MODE_PRIVATE).getFloat("SPREADUSD", 10);
        final EditText spreadEditText = view.findViewById(R.id.spreadHighUSD);
        spreadEditText.setText(spread + "");

        final Button resyncButton = view.findViewById(R.id.resyncUsd);
        resyncButton.setOnClickListener(v -> {

            SharedPreferences.Editor editor = getActivity().getSharedPreferences(null, Context.MODE_PRIVATE).edit();
            editor.putFloat("SPREADUSD", Float.parseFloat(spreadEditText.getText().toString())).commit();

            if (usdRest != null) {
                usdRest.cancel(true);
            }

            usdRest = new USDRestCalls(getActivity(), true);
            usdRest.execute();
            resyncButton.setText("Resync\nLast Sync : " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
        });

        CheckBox buzz = view.findViewById(R.id.buzzUsd);
        final boolean buzzChecked = getActivity().getSharedPreferences(null, Context.MODE_PRIVATE).getBoolean("BUZZUSD", true);
        buzz.setChecked(buzzChecked);
        buzz.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = getActivity().getSharedPreferences(null, Context.MODE_PRIVATE).edit();
            editor.putBoolean("BUZZUSD", isChecked).commit();
        });

        CheckBox sound = view.findViewById(R.id.sound);
        final boolean soundChecked = getActivity().getSharedPreferences(null, Context.MODE_PRIVATE).getBoolean("SOUNDUSD", true);
        sound.setChecked(soundChecked);
        sound.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = getActivity().getSharedPreferences(null, Context.MODE_PRIVATE).edit();
            editor.putBoolean("SOUNDUSD", isChecked).commit();
        });

        EditText buyUSDEdit = view.findViewById(R.id.buyUSD);
        Float buy = getActivity().getSharedPreferences(null, Context.MODE_PRIVATE).getFloat("BUYUSD", 3000);
        buyUSDEdit.setText(buy.toString());

        final Handler handler = new Handler();
        restRunnable = () -> {

            if (usdRest != null) {
                usdRest.cancel(true);
            }
            usdRest = new USDRestCalls(getActivity(), false);
            usdRest.execute();
            resyncButton.setText("Resync\nLast Sync : " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
            //handler.postDelayed(restRunnable, MainActivity.syncTime);
        };
        handler.postDelayed(restRunnable, MainActivity.syncTime);

        SwipeRefreshLayout swipeLayout = view.findViewById(R.id.swipe_containerUsd);
        swipeLayout.setOnRefreshListener(() -> {
            if (usdRest != null) {
                usdRest.cancel(true);
            }

            usdRest = new USDRestCalls(getActivity(), true);
            usdRest.execute();
            resyncButton.setText("Resync\nLast Sync : " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));

        });

        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return view;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (usdRest != null) {
            usdRest.cancel(true);
        }
    }
}