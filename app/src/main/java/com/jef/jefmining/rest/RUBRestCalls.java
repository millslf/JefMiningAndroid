package com.jef.jefmining.rest;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.jef.jefmining.R;
import com.jef.jefmining.currency.RUBtoZAR;

/**
 * Created by ettienne on 2017/12/18.
 */

public class RUBRestCalls extends BaseRestCalls {
    private RUBtoZAR rubToZAR;
    private boolean allSyncDone = true;

    public RUBRestCalls(SwipeRefreshLayout swipeRefreshLayout, Activity context, boolean isVissible) {
        super("BUSY SYNCING", context, isVissible);
        currency = "RUB";
        swipeLayout = swipeRefreshLayout;
        currencyClass = RUBtoZAR.class;
    }

    @Override
    protected void setCurrencyToZarCurrency(Object object) {
        rubToZAR = (RUBtoZAR) object;
    }

    @Override
    public Double getExchangeRate() {
        return Double.parseDouble(rubToZAR.getRubZar());
    }

    @Override
    protected boolean isBuzzCheckBoxChecked() {
        CheckBox buzzCheckBox = context.findViewById(R.id.buzzRub);
        return buzzCheckBox.isChecked();
    }

    @Override
    protected double getSpreadAlertValue() {
        return context.getSharedPreferences(null, Context.MODE_PRIVATE).getFloat("SPREADRUB", 10);
    }

    @Override
    protected ImageView getBCZarTrendImage() {
        return context.findViewById(R.id.BCZARTrendImageRUB);
    }

    @Override
    protected ImageView getBCCurrencyTrendImage() {
        return context.findViewById(R.id.BCRUBTrendImage);
    }

    @Override
    protected EditText getBCCurrencyText() {
        return context.findViewById(R.id.BCRUB);
    }

    @Override
    protected EditText getBCZarEditText() {
        return context.findViewById(R.id.BCZARONRUB);
    }

    @Override
    protected EditText getCurrencyToZarText() {
        return context.findViewById(R.id.RUBtoZAR);
    }

    @Override
    protected EditText getSpreadEditText() {
        return context.findViewById(R.id.spreadRub);
    }

    @Override
    protected EditText getCurrencyBuyText() {
        return context.findViewById(R.id.buyRUB);
    }

    @Override
    protected void setSharedPrefBuyCurrenctValue(String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(null, Context.MODE_PRIVATE).edit();
        editor.putFloat("BUYRUB", new Float(value)).commit();
    }

    @Override
    protected EditText getActualCostRandText() {
        return context.findViewById(R.id.actualCostRandRub);
    }

    @Override
    protected EditText getBCBoughtText() {
        return context.findViewById(R.id.bitCoinsBoughtRub);
    }

    @Override
    protected EditText getSellCurrentZarRateText() {
        return context.findViewById(R.id.sellAtCurrentRateRub);
    }

    @Override
    protected EditText getProfitText() {
        return context.findViewById(R.id.profitRub);
    }

}
