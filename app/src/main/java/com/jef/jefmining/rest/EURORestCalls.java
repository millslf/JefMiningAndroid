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
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.jef.jefmining.R;
import com.jef.jefmining.currency.EURtoZAR;
import com.jef.jefmining.fragments.FragmentEURO;

/**
 * Created by ettienne on 2017/12/18.
 */

public class EURORestCalls extends BaseRestCalls {

    private EURtoZAR eurToZAR;
    private boolean allSyncDone = true;

    public EURORestCalls(Activity context, boolean isVissible) {
        super("BUSY SYNCING", context, isVissible);
        currency = "EUR";
        swipeLayout = context.findViewById(R.id.swipe_containerEuro);
        currencyClass = EURtoZAR.class;
    }

    @Override
    protected void setCurrencyToZarCurrency(Object object) {
        eurToZAR = (EURtoZAR) object;
    }

    @Override
    public void onResult(final Void value) {

        if (allSyncDone) {

            // Calculate spread
            Double spread = getSpread(Double.parseDouble(bczar.getLastTrade()), Double.parseDouble(bcToCurrency.getLprice()),
                    Double.parseDouble(eurToZAR.getEurZar()));

            CheckBox buzzCheckBox = context.findViewById(R.id.buzz);

            final double spreadAlert = context.getSharedPreferences(null, Context.MODE_PRIVATE).getFloat("SPREADEURO", 10);

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
                            .setContentText("BUY EURO  : Spread  = " + spread)
                            .setSound(soundUri)
                            .setAutoCancel(true);
                    Intent intent = new Intent(context, FragmentEURO.class);
                    PendingIntent pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    mBuilder.setContentIntent(pi);
                    NotificationManager mNotificationManager =
                            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(0, mBuilder.build());
                }
            } else {
                ImageView bcZarTrend = context.findViewById(R.id.BCZARTrendImageEUR);
                if (lunoTrendUp) {
                    bcZarTrend.setImageResource(R.drawable.uparrow);
                } else {
                    bcZarTrend.setImageResource(R.drawable.downarrow);
                }

                ImageView bcUSDTrendImage = context.findViewById(R.id.BCEURTrendImage);
                if (bcCurrencyTrendUp) {
                    bcUSDTrendImage.setImageResource(R.drawable.uparrow);
                } else {
                    bcUSDTrendImage.setImageResource(R.drawable.downarrow);
                }

                EditText bceurEditTExt = context.findViewById(R.id.BCEURO);
                bceurEditTExt.setText(String.format("%.2f", new Double(bcToCurrency.getLprice())));

                EditText bczarEditTExt = context.findViewById(R.id.BCZARONEUR);
                bczarEditTExt.setText(String.format("R%.2f", new Double(bczar.getLastTrade())));

                EditText eurToZarEditTExt = context.findViewById(R.id.EUROtoZAR);
                eurToZarEditTExt.setText(String.format("R%.2f", new Double(eurToZAR.getEurZar())));

                EditText spreadEditText = context.findViewById(R.id.spreadeur);
                spreadEditText.setText("SPREAD:  " + String.format("%.2f", spread));

                if (spread < 0) {
                    spreadEditText.setTextColor(Color.RED);
                } else {
                    spreadEditText.setTextColor(Color.BLUE);
                }

                // Calculate profit
                EditText buyEUROEdit = context.findViewById(R.id.buyEURO);
                if (!buyEUROEdit.getText().toString().isEmpty()) {

                    SharedPreferences.Editor editor = context.getSharedPreferences(null, Context.MODE_PRIVATE).edit();
                    editor.putFloat("BUYEURO", new Float(buyEUROEdit.getText().toString())).commit();

                    EditText actualCostRand = context.findViewById(R.id.actualCostRandEuro);
                    Double actualCostValue = new Double(getFnbExchange(Double.parseDouble(eurToZAR.getEurZar())) * (new Double(buyEUROEdit.getText().toString()) * 1.035));
                    actualCostRand.setText(String.format("R%.2f", actualCostValue));

                    EditText bitCoinsBought = context.findViewById(R.id.bitCoinsBoughtEur);

                    bitCoinsBought.setText(new Double(new Double(buyEUROEdit.getText().toString())
                            / new Double(bcToCurrency.getLprice()) - 0.0012).toString());

                    EditText sellAtCurrentRate = context.findViewById(R.id.sellAtCurrentRateEuro);
                    Double sellCurrentRateValue = new Double(bczar.getLastTrade()) * new Double(bitCoinsBought.getText().toString());
                    sellAtCurrentRate.setText(String.format("R%.2f", sellCurrentRateValue));

                    EditText profit = context.findViewById(R.id.profitEuro);
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
