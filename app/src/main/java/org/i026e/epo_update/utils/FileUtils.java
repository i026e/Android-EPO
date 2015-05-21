package org.i026e.epo_update.utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.MessageDigest;

/**
 * Created by pavel on 18.05.15.
 *
 * some functions to deal with files
 */

public class FileUtils {
    private static char[] hexDigits = "0123456789abcdef".toCharArray();

    public static String getMD5(File file) throws Exception {
        String md5 = "";

        FileInputStream fis = new FileInputStream(file);
        byte[] bytes = new byte[4096];
        int read = 0;
        MessageDigest digest = MessageDigest.getInstance("MD5");

        while ((read = fis.read(bytes)) != -1) {
            digest.update(bytes, 0, read);
        }

        byte[] messageDigest = digest.digest();

        StringBuilder sb = new StringBuilder(32);

        for (byte b : messageDigest) {
            sb.append(hexDigits[(b >> 4) & 0x0f]);
            sb.append(hexDigits[b & 0x0f]);
        }

        fis.close();
        md5 = sb.toString();

        return md5;
    }

    public static String readTextFile(File file) throws Exception{
        FileInputStream fis = new FileInputStream(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }

        reader.close();
        fis.close();

        return sb.toString();
    }

    public static boolean isValidMD5(File data, File md5) throws Exception{
        String actual_md5 = getMD5(data).trim();
        String supposed_md5 = readTextFile(md5).trim();
        return supposed_md5.equals(actual_md5);
    }

    public static File downloadFile(String url_str, String dir, String filename) throws Exception{

            URL url = new URL(url_str);

            File outputFile = new File(dir, filename);

            DataInputStream dis = new DataInputStream(url.openStream());
            FileOutputStream fos = new FileOutputStream(outputFile);

            byte[] buffer = new byte[1024];
            int length;

            while ((length = dis.read(buffer))>0) {
                fos.write(buffer, 0, length);
            }

            fos.flush();
            fos.close();
            dis.close();

            return outputFile;

    }

}
