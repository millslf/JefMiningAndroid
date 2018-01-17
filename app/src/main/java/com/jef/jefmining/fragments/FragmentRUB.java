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
import com.jef.jefmining.rest.RUBRestCalls;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FragmentRUB extends BaseFragment {

    protected RUBRestCalls rubRestCalls;

    public FragmentRUB() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_rub, container, false);
        final double spread = getActivity().getSharedPreferences(null, Context.MODE_PRIVATE).getFloat("SPREADRUB", 10);
        final EditText spreadEditText = view.findViewById(R.id.spreadHighRUB);
        spreadEditText.setText(spread + "");

        final Button resyncButton = view.findViewById(R.id.resyncRub);
        resyncButton.setOnClickListener(v -> {

            SharedPreferences.Editor editor = getActivity().getSharedPreferences(null, Context.MODE_PRIVATE).edit();
            editor.putFloat("SPREADRUB", Float.parseFloat(spreadEditText.getText().toString())).commit();

            if (rubRestCalls != null) {
                rubRestCalls.cancel(true);
            }

            rubRestCalls = new RUBRestCalls(getActivity(), true);
            rubRestCalls.execute();
            resyncButton.setText("Resync\nLast Sync : " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
        });

        CheckBox buzz = view.findViewById(R.id.buzzRub);
        final boolean buzzChecked = getActivity().getSharedPreferences(null, Context.MODE_PRIVATE).getBoolean("BUZZRUB", true);
        buzz.setChecked(buzzChecked);
        buzz.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = getActivity().getSharedPreferences(null, Context.MODE_PRIVATE).edit();
            editor.putBoolean("BUZZRUB", isChecked).commit();
        });

        CheckBox sound = view.findViewById(R.id.sound);
        final boolean soundChecked = getActivity().getSharedPreferences(null, Context.MODE_PRIVATE).getBoolean("SOUNDRUB", true);
        sound.setChecked(soundChecked);
        sound.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = getActivity().getSharedPreferences(null, Context.MODE_PRIVATE).edit();
            editor.putBoolean("SOUNDRUB", isChecked).commit();
        });

        EditText buyRUBEdit = view.findViewById(R.id.buyRUB);
        Float buy = getActivity().getSharedPreferences(null, Context.MODE_PRIVATE).getFloat("BUYRUB", 1000);
        buyRUBEdit.setText(buy.toString());

        final Handler handler = new Handler();
        restRunnable = () -> {

            if (rubRestCalls != null) {
                rubRestCalls.cancel(true);
            }

            rubRestCalls = new RUBRestCalls(getActivity(), false);
            rubRestCalls.execute();

            resyncButton.setText("Resync\nLast Sync : " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
           // handler.postDelayed(restRunnable, 1000 * 60 * 5);

        };
        handler.postDelayed(restRunnable, MainActivity.syncTime);

        SwipeRefreshLayout swipeLayout = view.findViewById(R.id.swipe_containerRub);
        swipeLayout.setOnRefreshListener(() -> {
            if (rubRestCalls != null) {
                rubRestCalls.cancel(true);
            }

            rubRestCalls = new RUBRestCalls(getActivity(), true);
            rubRestCalls.execute();
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

        if (rubRestCalls != null) {
            rubRestCalls.cancel(true);
        }
    }
}