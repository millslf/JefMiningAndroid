package com.jef.jefmining.rest;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.jef.jefmining.R;
import com.jef.jefmining.currency.USDtoZAR;

/**
 * Created by ettienne on 2017/12/18.
 */

public class USDRestCalls extends BaseRestCalls {
    private USDtoZAR usdToZAR;
    private boolean allSyncDone = true;

    public USDRestCalls(Activity context, boolean isVissible) {
        super("BUSY SYNCING", context, isVissible);
        currency = "USD";
        swipeLayout = context.findViewById(R.id.swipe_containerUsd);
        currencyClass = USDtoZAR.class;
    }

    @Override
    protected void setCurrencyToZarCurrency(Object object) {
        usdToZAR = (USDtoZAR) object;
    }

    @Override
    public Double getExchangeRate() {
        return Double.parseDouble(usdToZAR.getUsdZar());
    }

    @Override
    protected boolean isBuzzCheckBoxChecked() {
        CheckBox buzzCheckBox = context.findViewById(R.id.buzzUsd);
        return buzzCheckBox.isChecked();
    }

    @Override
    protected double getSpreadAlertValue() {
        return context.getSharedPreferences(null, Context.MODE_PRIVATE).getFloat("SPREADUSD", 10);
    }

    @Override
    protected ImageView getBCZarTrendImage() {
        return context.findViewById(R.id.BCZARTrendImageUSD);
    }

    @Override
    protected ImageView getBCCurrencyTrendImage() {
        return context.findViewById(R.id.BCUSDTrendImage);
    }

    @Override
    protected EditText getBCCurrencyText() {
        return context.findViewById(R.id.BCUSD);
    }

    @Override
    protected EditText getBCZarEditText() {
        return context.findViewById(R.id.BCZARONUSD);
    }

    @Override
    protected EditText getCurrencyToZarText() {
        return context.findViewById(R.id.USDtoZAR);
    }

    @Override
    protected EditText getSpreadEditText() {
        return context.findViewById(R.id.spreadUsd);
    }

    @Override
    protected EditText getCurrencyBuyText() {
        return context.findViewById(R.id.buyUSD);
    }

    @Override
    protected void setSharedPrefBuyCurrenctValue(String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(null, Context.MODE_PRIVATE).edit();
        editor.putFloat("BUYUSD", new Float(value)).commit();
    }

    @Override
    protected EditText getActualCostRandText() {
        return context.findViewById(R.id.actualCostRandUsd);
    }

    @Override
    protected EditText getBCBoughtText() {
        return context.findViewById(R.id.bitCoinsBoughtUsd);
    }

    @Override
    protected EditText getSellCurrentZarRateText() {
        return context.findViewById(R.id.sellAtCurrentRateUsd);
    }

    @Override
    protected EditText getProfitText() {
        return context.findViewById(R.id.profitUsd);
    }

}
