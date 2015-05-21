package org.i026e.epo_update.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by pavel on 20.05.15.
 *
 * some functions to deal with dates
 */
public class DateUtils {
    public static Date today(){
        return new Date();
    }

    public static String formattedDate(Date date, String formatting){
        SimpleDateFormat sdf = new SimpleDateFormat(formatting);
        return sdf.format(date);
    }

    /* This method sometimes yields incorrect results
    * but that is not a big deal in our case*/
    public static long calculateDays(Date dateEarly, Date dateLater) {
        return (dateLater.getTime() - dateEarly.getTime()) / (24 * 60 * 60 * 1000);
    }

    public static long calculateDaysFromToday(Date dateEarly) {
        return calculateDays(dateEarly, today());
    }

}
