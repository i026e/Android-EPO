package org.i026e.epo_update.receivers;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import org.i026e.epo_update.SettingsManager;
import org.i026e.epo_update.UpdateIntentService;
import org.i026e.epo_update.utils.NetworkUtils;

/**
 * Created by pavel on 20.05.15.
 *
 * Check for network connection
 * and do update when it is possible
 */
public class NetworkChangeBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (NetworkUtils.isConnected(context, SettingsManager.getWifiOnly(context))){
            // do the job
            UpdateIntentService.startActionUpdate(context);

            // Self disabling
            disable(context);
        }
    }

    public static void enable(Context context){
        ComponentName receiver = new ComponentName(context, NetworkChangeBroadcastReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }
    public static void disable(Context context){
        ComponentName receiver = new ComponentName(context, NetworkChangeBroadcastReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
}
