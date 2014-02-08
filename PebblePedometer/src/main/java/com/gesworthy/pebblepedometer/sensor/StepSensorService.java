package com.gesworthy.pebblepedometer.sensor;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

import com.gesworthy.pebblepedometer.database.DatabaseHelper;
import com.gesworthy.pebblepedometer.model.DayRecord;

import org.joda.time.DateTime;

import java.util.Date;

/**
 * The StepSensorService will always run in the background counting stepsToday.
 * <p/>
 * This service will also be responsible for resetting the count every night at
 * midnight.
 * <p/>
 * Created by Gary Esworthy on 1/20/14.
 */
public class StepSensorService extends Service implements SensorEventListener {

    /**
     * Target we publish for clients to send messages to IncomingHandler.
     */
    final Messenger mMessenger = new Messenger(new IncomingHandler());

    private int stepsToday;
    private long dateToday;

    public StepSensorService() {
        super();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        stepsToday = stepsToday + (int) sensorEvent.values[0];

        Intent i = new Intent("stepsToday");
        Bundle bundle = new Bundle();
        bundle.putInt("stepsToday", stepsToday);
        i.putExtras(bundle);
        sendBroadcast(i);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // Ignore
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    public int getStepsToday() {
        return stepsToday;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor s = sm.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        sm.registerListener(this, s, SensorManager.SENSOR_DELAY_NORMAL);

        createMidnightAlarm(getApplicationContext());
    }

    private static void createMidnightAlarm(Context context) {
        // Get tomorrow's date
        DateTime date = new DateTime().toDateMidnight().toDateTime();
        DateTime tomorrow = date.plusDays(1);

        // Register alarm
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, tomorrow.getMillis(), PendingIntent.getBroadcast(
                context.getApplicationContext(), 10, new Intent(context.getApplicationContext(), DayChangeMessageReceiver.class),
                PendingIntent.FLAG_UPDATE_CURRENT));
    }

    /**
     * Handler of incoming messages from clients.
     */
    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            // TODO
        }
    }

    /**
     * Handle
     */
    class DayChangeMessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // Save today's number in the database
            DayRecord day = new DayRecord(dateToday, stepsToday);
            DatabaseHelper helper = new DatabaseHelper(context);
            helper.addRecord(day);

            // Reset the count and date and broadcast the new number
            dateToday = new DateTime().toDateMidnight().toDateTime().getMillis();
            stepsToday = 0;

            Intent i = new Intent("stepsToday");
            Bundle bundle = new Bundle();
            bundle.putInt("stepsToday", stepsToday);
            i.putExtras(bundle);
            sendBroadcast(i);

            // Reset the alarm
            createMidnightAlarm(context);
        }
    }
}
