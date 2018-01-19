package com.jef.jefmining.rest;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.jef.jefmining.R;
import com.jef.jefmining.currency.EURtoZAR;

/**
 * Created by ettienne on 2017/12/18.
 */

public class EURORestCalls extends BaseRestCalls {

    private EURtoZAR eurToZAR;

    public EURORestCalls(SwipeRefreshLayout swipeRefreshLayout, Activity context, boolean isVissible) {
        super("BUSY SYNCING", context, isVissible);
        currency = "EUR";
        swipeLayout = swipeRefreshLayout;
        currencyClass = EURtoZAR.class;
    }

    @Override
    protected void setCurrencyToZarCurrency(Object object) {
        eurToZAR = (EURtoZAR) object;
    }

    @Override
    public Double getExchangeRate() {
        return Double.parseDouble(eurToZAR.getEurZar());
    }

    @Override
    protected boolean isBuzzCheckBoxChecked() {
        CheckBox buzzCheckBox = context.findViewById(R.id.buzzEURO);
        return buzzCheckBox.isChecked();
    }

    @Override
    protected double getSpreadAlertValue() {
        return context.getSharedPreferences(null, Context.MODE_PRIVATE).getFloat("SPREADEURO", 10);
    }

    @Override
    protected ImageView getBCZarTrendImage() {
        return context.findViewById(R.id.BCZARTrendImageEUR);
    }

    @Override
    protected ImageView getBCCurrencyTrendImage() {
        return context.findViewById(R.id.BCEURTrendImage);
    }

    @Override
    protected EditText getBCCurrencyText() {
        return context.findViewById(R.id.BCEURO);
    }

    @Override
    protected EditText getBCZarEditText() {
        return context.findViewById(R.id.BCZARONEUR);
    }

    @Override
    protected EditText getCurrencyToZarText() {
        return context.findViewById(R.id.EUROtoZAR);
    }

    @Override
    protected EditText getSpreadEditText() {
        return context.findViewById(R.id.spreadeur);
    }

    @Override
    protected EditText getCurrencyBuyText() {
        return context.findViewById(R.id.buyEURO);
    }

    @Override
    protected void setSharedPrefBuyCurrenctValue(String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(null, Context.MODE_PRIVATE).edit();
        editor.putFloat("BUYEURO", new Float(value)).commit();
    }

    @Override
    protected EditText getActualCostRandText() {
        return context.findViewById(R.id.actualCostRandEuro);
    }

    @Override
    protected EditText getBCBoughtText() {
        return context.findViewById(R.id.bitCoinsBoughtEur);
    }

    @Override
    protected EditText getSellCurrentZarRateText() {
        return context.findViewById(R.id.sellAtCurrentRateEuro);
    }

    @Override
    protected EditText getProfitText() {
        return context.findViewById(R.id.profitEuro);
    }


}
