package com.hashicode.simpletodo.service;

/**
 * Interface that define a callback used by {@link AsyncTaskService}
 *
 * Created by takahashi on 13/02/16.
 */
public interface AsyncTaskServiceCallback {

    void onPostExecute(Void aVoid);
}
