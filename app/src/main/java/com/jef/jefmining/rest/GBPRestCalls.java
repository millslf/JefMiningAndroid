package com.jef.jefmining.rest;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.jef.jefmining.R;
import com.jef.jefmining.currency.GBPtoZAR;

;

/**
 * Created by ettienne on 2017/12/18.
 */

public class GBPRestCalls extends BaseRestCalls {
    private GBPtoZAR gbpToZAR;
    private boolean allSyncDone = true;

    public GBPRestCalls(Activity context, boolean isVissible) {
        super("BUSY SYNCING", context, isVissible);
        currency = "GBP";
        swipeLayout = context.findViewById(R.id.swipe_containerGbp);
        currencyClass = GBPtoZAR.class;
    }

    @Override
    protected void setCurrencyToZarCurrency(Object object) {
        gbpToZAR = (GBPtoZAR) object;
    }

    @Override
    public Double getExchangeRate() {
        return Double.parseDouble(gbpToZAR.getGbpZar());
    }

    @Override
    protected boolean isBuzzCheckBoxChecked() {
        CheckBox buzzCheckBox = context.findViewById(R.id.buzzGbp);
        return buzzCheckBox.isChecked();
    }

    @Override
    protected double getSpreadAlertValue() {
        return context.getSharedPreferences(null, Context.MODE_PRIVATE).getFloat("SPREADGBP", 10);
    }

    @Override
    protected ImageView getBCZarTrendImage() {
        return context.findViewById(R.id.BCZARTrendImageGBP);
    }

    @Override
    protected ImageView getBCCurrencyTrendImage() {
        return context.findViewById(R.id.BCGBPTrendImage);
    }

    @Override
    protected EditText getBCCurrencyText() {
        return context.findViewById(R.id.BCGBP);
    }

    @Override
    protected EditText getBCZarEditText() {
        return context.findViewById(R.id.BCZARONGBP);
    }

    @Override
    protected EditText getCurrencyToZarText() {
        return context.findViewById(R.id.GBPtoZAR);
    }

    @Override
    protected EditText getSpreadEditText() {
        return context.findViewById(R.id.spreadGbp);
    }

    @Override
    protected EditText getCurrencyBuyText() {
        return context.findViewById(R.id.buyGBP);
    }

    @Override
    protected void setSharedPrefBuyCurrenctValue(String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(null, Context.MODE_PRIVATE).edit();
        editor.putFloat("BUYGBP", new Float(value)).commit();
    }

    @Override
    protected EditText getActualCostRandText() {
        return context.findViewById(R.id.actualCostRandGbp);
    }

    @Override
    protected EditText getBCBoughtText() {
        return context.findViewById(R.id.bitCoinsBoughtGbp);
    }

    @Override
    protected EditText getSellCurrentZarRateText() {
        return context.findViewById(R.id.sellAtCurrentRateGbp);
    }

    @Override
    protected EditText getProfitText() {
        return context.findViewById(R.id.profitGbp);
    }


}
