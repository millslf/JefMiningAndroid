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
import com.jef.jefmining.rest.EURORestCalls;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FragmentEURO extends BaseFragment {

    protected EURORestCalls euroRestCalls;

    public FragmentEURO() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_euro, container, false);
        final double spread = getActivity().getSharedPreferences(null, Context.MODE_PRIVATE).getFloat("SPREADEURO", 10);
        final EditText spreadEditText = view.findViewById(R.id.spreadHighEURO);
        spreadEditText.setText(spread + "");

        final Button resyncButton = view.findViewById(R.id.resyncEuro);
        resyncButton.setOnClickListener(v -> {
            SharedPreferences.Editor editor = getActivity().getSharedPreferences(null, Context.MODE_PRIVATE).edit();
            editor.putFloat("SPREADEURO", Float.parseFloat(spreadEditText.getText().toString())).commit();

            if (euroRestCalls != null) {
                euroRestCalls.cancel(true);
            }

            euroRestCalls = new EURORestCalls(getActivity(), true);
            euroRestCalls.execute();
            resyncButton.setText("Resync\nLast Sync : " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
        });

        CheckBox buzz = view.findViewById(R.id.buzz);
        final boolean buzzChecked = getActivity().getSharedPreferences(null, Context.MODE_PRIVATE).getBoolean("BUZZEURO", true);
        buzz.setChecked(buzzChecked);
        buzz.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = getActivity().getSharedPreferences(null, Context.MODE_PRIVATE).edit();
            editor.putBoolean("BUZZEURO", isChecked).commit();
        });

        CheckBox sound = view.findViewById(R.id.sound);
        final boolean soundChecked = getActivity().getSharedPreferences(null, Context.MODE_PRIVATE).getBoolean("SOUNDEURO", true);
        sound.setChecked(soundChecked);
        sound.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = getActivity().getSharedPreferences(null, Context.MODE_PRIVATE).edit();
            editor.putBoolean("SOUNDEURO", isChecked).commit();
        });

        EditText buyEUROEdit = view.findViewById(R.id.buyEURO);
        Float buy = getActivity().getSharedPreferences(null, Context.MODE_PRIVATE).getFloat("BUYEURO", 3000);
        buyEUROEdit.setText(buy.toString());

        final Handler handler = new Handler();
        restRunnable = () -> {
            if (euroRestCalls != null) {
                euroRestCalls.cancel(true);
            }
            euroRestCalls = new EURORestCalls(getActivity(), false);
            euroRestCalls.execute();
            //handler.postDelayed(restRunnable, MainActivity.syncTime);
            resyncButton.setText("Resync\nLast Sync : " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
        };
        handler.postDelayed(restRunnable, MainActivity.syncTime);

        SwipeRefreshLayout swipeLayout = view.findViewById(R.id.swipe_containerEuro);
        swipeLayout.setOnRefreshListener(() -> {
            if (euroRestCalls != null) {
                euroRestCalls.cancel(true);
            }

            euroRestCalls = new EURORestCalls(getActivity(), true);
            euroRestCalls.execute();
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
        if (euroRestCalls != null) {
            euroRestCalls.cancel(true);
        }
    }

}