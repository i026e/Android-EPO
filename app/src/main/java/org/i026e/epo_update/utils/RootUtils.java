package org.i026e.epo_update.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.util.Formatter;


/**
 *
 * some functions for root access
 */

public abstract class RootUtils {
    private static boolean rootCommand(String command) throws Exception {
        boolean retval = false;

        String[] su_cp = { "su", "-c", command };
        Process suProcess = Runtime.getRuntime().exec(su_cp);
        DataOutputStream os = new DataOutputStream(suProcess.getOutputStream());

        os.flush();

        os.writeBytes("exit\n");
        os.flush();

        int suProcessRetval = suProcess.waitFor();
        if (255 != suProcessRetval)
        {
            // Root access granted
            retval = true;
        }
        else
        {
            // Root access denied
            retval = false;
        }

        //}
        return retval;
    }

    public static boolean rootCopy(File file, String path) throws Exception{
        String rootCopyCommand =  "cp \"%s\" \"%s\" ";
        Formatter f = new Formatter();
        String command = f.format(rootCopyCommand, file.getAbsolutePath(), path).toString();
        return rootCommand(command);
    }

    public static boolean rootChMod(String path, String mod) throws Exception{
        String rootCopyCommand =  "chmod \"%s\" \"%s\" ";
        Formatter f = new Formatter();
        String command = f.format(rootCopyCommand, mod, path).toString();
        return rootCommand(command);
    }

    public static boolean rootChOwn(String path, String owner, String group) throws Exception{
        String rootCopyCommand =  "chown \"%s\":\"%s\" \"%s\" ";
        Formatter f = new Formatter();
        String command = f.format(rootCopyCommand, owner,group, path).toString();
        return rootCommand(command);
    }

    public static boolean validRoot(){
        boolean retval = false;
        Process suProcess;

        try
        {
            suProcess = Runtime.getRuntime().exec("su");

            DataOutputStream os = new DataOutputStream(suProcess.getOutputStream());
            DataInputStream osRes = new DataInputStream(suProcess.getInputStream());

            if (null != os && null != osRes)
            {
                // Getting the id of the current user to check if this is root
                os.writeBytes("id\n");
                os.flush();

                String currUid = osRes.readLine();
                boolean exitSu = false;
                if (null == currUid)
                {
                    retval = false;
                    exitSu = false;
                }
                else if (true == currUid.contains("uid=0"))
                {
                    retval = true;
                    exitSu = true;
                }
                else
                {
                    retval = false;
                    exitSu = true;
                }

                if (exitSu)
                {
                    os.writeBytes("exit\n");
                    os.flush();
                }
            }
        }
        catch (Exception e)
        {
            // Can't get root !
            // Probably broken pipe exception on trying to write to output stream (os) after su failed, meaning that the device is not rooted

            retval = false;
        }

        return retval;
    }
}
