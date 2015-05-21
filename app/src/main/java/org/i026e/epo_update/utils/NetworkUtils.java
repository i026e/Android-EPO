package org.i026e.epo_update.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by pavel on 20.05.15.
 *
 * some functions to deal with network
 */

public class NetworkUtils {
    public static boolean isConnected(Context context, boolean WiFiOnly){
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if ( activeNetwork != null ) {
            if (WiFiOnly)
                return activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
            return activeNetwork.isConnectedOrConnecting();
        }
        return false;
    }
}
