package com.philliphsu.clock2;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.philliphsu.clock2.alarms.AlarmsAdapter;
import com.philliphsu.clock2.model.DatabaseManager;
import com.philliphsu.clock2.util.AlarmUtils;

/**
 * Created by Phillip Hsu on 7/1/2016.
 *
 * TODO: Generify this class for any item.
 */
public final class AsyncRecyclerViewItemChangeHandler {

    private final Context mContext;
    private final AlarmsAdapter mAdapter;
    private final View mSnackbarAnchor;

    /**
     * @param snackbarAnchor an optional anchor for a Snackbar to anchor to
     */
    public AsyncRecyclerViewItemChangeHandler(AlarmsAdapter adapter, View snackbarAnchor) {
        mContext = snackbarAnchor.getContext();
        mAdapter = adapter;
        mSnackbarAnchor = snackbarAnchor;
    }

    public void asyncAddAlarm(final Alarm alarm) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... params) {
                return DatabaseManager.getInstance(mContext).insertAlarm(alarm);
            }

            @Override
            protected void onPostExecute(Long aLong) {
                // TODO: Snackbar/Toast here? If so, remove the code in AlarmUtils.scheduleAlarm() that does it.
                mAdapter.addItem(alarm);
                AlarmUtils.scheduleAlarm(mContext, alarm, true);
            }
        }.execute();
    }

    public void asyncUpdateAlarm(final Alarm oldAlarm, final Alarm newAlarm) {
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... params) {
                mAdapter.updateItem(oldAlarm, newAlarm);
                // Save the item to the db
                return DatabaseManager.getInstance(mContext).updateAlarm(oldAlarm.id(), newAlarm);
            }

            @Override
            protected void onPostExecute(Integer integer) {
                // TODO: Snackbar/Toast here? If so, remove the code in AlarmUtils.scheduleAlarm() that does it.
            }
        }.execute();
    }

    public void asyncRemoveAlarm(final Alarm alarm) {
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... params) {
                mAdapter.removeItem(alarm);
                // Save the item to the db
                return DatabaseManager.getInstance(mContext).deleteAlarm(alarm);
            }

            @Override
            protected void onPostExecute(Integer integer) {
                // TODO: Snackbar
                if (mSnackbarAnchor != null) {
                    String message = mContext.getString(R.string.snackbar_item_deleted,
                            mContext.getString(R.string.alarm));
                    Snackbar.make(mSnackbarAnchor, message, Snackbar.LENGTH_LONG)
                            .setAction(R.string.snackbar_undo_item_deleted, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DatabaseManager.getInstance(mContext).insertAlarm(alarm);
                                    if (alarm.isEnabled()) {
                                        AlarmUtils.scheduleAlarm(mContext, alarm, true);
                                    }
                                }
                            }).show();
                }
            }
        }.execute();
    }
}