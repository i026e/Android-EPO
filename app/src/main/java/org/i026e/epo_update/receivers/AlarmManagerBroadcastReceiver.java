package org.i026e.epo_update.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.i026e.epo_update.AsyncResponse;
import org.i026e.epo_update.SettingsManager;
import org.i026e.epo_update.UpdateEPO;
import org.i026e.epo_update.utils.DateUtils;
import org.i026e.epo_update.utils.NetworkUtils;

/**
 * Created by pavel on 19.05.15.
 *
 * Do updates with specified periodicity
 */
public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {
    private static final int MAGIC_NUMBER = 1236547890;
    private static final long INITIAL_DELAY = 30000L;


    @Override
    public void onReceive(Context context, Intent intent) {
        SettingsManager settingsManager = new SettingsManager(context);
        if (!settingsManager.getAutoUpdateEnabled()){
            cancelAlarm(context); // self disabling
        }
        // Check if it is time to update EPO
        if (DateUtils.calculateDaysFromToday(settingsManager.getLastUpdate()) >= settingsManager.getUpdatePeriod()) {
            if (NetworkUtils.isConnected(context, settingsManager.getWifiOnly())){
                updateEPO(context);
            }
            else {
                //waiting for network
                NetworkChangeBroadcastReceiver.enable(context);
            }
        }
    }

    // callback to save time of last update
    private static void onDataUpdate(Context context, Object output){
        if (output != null){
            boolean result = (Boolean) output;
            if (result){
                SettingsManager.setLastUpdate(context, DateUtils.today());
            }
        }

    }

    // do the job
    public static void updateEPO(final Context context){
        AsyncResponse response = new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                onDataUpdate(context, output);
            }
        };
        UpdateEPO uepo = new UpdateEPO(context, response);
        uepo.execute();
    }


    public static void setAlarm(Context context){
        BootUpBroadcastReceiver.enable(context);

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);

        PendingIntent pi = PendingIntent.getBroadcast(context, MAGIC_NUMBER, intent, 0);

        // Set alarm to repeat every day
        // Upon receiving, period since the last update will be checked

        am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, INITIAL_DELAY,
                AlarmManager.INTERVAL_DAY, pi);


    }
    public static void cancelAlarm(Context context) {
        BootUpBroadcastReceiver.disable(context);
        NetworkChangeBroadcastReceiver.disable(context);

        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);

        PendingIntent sender = PendingIntent.getBroadcast(context, MAGIC_NUMBER, intent, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.cancel(sender);

    }


}
