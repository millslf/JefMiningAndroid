package com.jef.jefmining.fragments;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.jef.jefmining.CustomPairAdapter;
import com.jef.jefmining.Pairing;
import com.jef.jefmining.R;
import com.jef.jefmining.cex.CexHelper;
import com.jef.jefmining.coinapult.CoinApultHelper;

import org.json.JSONException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

public class FragmentCryptoSpreadPair extends BaseFragment {

    public FragmentCryptoSpreadPair() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_cryto_spread_pair, container, false);
        final Button resyncButton = view.findViewById(R.id.resyncCryptoSpread);

        SwipeRefreshLayout swipeLayout = view.findViewById(R.id.swipe_crypto_spread_pair);
        swipeLayout.setOnRefreshListener(() -> {
            buildPairings();
            resyncButton.setText("Resync\nLast Sync : " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
            swipeLayout.setRefreshing(false);
        });

        resyncButton.setOnClickListener(v -> {
            swipeLayout.setRefreshing(true);
            buildPairings();
            resyncButton.setText("Resync\nLast Sync : " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
            swipeLayout.setRefreshing(false);
        });

        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return view;

    }

    @SuppressLint("WrongViewCast")
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void buildPairings() {
        try {
            TreeMap<String, Double> cex = CexHelper.getLastCryptoPrice(getActivity());
            TreeMap<String, Double> coinApult = CoinApultHelper.getLastCryptoPrice(getActivity());
            List<Pairing> cex_coinApult = Pairing.createParing("CEX", cex, "COINAPULT", coinApult);
            ListView cexCoinApultListView = getActivity().findViewById(R.id.pairList);

            ViewGroup header = (ViewGroup) getLayoutInflater().inflate(R.layout.pair_row_item_header, cexCoinApultListView, false);
            TextView exchange1 = header.findViewById(R.id.exchange1);
            exchange1.setText("CEX");
            TextView exchange2 = header.findViewById(R.id.exchange2);
            exchange2.setText("COINAPULT");

            if (cexCoinApultListView.getHeaderViewsCount() == 0) {
                cexCoinApultListView.addHeaderView(header, null, false);
            }

            CustomPairAdapter customAdapter = new CustomPairAdapter(cex_coinApult, getContext());
            cexCoinApultListView.setAdapter(customAdapter);

        } catch (IOException | JSONException | InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

}