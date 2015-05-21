package org.i026e.epo_update;

import android.content.Context;
import android.os.AsyncTask;

import org.i026e.epo_update.utils.FileUtils;
import org.i026e.epo_update.utils.InterfaceUtils;
import org.i026e.epo_update.utils.RootUtils;

import java.io.File;

/**
 * Created by pavel on 18.05.15.
 *
 * Do the actual work on updating EPO files
 */
public class UpdateEPO  extends AsyncTask<Void, String, Boolean> {
    private final static String EPO_DAT_URL = "http://epodownload.mediatek.com/EPO.DAT";
    private final static String EPO_MD5_URL = "http://epodownload.mediatek.com/EPO.MD5";

    private final static  String DAT_NAME = "EPO.DAT";
    private final static  String MD5_NAME = "EPO.MD5";

    private final static  String EPO_FILE_PATH = "/data/misc/EPO.DAT";
    private final static  String FILE_OWNER = "system";
    private final static  String FILE_MOD = "664";


    Context context;
    public AsyncResponse delegate = null;//Call back interface

    public  UpdateEPO(Context context, AsyncResponse delegate){
        this.context = context;
        this.delegate = delegate;
    }
    @Override
    protected Boolean doInBackground(Void... v) {
        if (RootUtils.validRoot()) {
            try {
                //downloading epo and its md5
                File data = FileUtils.downloadFile(EPO_DAT_URL, String.valueOf(context.getCacheDir()), DAT_NAME);
                File md5 = FileUtils.downloadFile(EPO_MD5_URL, String.valueOf(context.getCacheDir()), MD5_NAME);
                if (data != null && md5 != null) { //check they actually downloaded
                    if (FileUtils.isValidMD5(data, md5)) { // and check if data file correct
                        // copy file, set permissions and owner
                        if (RootUtils.rootCopy(data, EPO_FILE_PATH)
                                && RootUtils.rootChMod(EPO_FILE_PATH, FILE_MOD)
                                && RootUtils.rootChOwn(EPO_FILE_PATH, FILE_OWNER, FILE_OWNER)) {
                            publishProgress(context.getString(R.string.ok));
                            return true;
                        }
                    }
                }

            } catch (Exception e) {
                publishProgress(e.getLocalizedMessage());
            }
        }
        publishProgress(context.getString(R.string.problem));
        return false;
    }

    @Override
    protected void onProgressUpdate(String... message) {
        // make a toast
        InterfaceUtils.makeToast(context, message[0]);
    }
    @Override
    protected void onPostExecute(Boolean result){
        delegate.processFinish(result);
    }

}
