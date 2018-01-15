package com.jef.jefmining.rest;

import android.app.Activity;

/**
 * Created by ettienne on 2017/12/18.
 */

public abstract class BaseRestCalls extends SyncTask<Void> {


    public BaseRestCalls(String progressMessage, Activity context, boolean isVissible) {
        super(progressMessage, context, isVissible);
    }

    public BaseRestCalls(Activity context) {
        super(context);
    }

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

    protected double getSpread(Double randVal, Double foreignCurrency, double exchangeRate) {
        return (randVal - (foreignCurrency * getFnbExchange(exchangeRate)) - getCost(randVal)) / (randVal - getCost(randVal)) * 100 - 3.65;
    }
}
