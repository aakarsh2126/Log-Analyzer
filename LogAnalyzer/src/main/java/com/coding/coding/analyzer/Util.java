package com.coding.coding.analyzer;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Util {
    public static String getTimeFromString(String dateTimeString) {
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateTimeString, DateTimeFormatter.ISO_ZONED_DATE_TIME);
        int hour = zonedDateTime.getHour();
        return hour + ":00";
    }
}
