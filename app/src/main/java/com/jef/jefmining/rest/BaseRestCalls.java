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
}
