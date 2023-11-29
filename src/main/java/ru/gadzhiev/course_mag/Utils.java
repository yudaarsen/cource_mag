package ru.gadzhiev.course_mag;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class Utils {

    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (Exception e) {
            return null;
        }
    }

}
