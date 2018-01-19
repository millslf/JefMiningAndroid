package com.jef.jefmining.fragments;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jef.jefmining.CustomPairAdapter;
import com.jef.jefmining.Pairing;
import com.jef.jefmining.R;
import com.jef.jefmining.cex.CexHelper;
import com.jef.jefmining.coinapult.CoinApultHelper;
import com.jef.jefmining.exmo.ExmoHelper;

import org.json.JSONException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

public class FragmentCryptoSpreadPair extends BaseFragment {

    Button resyncButton;

    public FragmentCryptoSpreadPair() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_cryto_spread_pair, container, false);
        resyncButton = view.findViewById(R.id.resyncCryptoSpread);

        /*SwipeRefreshLayout swipeLayout = view.findViewById(R.id.scrollingId);*/

        /*swipeLayout.setOnRefreshListener(() -> {
            buildPairings();
            resyncButton.setText("Resync\nLast Sync : " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
            swipeLayout.setRefreshing(false);
        });*/

        resyncButton.setOnClickListener(v -> {
            resyncButton.setEnabled(false);
            buildPairings();
            resyncButton.setText("Resync\nLast Sync : " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
        });

        /*swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);*/

        return view;

    }

    @SuppressLint("WrongViewCast")
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void buildPairings() {
        try {
            TreeMap<String, Double> cex = CexHelper.getLastCryptoPrice(getActivity());
            TreeMap<String, Double> coinApult = CoinApultHelper.getLastCryptoPrice(getActivity());
            TreeMap<String, Double> exmo = ExmoHelper.getLastCryptoPrice(getActivity());


            List<Pairing> cexCoinApult = Pairing.createParing("CEX", cex, "COINAPULT", coinApult);
            List<Pairing> cexExmo = Pairing.createParing("CEX", cex, "EXMO", exmo);
            List<Pairing> exmoCoinApult = Pairing.createParing("EXMO", exmo, "COINAPULT", coinApult);

            createList(cexCoinApult, getActivity().findViewById(R.id.pairListCexCoinApult), R.id.exchange1, "CEX", R.id.exchange2, "COINAPULT");
            createList(cexExmo, getActivity().findViewById(R.id.pairListCexExmo), R.id.exchange1, "CEX", R.id.exchange2, "EXMO");
            createList(exmoCoinApult, getActivity().findViewById(R.id.pairListExmoCoinApult), R.id.exchange1, "EXMO", R.id.exchange2, "COINAPULT");


        } catch (IOException | JSONException | InterruptedException | ExecutionException e) {

            new Handler(Looper.getMainLooper()).post(() -> {
                Toast toast = Toast.makeText(this.getActivity(), e.getMessage(), Toast.LENGTH_LONG);
                toast.show();
            });

            Log.e(CexHelper.class.getName(), e.getMessage());
        } finally {
            resyncButton.setEnabled(true);
        }
    }


    private void createList(List<Pairing> pairing, ListView listView, int exchange1Id, String exchange1Value, int exchange2Id, String exchange2Value) {
        ViewGroup header = (ViewGroup) getLayoutInflater().inflate(R.layout.pair_row_item_header, listView, false);
        TextView exchange1 = header.findViewById(exchange1Id);
        exchange1.setText(exchange1Value);
        TextView exchange2 = header.findViewById(exchange2Id);
        exchange2.setText(exchange2Value);

        if (listView.getHeaderViewsCount() == 0) {
            listView.addHeaderView(header, null, false);
        }

        CustomPairAdapter customAdapter = new CustomPairAdapter(pairing, getContext());
        listView.setAdapter(customAdapter);

    }

}