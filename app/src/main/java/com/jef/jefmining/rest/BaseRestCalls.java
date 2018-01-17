package com.jef.jefmining.rest;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;

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

        } finally {
            executorService.shutdown();
            new Handler(Looper.getMainLooper()).post(() -> {
                if (context != null) {
                    if (swipeLayout != null) {
                        swipeLayout.setRefreshing(false);
                    }
                }
            });
        }
        return null;
    }

    abstract protected void setCurrencyToZarCurrency(Object object);
}
