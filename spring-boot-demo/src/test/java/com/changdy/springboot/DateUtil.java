package com.changdy.springboot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Changdy on 2018/6/29.
 */
public class DateUtil {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static String getDateLimit() {
        Calendar calendar = Calendar.getInstance();
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        String dateLimit = calendar.get(Calendar.YEAR) + "-";
        int month = calendar.get(Calendar.MONTH);
        if (dayOfMonth <= 10) {
            dateLimit += String.format("%02d", month);
        } else {
            dateLimit += String.format("%02d", ++month);
        }
        dateLimit += "-01";
        return dateLimit;
    }

    public static String getCurrentDate() {
        return DATE_FORMAT.format(new Date());
    }
}
