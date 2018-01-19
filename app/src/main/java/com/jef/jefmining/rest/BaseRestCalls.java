package com.jef.jefmining.rest;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.jef.jefmining.MainActivity;
import com.jef.jefmining.R;
import com.jef.jefmining.cex.CexHelper;
import com.jef.jefmining.currency.BCTOCURRENCY;
import com.jef.jefmining.currency.BCZAR;
import com.jef.jefmining.currency.CurrencyHelper;
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

public abstract class BaseRestCalls extends SyncTask<Void> {

    protected BCZAR bczar;
    protected boolean lunoTrendUp;
    protected boolean bcCurrencyTrendUp;
    protected BCTOCURRENCY bcToCurrency;
    protected String currency;
    protected SwipeRefreshLayout swipeLayout;
    protected Class currencyClass;
    protected boolean allSyncDone = false;

    public BaseRestCalls(String progressMessage, Activity context, boolean isVissible) {
        super(progressMessage, context, isVissible);
    }

    public BaseRestCalls(Activity context) {
        super(context);
    }

    public abstract Double getExchangeRate();

    public static class FutureResult {
        Object result;
        String name;

        public FutureResult(Object result, String name) {
            this.result = result;
            this.name = name;
        }

        public Object getResult() {
            return result;
        }

        public void setResult(Object result) {
            this.result = result;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    protected double getFnbExchange(Double amt) {
        return amt * 1.035;
    }

    protected double getCost(Double amt) {
        return amt * 0.0012;
    }

    protected double getSpread(Double randValBC, Double foreignBCCurrency, Double exchangeRate) {
        return (randValBC - (foreignBCCurrency * getFnbExchange(exchangeRate)) - getCost(randValBC)) / (randValBC - getCost(randValBC)) * 100 - 3.65;
    }

    @Override
    public Void doWork(Object... objects) throws Exception {
        allSyncDone = false;
        RestClient<String> restClient = new RestClient<>();
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        try {

            Set<Callable<FutureResult>> callables = new HashSet<>();

            callables.add(() -> {
                return new FutureResult(LunoHelper.isTrendUp(restClient, context), "lunoTrendUp");
            });

            callables.add(() -> {
                return new FutureResult(CexHelper.isTrendUp(restClient, context, currency), "bcCurrencyTrendUp");
            });

            callables.add(() -> {
                return new FutureResult(CexHelper.getBCToCurrency(restClient, context, currency), "bcCurrency");
            });

            callables.add(() -> {
                return new FutureResult(LunoHelper.getLastLunoTrade(restClient, context), "bczar");
            });

            callables.add(() -> {
                return new FutureResult(CurrencyHelper.getCurrencyToZar(restClient, context, currency, currencyClass), "currencyToZar");
            });

            List<Future<FutureResult>> futures = executorService.invokeAll(callables);

            for (Future<FutureResult> future : futures) {
                if (future.get().getName().equals("lunoTrendUp")) {
                    lunoTrendUp = (Boolean) future.get().getResult();
                }

                if (future.get().getName().equals("bcCurrencyTrendUp")) {
                    bcCurrencyTrendUp = (Boolean) future.get().getResult();
                }

                if (future.get().getName().equals("bcCurrency")) {
                    bcToCurrency = (BCTOCURRENCY) future.get().getResult();
                }

                if (future.get().getName().equals("bczar")) {
                    bczar = (BCZAR) future.get().getResult();
                }

                if (future.get().getName().equals("currencyToZar")) {
                    setCurrencyToZarCurrency(future.get().getResult());
                }
            }

            allSyncDone = true;
        } finally {
            executorService.shutdown();
            new Handler(Looper.getMainLooper()).post(() -> {

                if (swipeLayout != null) {
                    swipeLayout.setRefreshing(false);
                }

            });
        }
        return null;
    }

    abstract protected void setCurrencyToZarCurrency(Object object);

    @Override
    public void onResult(final Void value) {

        if (allSyncDone) {

            // Calculate spread
            Double spread = getSpread(Double.parseDouble(bczar.getLastTrade()), Double.parseDouble(bcToCurrency.getLprice()), getExchangeRate());
            final double spreadAlert = getSpreadAlertValue();

            if (!isVissible) {

                // Do alert
                if (spreadAlert <= spread) {
                    if (isBuzzCheckBoxChecked()) {
                        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(1000);
                    }

                    Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.jefmining)
                            .setContentTitle("BitCoin Spread!")
                            .setContentText("BUY EURO  : Spread  = " + spread)
                            .setSound(soundUri)
                            .setAutoCancel(true);
                    Intent intent = new Intent(context, MainActivity.class);
                    PendingIntent pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    mBuilder.setContentIntent(pi);
                    NotificationManager mNotificationManager =
                            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(0, mBuilder.build());
                }
            } else {

                ImageView bcZarTrend = getBCZarTrendImage();
                if (lunoTrendUp) {
                    bcZarTrend.setImageResource(R.drawable.uparrow);
                } else {
                    bcZarTrend.setImageResource(R.drawable.downarrow);
                }

                ImageView bcCurrencyTrendImage = getBCCurrencyTrendImage();
                if (bcCurrencyTrendUp) {
                    bcCurrencyTrendImage.setImageResource(R.drawable.uparrow);
                } else {
                    bcCurrencyTrendImage.setImageResource(R.drawable.downarrow);
                }

                EditText bcCurrencyText = getBCCurrencyText();
                bcCurrencyText.setText(String.format("%.2f", new Double(bcToCurrency.getLprice())));

                EditText bczarEditTExt = getBCZarEditText();
                bczarEditTExt.setText(String.format("R%.2f", new Double(bczar.getLastTrade())));

                EditText eurToZarEditTExt = getCurrencyToZarText();
                eurToZarEditTExt.setText(String.format("R%.2f", getExchangeRate()));

                EditText spreadEditText = getSpreadEditText();
                spreadEditText.setText("SPREAD:  " + String.format("%.2f", spread));

                if (spread < 0) {
                    spreadEditText.setTextColor(Color.RED);
                } else {
                    spreadEditText.setTextColor(Color.BLUE);
                }

                // Calculate profit
                EditText buyCurrencyText = getCurrencyBuyText();
                if (!buyCurrencyText.getText().toString().isEmpty()) {

                    setSharedPrefBuyCurrenctValue(buyCurrencyText.getText().toString());
                    EditText actualCostRand = getActualCostRandText();
                    Double actualCostValue = new Double(getFnbExchange(getExchangeRate()) * (new Double(buyCurrencyText.getText().toString()) * 1.035));
                    actualCostRand.setText(String.format("R%.2f", actualCostValue));

                    EditText bitCoinsBought = getBCBoughtText();
                    bitCoinsBought.setText(new Double(new Double(buyCurrencyText.getText().toString())
                            / new Double(bcToCurrency.getLprice()) - 0.0012).toString());

                    EditText sellAtCurrentZarRate = getSellCurrentZarRateText();

                    Double sellCurrentRateValue = new Double(bczar.getLastTrade()) * new Double(bitCoinsBought.getText().toString());
                    sellAtCurrentZarRate.setText(String.format("R%.2f", sellCurrentRateValue));

                    EditText profit = getProfitText();
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

    protected abstract EditText getProfitText();

    protected abstract EditText getSellCurrentZarRateText();

    protected abstract EditText getBCBoughtText();

    protected abstract EditText getActualCostRandText();

    protected abstract void setSharedPrefBuyCurrenctValue(String value);

    protected abstract EditText getCurrencyBuyText();

    protected abstract EditText getSpreadEditText();

    protected abstract EditText getCurrencyToZarText();

    protected abstract EditText getBCZarEditText();

    protected abstract EditText getBCCurrencyText();

    protected abstract ImageView getBCCurrencyTrendImage();

    protected abstract ImageView getBCZarTrendImage();

    protected abstract double getSpreadAlertValue();

    protected abstract boolean isBuzzCheckBoxChecked();

    @Override
    public void onError() {
        if (context != null) {
            Toast toast = Toast.makeText(context, exception.getMessage(), Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
