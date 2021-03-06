package org.i026e.epo_update;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import org.i026e.epo_update.utils.DateUtils;
import org.i026e.epo_update.utils.InterfaceUtils;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * helper methods.
 */
public class UpdateIntentService extends IntentService {
    private static final String ACTION_UPDATE_EPO = "org.i026e.epo_update.action.UPDATE_EPO";

    public UpdateIntentService() {
        super("UpdateIntentService");
    }

    /**
     * Starts this service to perform action startActionUpdate with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionUpdate(Context context) {
        Intent intent = new Intent(context, UpdateIntentService.class);
        intent.setAction(ACTION_UPDATE_EPO);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Context context = getApplicationContext();
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_EPO.equals(action)) {
                try {
                    if (UpdateEPO.update(context)) {
                        SettingsManager.setLastUpdate(context, DateUtils.today());
                    }
                } catch (Exception e) {
                    InterfaceUtils.makeToast(context, e.getLocalizedMessage());
                }

            }
        }
    }

}
