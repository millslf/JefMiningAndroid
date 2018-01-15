package com.jef.jefmining.rest;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.jef.jefmining.R;
import com.jef.jefmining.cex.CexHelper;
import com.jef.jefmining.currency.BCTOCURRENCY;
import com.jef.jefmining.currency.BCZAR;
import com.jef.jefmining.currency.CurrencyHelper;
import com.jef.jefmining.currency.RUBtoZAR;
import com.jef.jefmining.fragments.FragmentRUB;
import com.jef.jefmining.luno.LunoHelper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by ettienne on 2017/12/18.
 */

public class RUBRestCalls extends BaseRestCalls {
    private BCTOCURRENCY bcRub;
    private BCZAR bczar;
    private RUBtoZAR rubToZAR;
    private boolean allSyncDone = true;
    private boolean lunoTrendUp;
    private boolean bcRubTrendUp;

    public RUBRestCalls(Activity context, boolean isVissible) {
        super("BUSY SYNCING", context, isVissible);
    }

    @Override
    public Void doWork(Object... objects) throws Exception {

        RestClient<String> restClient = new RestClient<>();
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        try {

            Set<Callable<FutureResult>> callables = new HashSet<>();

            callables.add(() -> {
                return new FutureResult(LunoHelper.isTrendUp(restClient, context), "lunoTrendUp");
            });

            callables.add(() -> {
                return new FutureResult(CexHelper.isTrendUp(restClient, context, "RUB"), "bcRubTrendUp");
            });

            callables.add(() -> {
                return new FutureResult(CexHelper.getBCToCurrency(restClient, context, "RUB"), "bcRub");
            });

            callables.add(() -> {
                return new FutureResult(LunoHelper.getLastLunoTrade(restClient, context), "bczar");
            });

            callables.add(() -> {
                return new FutureResult(CurrencyHelper.getRubToZar(restClient, context), "rubToZAR");
            });

            List<Future<FutureResult>> futures = executorService.invokeAll(callables);

            for (Future<FutureResult> future : futures) {
                if (future.get().getName().equals("lunoTrendUp")) {
                    lunoTrendUp = (Boolean) future.get().getResult();
                }

                if (future.get().getName().equals("bcRubTrendUp")) {
                    bcRubTrendUp = (Boolean) future.get().getResult();
                }

                if (future.get().getName().equals("bcRub")) {
                    bcRub = (BCTOCURRENCY) future.get().getResult();
                }

                if (future.get().getName().equals("bczar")) {
                    bczar = (BCZAR) future.get().getResult();
                }

                if (future.get().getName().equals("rubToZAR")) {
                    rubToZAR = (RUBtoZAR) future.get().getResult();
                }
            }

        } finally {
            executorService.shutdown();
            new Handler(Looper.getMainLooper()).post(() -> {
                if (context != null) {
                    SwipeRefreshLayout swipeLayout = context.findViewById(R.id.swipe_containerRub);
                    if (swipeLayout != null) {
                        swipeLayout.setRefreshing(false);
                    }
                }
            });
        }
        return null;
    }


    @Override
    public void onResult(final Void value) {

        if (allSyncDone) {

            // Calculate spread
            Double spread = getSpread(Double.parseDouble(bczar.getLastTrade()), Double.parseDouble(bcRub.getLprice()),
                    Double.parseDouble(rubToZAR.getRubZar()));

            CheckBox buzzCheckBox = context.findViewById(R.id.buzz);
            final double spreadAlert = context.getSharedPreferences(null, Context.MODE_PRIVATE).getFloat("SPREADRUB", 10);

            if (!isVissible) {

                // Do alert
                if (spreadAlert <= spread) {
                    if (buzzCheckBox.isChecked()) {
                        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(1000);
                    }

                    Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.jefmining)
                            .setContentTitle("BitCoin Spread!")
                            .setContentText("BUY RUB  : Spread  = " + spread)
                            .setSound(soundUri)
                            .setAutoCancel(true);
                    Intent intent = new Intent(context, FragmentRUB.class);
                    PendingIntent pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    mBuilder.setContentIntent(pi);
                    NotificationManager mNotificationManager =
                            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(0, mBuilder.build());
                }
            } else {

                ImageView bcZarTrend = context.findViewById(R.id.BCZARTrendImageRUB);
                if (lunoTrendUp) {
                    bcZarTrend.setImageResource(R.drawable.uparrow);
                } else {
                    bcZarTrend.setImageResource(R.drawable.downarrow);
                }

                ImageView bcUSDTrendImage = context.findViewById(R.id.BCRUBTrendImage);
                if (bcRubTrendUp) {
                    bcUSDTrendImage.setImageResource(R.drawable.uparrow);
                } else {
                    bcUSDTrendImage.setImageResource(R.drawable.downarrow);
                }

                EditText bcrubEditTExt = context.findViewById(R.id.BCRUB);
                bcrubEditTExt.setText(String.format("%.2f", new Double(bcRub.getLprice())));

                EditText bczarEditTExt = context.findViewById(R.id.BCZARONRUB);
                bczarEditTExt.setText(String.format("R%.2f", new Double(bczar.getLastTrade())));

                EditText rubToZarEditTExt = context.findViewById(R.id.RUBtoZAR);
                rubToZarEditTExt.setText(String.format("R%.2f", new Double(rubToZAR.getRubZar())));

                EditText spreadEditText = context.findViewById(R.id.spreadrub);
                spreadEditText.setText("SPREAD:  " + String.format("%.2f", spread));

                if (spread < 0) {
                    spreadEditText.setTextColor(Color.RED);
                } else {
                    spreadEditText.setTextColor(Color.BLUE);
                }

                EditText buyRUBOEdit = context.findViewById(R.id.buyRUB);
                if (!buyRUBOEdit.getText().toString().isEmpty()) {

                    SharedPreferences.Editor editor = context.getSharedPreferences(null, Context.MODE_PRIVATE).edit();
                    editor.putFloat("BUYRUB", new Float(buyRUBOEdit.getText().toString())).commit();

                    EditText actualCostRand = context.findViewById(R.id.actualCostRandRub);
                    Double actualCostValue = new Double(getFnbExchange(Double.parseDouble(rubToZAR.getRubZar())) * (new Double(buyRUBOEdit.getText().toString()) * 1.035));
                    actualCostRand.setText(String.format("R%.2f", actualCostValue));

                    EditText bitCoinsBought = context.findViewById(R.id.bitCoinsBoughtRub);

                    bitCoinsBought.setText(new Double(new Double(buyRUBOEdit.getText().toString())
                            / new Double(bcRub.getLprice()) - 0.0012).toString());

                    EditText sellAtCurrentRate = context.findViewById(R.id.sellAtCurrentRateRub);
                    Double sellCurrentRateValue = new Double(bczar.getLastTrade()) * new Double(bitCoinsBought.getText().toString());
                    sellAtCurrentRate.setText(String.format("R%.2f", sellCurrentRateValue));

                    EditText profit = context.findViewById(R.id.profitRub);
                    Double profitValue = sellCurrentRateValue - actualCostValue;
                    profit.setText(String.format("R%.2f", new Double(profitValue)));

                    if (profitValue < 0) {
                        profit.setTextColor(Color.RED);
                    } else {
                        profit.setTextColor(Color.BLUE);
                    }
                }
            }
        }
    }

    @Override
    public void onError() {
        if (context != null) {
            Toast toast = Toast.makeText(context, exception.getMessage(), Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
