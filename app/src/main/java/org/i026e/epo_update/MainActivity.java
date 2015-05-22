package org.i026e.epo_update;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import org.i026e.epo_update.receivers.AlarmManagerBroadcastReceiver;
import org.i026e.epo_update.utils.DateUtils;
import org.i026e.epo_update.utils.InterfaceUtils;
import org.i026e.epo_update.utils.RootUtils;


public class MainActivity extends Activity implements CompoundButton.OnCheckedChangeListener {

    SettingsManager mSettingsManager;
    TextView informationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSettingsManager = new SettingsManager(this);
        informationView = (TextView) findViewById(R.id.last_update_date);

        loadSettings();


    }
    private void loadSettings(){
        Switch settings_sw = (Switch) findViewById(R.id.switch_auto);
        settings_sw.setOnCheckedChangeListener(this);

        settings_sw.setChecked(mSettingsManager.getAutoUpdateEnabled());

        CheckBox wifi_cb = (CheckBox) findViewById(R.id.wifi_only);
        wifi_cb.setChecked(mSettingsManager.getWifiOnly());

        int update_period = mSettingsManager.getUpdatePeriod();
        RadioButton period_rb = null;

        switch (update_period) {
            case SettingsManager.DAILY:
                period_rb = (RadioButton) findViewById(R.id.daily);
                break;
            case SettingsManager.WEEKLY:
                period_rb = (RadioButton) findViewById(R.id.weekly);
                break;
            case SettingsManager.MONTHLY:
                period_rb = (RadioButton) findViewById(R.id.monthly);
        }
        if (period_rb != null){
            period_rb.setChecked(true);
        }

    }
    private void redrawInformationView(){
        String last_update_date = DateUtils.formattedDate(mSettingsManager.getLastUpdate(),
                getString(R.string.date_format));
        informationView.setText(last_update_date);
    }

    @Override
    protected  void onStart(){
        super.onStart();
        redrawInformationView();
    }
    @Override
    protected void onPause(){
        setAlarms();
        super.onPause();
    }

    // set/stop autoupdate
    private void setAlarms(){
        if (mSettingsManager.getAutoUpdateEnabled()){
            AlarmManagerBroadcastReceiver.setAlarm(getApplicationContext());
            InterfaceUtils.makeToast(this, getString(R.string.audoupdate_set));
        }
        else{
            AlarmManagerBroadcastReceiver.cancelAlarm(getApplicationContext());
            //InterfaceUtils.makeToast(this, getString(R.string.audoupdate_reset));
        }
    }
    // callback function called when epo file is updated
    private void onDataUpdate(Object output){
        if (output != null){
            boolean result = (Boolean) output;
            if (result){
                mSettingsManager.setLastUpdate(DateUtils.today());
                redrawInformationView();
            }
        }

    }

    // when user presses update now button
    public void onForceUpdate (View v){
        AsyncResponse response = new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                    onDataUpdate(output);
            }
        };
        UpdateEPO  uepo = new UpdateEPO(getBaseContext(), response);
        uepo.execute();

    }

    // show / hide layout
    private void setSettingsVisibility(boolean visible){
        LinearLayout params_layout = (LinearLayout) findViewById(R.id.params_layout);
        if (visible) {
            params_layout.setVisibility(View.VISIBLE);
        }
        else{
            params_layout.setVisibility(View.INVISIBLE);
        }
    }

    // when user changes autoupdate settings
    public void onSettingsChange (View view){
        boolean checked;
        switch (view.getId()){
            case  R.id.wifi_only :
                checked = ((CheckBox) view).isChecked();
                mSettingsManager.setWifiOnly(checked);
                break;
            case R.id.daily:
                checked = ((RadioButton) view).isChecked();
                if (checked){
                    mSettingsManager.setUpdatePeriod(SettingsManager.DAILY);
                }
                break;
            case R.id.weekly:
                checked = ((RadioButton) view).isChecked();
                if (checked){
                    mSettingsManager.setUpdatePeriod(SettingsManager.WEEKLY);
                }
                break;
            case R.id.monthly:
                checked = ((RadioButton) view).isChecked();
                if (checked){
                    mSettingsManager.setUpdatePeriod(SettingsManager.MONTHLY);
                }
                break;
        }
    }

    // when user enables/disables autoupdate
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.switch_auto){

            if (isChecked && !RootUtils.validRoot()){
                mSettingsManager.setAutoUpdateEnabled(false);
                setSettingsVisibility(false);
                buttonView.setChecked(false);
                InterfaceUtils.makeToast(this, getString(R.string.no_root));
            } else {
                mSettingsManager.setAutoUpdateEnabled(isChecked);
                setSettingsVisibility(isChecked);
            }
            setAlarms();
        }
    }
}
