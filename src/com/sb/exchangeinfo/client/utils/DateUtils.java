package com.sb.exchangeinfo.client.utils;


import java.text.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: sbenner
 * Date: 1/20/16
 * Time: 4:08 AM
 */
public class DateUtils {


    public static final String YYYY_MMDD = "yyyyMMdd";

    public static final String YYYY_MMDD_hhmm = "yyyy-MM-dd HH:mm";

    public static String getDayStringFromLong(long ts) {
        Date dt = new Date(ts*1000);
        DateFormat sdf = new SimpleDateFormat(YYYY_MMDD);
        return sdf.format(dt);

    }

    public static String getDayStringFromLongHHmm(long ts) {
        Date dt = new Date(ts*1000);
        DateFormat sdf = new SimpleDateFormat(YYYY_MMDD_hhmm);
        return sdf.format(dt);

    }



}

