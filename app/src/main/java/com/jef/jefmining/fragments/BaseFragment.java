package com.jef.jefmining.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jef.jefmining.rest.EURORestCalls;

public class BaseFragment extends Fragment {

    protected Runnable restRunnable = null;

    protected EURORestCalls euroRestCalls;

    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (euroRestCalls != null) {
            euroRestCalls.cancel(true);
        }
    }


    private void persistIntoBundle() {

    }



}