package com.philliphsu.clock2.alarms;

import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.Button;

import com.philliphsu.clock2.Alarm;
import com.philliphsu.clock2.R;
import com.philliphsu.clock2.model.AlarmLoader;
import com.philliphsu.clock2.ringtone.RingtoneActivity;
import com.philliphsu.clock2.util.AlarmController;

public class AlarmActivity extends RingtoneActivity<Alarm> {

    private AlarmController mAlarmController;
    // TODO: Write a getter method instead in the base class?
    private Alarm mAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAlarmController = new AlarmController(this, null);
        // TODO: Butterknife binding
        Button snooze = (Button) findViewById(R.id.btn_snooze);
        snooze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snooze();
            }
        });
        Button dismiss = (Button) findViewById(R.id.btn_dismiss);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public Loader<Alarm> onCreateLoader(long id) {
        return new AlarmLoader(this, id);
    }

    @Override
    public void onLoadFinished(Loader<Alarm> loader, Alarm data) {
        super.onLoadFinished(loader, data);
        mAlarm = data;
        if (data != null) {
            // TODO: If the upcoming alarm notification isn't present, verify other notifications aren't affected.
            // This could be the case if we're starting a new instance of this activity after leaving the first launch.
            mAlarmController.removeUpcomingAlarmNotification(data);
        }
    }

    @Override
    public int layoutResource() {
        return R.layout.activity_ringtone;
    }

    private void snooze() {
        if (mAlarm != null) {
            mAlarmController.snoozeAlarm(mAlarm);
        }
        // Can't call dismiss() because we don't want to also call cancelAlarm()! Why? For example,
        // we don't want the alarm, if it has no recurrence, to be turned off right now.
        stopAndFinish();
    }

    private void dismiss() {
        if (mAlarm != null) {
            // TODO do we really need to cancel the intent and alarm?
            mAlarmController.cancelAlarm(mAlarm, false);
        }
        stopAndFinish();
    }
}