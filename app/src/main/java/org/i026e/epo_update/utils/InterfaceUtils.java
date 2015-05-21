package org.i026e.epo_update.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by pavel on 20.05.15.
 *
 *
 */
public class InterfaceUtils {
    public static void makeToast(Context context, String s){
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }
}
