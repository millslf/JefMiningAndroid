package com.jef.jefmining.rest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

/**
 * Created by ettienne on 16/07/08.
 */
public abstract class SyncTask<T> extends AsyncTask<Object, Void, T> {

    public Activity context;
    public ProgressDialog dialog;
    public Exception exception;
    public String progressMessage;
    public boolean isVissible;

    public SyncTask(String progressMessage, Activity context, boolean isVissible) {
        this.context = context;
        if (isVissible) {
            this.dialog = new ProgressDialog(context);
            this.dialog.setCancelable(false);
            this.progressMessage = progressMessage;
            this.isVissible = isVissible;
        }
    }

    public SyncTask(Activity context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {

        if (dialog != null) {
            this.dialog.setMessage(progressMessage);
            this.dialog.show();
        }
    }

    @Override
    protected T doInBackground(Object... objects) {
        try {
            return doWork(objects);
        } catch (Exception e) {
            exception = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(T result) {

        if (exception == null) {
            onResult(result);
        } else {
            onError();
        }
        if (dialog != null && dialog.isShowing()) dialog.dismiss();
    }

    public abstract T doWork(Object... objects) throws Exception;

    public abstract void onResult(T result);

    public abstract void onError();
}