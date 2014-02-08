package com.gesworthy.pebblepedometer.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

/**
 * Created by Gary on 1/29/14.
 */
public class StepMessageReceiver extends BroadcastReceiver
{
    private TextView count;

    public StepMessageReceiver() {}

    public StepMessageReceiver(TextView count) {
        this.count = count;
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        int steps = intent.getExtras().getInt("steps");
        count.setText(String.valueOf(steps));
    }
}