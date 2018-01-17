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
import com.jef.jefmining.rest.GBPRestCalls;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FragmentGBP extends BaseFragment {

    protected GBPRestCalls gdpRestCalls;

    public FragmentGBP() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_gbp, container, false);
        final double spread = getActivity().getSharedPreferences(null, Context.MODE_PRIVATE).getFloat("SPREADGBP", 10);
        final EditText spreadEditText = view.findViewById(R.id.spreadHighGBP);
        spreadEditText.setText(spread + "");

        final Button resyncButton = view.findViewById(R.id.resyncGbp);
        resyncButton.setOnClickListener(v -> {

            SharedPreferences.Editor editor = getActivity().getSharedPreferences(null, Context.MODE_PRIVATE).edit();
            editor.putFloat("SPREADGBP", Float.parseFloat(spreadEditText.getText().toString())).commit();

            if (gdpRestCalls != null) {
                gdpRestCalls.cancel(true);
            }

            gdpRestCalls = new GBPRestCalls(getActivity(), true);
            gdpRestCalls.execute();
            resyncButton.setText("Resync\nLast Sync : " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
        });

        CheckBox buzz = view.findViewById(R.id.buzz);
        final boolean buzzChecked = getActivity().getSharedPreferences(null, Context.MODE_PRIVATE).getBoolean("BUZZGBP", true);
        buzz.setChecked(buzzChecked);
        buzz.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = getActivity().getSharedPreferences(null, Context.MODE_PRIVATE).edit();
            editor.putBoolean("BUZZGBP", isChecked).commit();
        });

        CheckBox sound = view.findViewById(R.id.sound);
        final boolean soundChecked = getActivity().getSharedPreferences(null, Context.MODE_PRIVATE).getBoolean("SOUNDGBP", true);
        sound.setChecked(soundChecked);
        sound.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = getActivity().getSharedPreferences(null, Context.MODE_PRIVATE).edit();
            editor.putBoolean("SOUNDGBP", isChecked).commit();
        });

        EditText buyGBPEdit = view.findViewById(R.id.buyGBP);
        Float buy = getActivity().getSharedPreferences(null, Context.MODE_PRIVATE).getFloat("BUYGBP", 2000);
        buyGBPEdit.setText(buy.toString());

        final Handler handler = new Handler();
        restRunnable = () -> {

            if (gdpRestCalls != null) {
                gdpRestCalls.cancel(true);
            }

            gdpRestCalls = new GBPRestCalls(getActivity(), false);
            gdpRestCalls.execute();

            resyncButton.setText("Resync\nLast Sync : " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
           // handler.postDelayed(restRunnable, MainActivity.syncTime);
        };
        handler.postDelayed(restRunnable, MainActivity.syncTime);

        SwipeRefreshLayout swipeLayout = view.findViewById(R.id.swipe_containerGbp);
        swipeLayout.setOnRefreshListener(() ->

        {
            if (gdpRestCalls != null) {
                gdpRestCalls.cancel(true);
            }

            gdpRestCalls = new GBPRestCalls(getActivity(), true);
            gdpRestCalls.execute();
            resyncButton.setText("Resync\nLast Sync : " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));

        });

        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return view;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (gdpRestCalls != null) {
            gdpRestCalls.cancel(true);
        }
    }
}